package exception;

public class ConsumerAlreadySubscribedException extends Throwable{
    public ConsumerAlreadySubscribedException(String msg) {
        super(msg);
    }
}
