package queue;

import consumer.Consumer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageSubscriberDetails implements Cloneable {
    // Consumer and its callback
    private Map<Consumer, String> consumerCallbackDetails;

    // Consumer and its dependant consumer
    private Map<Consumer, List<Consumer>> dependentConsumer;

    private Set<Consumer> subscribedConsumer;

    public MessageSubscriberDetails(Map<Consumer, String> consumerCallbackDetails,
                                    Map<Consumer, List<Consumer>> dependentConsumer,
                                    Set<Consumer> subscribedConsumer) {
        this.consumerCallbackDetails = consumerCallbackDetails;
        this.dependentConsumer = dependentConsumer;
        this.subscribedConsumer = subscribedConsumer;
    }

    public Map<Consumer, String> getConsumerCallbackDetails() {
        return consumerCallbackDetails;
    }

    public void setConsumerCallbackDetails(Map<Consumer, String> consumerCallbackDetails) {
        this.consumerCallbackDetails = consumerCallbackDetails;
    }

    public Map<Consumer, List<Consumer>> getDependentConsumer() {
        return dependentConsumer;
    }

    public void setDependentConsumer(Map<Consumer, List<Consumer>> dependentConsumer) {
        this.dependentConsumer = dependentConsumer;
    }

    public Set<Consumer> getSubscribedConsumer() {
        return subscribedConsumer;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setSubscribedConsumer(Set<Consumer> subscribedConsumer) {
        this.subscribedConsumer = subscribedConsumer;
    }
}
