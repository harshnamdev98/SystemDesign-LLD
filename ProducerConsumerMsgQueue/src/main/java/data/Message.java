package data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class Message {
    private String data;

    public Message() {
    }

    @JsonCreator
    public Message(@JsonProperty("data") String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "data='" + data + '\'' + '}';
    }

    }
