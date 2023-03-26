package queue;

import data.Message;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class QueueMessage {
    private String routingKey; // messageName
    private int retryCount;
    private Message payload;

    public QueueMessage() {
    }

    @JsonCreator
    public QueueMessage(@JsonProperty("routingKey") String routingKey,
                        @JsonProperty("retryCount")int retryCount,
                        @JsonProperty("payload") Message payload) {
        this.routingKey = routingKey;
        this.retryCount = retryCount;
        this.payload = payload;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Message getPayload() {
        return payload;
    }

    public void setPayload(Message payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "QueueMessage{" +
                "routingKey='" + routingKey + '\'' +
                ", retryCount=" + retryCount +
                ", payload=" + payload +
                '}';
    }
}
