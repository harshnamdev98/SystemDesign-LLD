package exception;

public class NewQueueCapacityLessThanCurrentQueueSize extends Throwable{
    public NewQueueCapacityLessThanCurrentQueueSize(String msg) {
        super(msg);
    }
}
