package network.connection.exceptions;

public class AlreadyBoundException extends BindingException {
    public AlreadyBoundException(String error) {super(error);}
    public AlreadyBoundException() {super("Cannot re-bind an existing binding");}
}
