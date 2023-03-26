package exception;

public class MessageNotValidException extends Throwable{
    public MessageNotValidException(String msg) {
        super(msg);
    }
}
