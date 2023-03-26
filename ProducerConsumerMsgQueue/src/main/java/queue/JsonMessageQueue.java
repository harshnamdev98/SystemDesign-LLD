package queue;

import consumer.Consumer;
import data.Message;
import exception.*;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class JsonMessageQueue implements Queue {
    private Map<String, MessageSubscriberDetails> messagesSubscriberDetailsMap;
    private BlockingDeque<String> inMemoryQueue;
    private ObjectMapper objectMapper;

    private static final int RETRY_COUNT = 1;

    public JsonMessageQueue(int queueCapacity) {
        this.inMemoryQueue =  new LinkedBlockingDeque<>(queueCapacity);
        messagesSubscriberDetailsMap = new ConcurrentHashMap<>();
        objectMapper = new ObjectMapper();
        System.out.println("Queue Created with total capacity - " + inMemoryQueue.remainingCapacity());
    }

    /**
     *         Set Queue Capacity if new capcity is greater than current size of queue
     *         Copy All items to new Queue of size newCapacity
     * @param size
     */
    @Override
    public void setQueueCapacity(int size){
        synchronized (this) {
            try {
                checkIfNewCapacityIsValid(size);
                BlockingDeque<String> newQueue = new LinkedBlockingDeque<String>(size);
                while (!inMemoryQueue.isEmpty()){
                    newQueue.put(inMemoryQueue.remove());
                }
                inMemoryQueue = newQueue;
                System.out.println("Changed Queue capacity to - "+size);
            }
            catch (InterruptedException interpt) {
                System.err.println("Exception occurred during resizing of Queue. Queue not resized.");
            }
            catch (NewQueueCapacityLessThanCurrentQueueSize lessCapExc) {
                System.err.println(lessCapExc.getMessage());
            }
        }
    }

    private void checkIfNewCapacityIsValid(int newCapacity)throws NewQueueCapacityLessThanCurrentQueueSize{
        if(newCapacity<inMemoryQueue.size()) {
            throw new NewQueueCapacityLessThanCurrentQueueSize("Cannot set queue capacity to -" + newCapacity +
                    " as it is less than current queue size - " + inMemoryQueue.size());
        }
    }

    @Override
    public int getRemainingSize() {
        return inMemoryQueue.remainingCapacity();
    }

    @Override
    public int getQueueSize() {
        return inMemoryQueue.size();
    }

    /**
     *         Add Message to Queue with its unique messageName/routingKey for which the consumers have subscribed for.
     *         Convert the Message to Json and add to Queue
     * @param message
     * @param routingKey
     */
    @Override
    public void addMessage(Message message, String routingKey) {
        try {
            String queueMessage = getJsonMessage(message, routingKey);
            inMemoryQueue.put(queueMessage);
            System.out.println("Produced Message - " + queueMessage);
            startConsumerThread();
        } catch (InterruptedException | IOException e) {
            System.err.println("Exception occurred during addition of message with messageName -"
                    + routingKey + " to queue, discarding message");
        }
    }

    private String getJsonMessage(QueueMessage message) throws IOException {
        return objectMapper.writeValueAsString(message);

    }
    private String getJsonMessage(Message message, String routingKey) throws IOException {
        QueueMessage queueMessage= new QueueMessage(routingKey, RETRY_COUNT, message);
        return objectMapper.writeValueAsString(queueMessage);
    }

    private void startConsumerThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                consume();
            }
        }).start();
    }

    /*
        Consume topmost message from Queue. As its Blocking queue, it will wait if queue is empty.
        1. Read Json from Queue
        2. Convert it to QueueMessage
        3. Check If retry counter is less than 0. If yes, discard message
        4. Check If subscriber list for this message is empty, if yes, then throw NoSubscriberFoundException
        5. For all subscribers, start a new thread and call their callBack Method
            5.1 For each subscriber, wait till its dependent subscriber is finished with consumption.
            5.2 When subscriber is done consuming, remove subscriber from subscriberSet.
                So that when dependent subscriber checks this set, it comes to know that this subscribed has already consumed
         6. If message is not subscribed by any consumer, add back to queue by decrementing retryCounter by 1
     */
    private void consume() {
        String jsonQueueMessage = null;
        QueueMessage queueMessage = null;

        try {
            //1. Get msg from Queue
            jsonQueueMessage = inMemoryQueue.take();

            //2. Convert jsonqmsg to qmsg
            queueMessage = objectMapper.readValue(jsonQueueMessage, QueueMessage.class);

            //3. check if msg is valid for consumption
            checkIfMessageIsValid(queueMessage);

            //4. Get subscribed consumers for this msg
            final String routingKey = queueMessage.getRoutingKey();
            final Message message = queueMessage.getPayload();
            MessageSubscriberDetails messageSubscriberDetails = messagesSubscriberDetailsMap.get(routingKey);

            //5. Check if subscriber list for this message is empty. if yes, then throw NoSubscriber foundException
            checkIfAnyConsumerHasSubscribedToMessage(routingKey, messageSubscriberDetails);

            //6. For all subscriber, call their callback method for consumption
            // clone the msgsubcsdetails , so when consumers are removed from subscriber set, during consumption,
            // original list remains intact for other messages
            final MessageSubscriberDetails messageSubscriberDetails1 = (MessageSubscriberDetails) messageSubscriberDetails.clone();

            for(Map.Entry<Consumer, String> subscribedConsumer: messageSubscriberDetails1.getConsumerCallbackDetails().entrySet()) {
                // Get callback method details
                final Consumer consumer = subscribedConsumer.getKey();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        consume(message, routingKey, consumer, messageSubscriberDetails1);
                    }
                }).start();
            }

        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } catch (JsonMappingException e) {
            System.err.println(e.getMessage());
        } catch (JsonParseException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MessageNotValidException e) {
            System.err.println(e.getMessage());
        } catch (NoSubscriberFoundException e) {
            System.err.println(e.getMessage());
            retryMessage(queueMessage);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        Decrement retry count by 1 and add message back to queue by converting it into Json
     */
    private void retryMessage(QueueMessage queueMessage) {
        queueMessage.setRetryCount(queueMessage.getRetryCount() - 1);
        try {
            String jsonMessage = getJsonMessage(queueMessage);
            inMemoryQueue.put(jsonMessage);
            System.out.println("Added Message - " + jsonMessage+" back to Queue");
            startConsumerThread();
        } catch (InterruptedException | IOException e) {
            System.err.println("Exception occurred during addition of message with messageName -"
                    + queueMessage.getRoutingKey() + " to queue, discarding message");
        }
    }

    private void consume(Message message, String routingKey, Consumer consumer, MessageSubscriberDetails messageSubscriberDetails) {
        Set<Consumer> subscribedConsumers = messageSubscriberDetails.getSubscribedConsumer();
        synchronized (subscribedConsumers) {
            try {
                /*
                    Wait till all dependent Subscribers are done with their consumption
                 */
                for (Consumer dependentConsumer : messageSubscriberDetails.getDependentConsumer().get(consumer)) {
                    while (subscribedConsumers.contains(dependentConsumer)) {
                        subscribedConsumers.wait();
                    }
                }

                /*
                  Call callback method of consumer for consumption and supress any exception if occurred
                 */
                String callBackMethod = messageSubscriberDetails.getConsumerCallbackDetails().get(consumer);
                try {
                    consumer.getClass().getMethod(callBackMethod, Message.class).invoke(consumer, message);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    System.err.println("Exception occurred during consumption of messageFromQueue - " + routingKey +
                            " Cannot find callback method " + callBackMethod + " for consumer - " + consumer.getConsumerName());
                } catch (Exception e) {
                    System.err.println("Exception occurred during consumption of messageFromQueue for "
                            + consumer.getConsumerName() + " for message " + routingKey);
                }
            } catch (InterruptedException e) {
                System.err.println("InterruptedException occurred during consumption of messageFromQueue for "
                        + consumer.getConsumerName() + " for message " + routingKey);
            }

            /*
                Remove current consumer from subscribedConsumers as it will let others know that this consumer has finished consuming
             */
            subscribedConsumers.remove(consumer);
            /*
                notify all other threads
             */
            subscribedConsumers.notifyAll();
        }
    }


    private void checkIfMessageIsValid(QueueMessage qmsg) throws MessageNotValidException{
        if(qmsg.getRetryCount()<=0){
            throw new MessageNotValidException("Message " + qmsg + " has maxed out retryCount. Discarding it");
        }
    }

    private void checkIfAnyConsumerHasSubscribedToMessage(String routingKey, MessageSubscriberDetails details)
            throws NoSubscriberFoundException {
        if (details == null || details.getSubscribedConsumer() == null || details.getSubscribedConsumer().size()==0){
            throw new NoSubscriberFoundException("No Consumer has subscribed to message -" + routingKey);
        }
    }

    @Override
    public void subscribe(Consumer consumer, String routingKey, String callBackMethod, Consumer... dependentConsumer) throws InvalidDependentConsumers {
        /*
            1. Check If messageName is already present in our subscriptionList
         */
        MessageSubscriberDetails messageSubscribers = this.messagesSubscriberDetailsMap.get(routingKey);
        if (messageSubscribers == null || messageSubscribers.getSubscribedConsumer() == null ||
                messageSubscribers.getSubscribedConsumer().size() == 0) {
            /*
                2.1  If subscription List is empty, then check whether Input Consumer has specified any dependent Consumers for subscription.
                    If Dependent Consumer List is not empty, then throw DependentConsumersNotYetSubscribed exception
            */
            checkIfDependentConsumerListIsEmptyForNewSubscription(consumer, routingKey, dependentConsumer);

            /*
                2.2  For new subscription of Message, Create a new Entry in our subscriber Map with consumer details and its callbackMethod
             */
            Map<Consumer, String> newMessageSubscriber = new ConcurrentHashMap<>();
            newMessageSubscriber.put(consumer, callBackMethod);
            Map<Consumer, List<Consumer>> dependentMessageConsumers = new ConcurrentHashMap<>();
            dependentMessageConsumers.put(consumer, new LinkedList<Consumer>());
            Set<Consumer> subscribedConsumers = new HashSet<>();
            subscribedConsumers.add(consumer);
            messageSubscribers = new MessageSubscriberDetails(newMessageSubscriber, dependentMessageConsumers, subscribedConsumers);
            this.messagesSubscriberDetailsMap.put(routingKey, messageSubscribers);
        } else {

            /*
                3.1 If Message is already present  in our subscription list, check if dependent consumers have subscribed to it
                    If Not, throw DependentConsumersNotYetSubscribed exception
             */
            checkIfDependentConsumersAreValidAndSubscribedForExistingMessage(consumer, routingKey, messageSubscribers, dependentConsumer);
            /*
                3.2 If Message already present and all dependentConsumers are subscribed to it, then add this consumer to subscription list
             */

            messageSubscribers.getSubscribedConsumer().add(consumer);
            messageSubscribers.getDependentConsumer().put(consumer, Arrays.asList(dependentConsumer));
            messageSubscribers.getConsumerCallbackDetails().put(consumer, callBackMethod);
        }
    }

    private void checkIfDependentConsumerListIsEmptyForNewSubscription(Consumer consumer, String messageName, Consumer[] dependentConsumers) throws InvalidDependentConsumers {
        if (dependentConsumers != null && dependentConsumers.length != 0) {
            throw new InvalidDependentConsumers("Dependent Consumers for Consumer " + consumer.getConsumerName() + " have not yet subscribed to message - " + messageName);
        }
    }

    private void checkIfDependentConsumersAreValidAndSubscribedForExistingMessage(Consumer givenConsumer, String messageName, MessageSubscriberDetails messageSubscribers, Consumer[] dependentConsumers) throws InvalidDependentConsumers {
        Set<Consumer> subscribedConsumers = messageSubscribers.getSubscribedConsumer();
        List<Consumer> dependentConsumerList = Arrays.asList(dependentConsumers);
        if (dependentConsumerList.contains(givenConsumer)) {
            String exceptionMessage = "Subscription of message failed for Consumer "+givenConsumer.getConsumerName()+" for Message "+messageName;
            exceptionMessage += " Because, Dependent consumer is same as given consumer";
            throw new InvalidDependentConsumers(exceptionMessage);
        }
        else if (!subscribedConsumers.containsAll(dependentConsumerList)) {
            String exceptionMessage = "Subscription of message failed for Consumer "+givenConsumer.getConsumerName()+" for Message "+messageName;
            exceptionMessage += " Because, Dependent Consumers for Input Consumer - " + givenConsumer.getConsumerName() + " have not yet subscribed to message - " + messageName;
            throw new InvalidDependentConsumers(exceptionMessage);
        }

    }

}
