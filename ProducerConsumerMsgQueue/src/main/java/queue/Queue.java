package queue;

import consumer.Consumer;
import data.Message;
import exception.InvalidDependentConsumers;

public interface Queue {
    void setQueueCapacity(int size);
    int getRemainingSize();
    int getQueueSize();
    void addMessage(Message message, String messageName); // routingkey
    void subscribe(Consumer consumer, String messageName, String callBackMethod, Consumer... dependentConsumer) throws InvalidDependentConsumers;

}
