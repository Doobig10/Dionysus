package connection.exceptions;

public class UnboundException extends BindingException {
    public UnboundException(String error) {
        super(error);
    }

    public UnboundException() {
        super("Network object must be bound to master");
    }
}
