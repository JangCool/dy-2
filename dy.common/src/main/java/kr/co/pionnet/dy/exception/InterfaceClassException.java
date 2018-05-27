package kr.co.pionnet.dy.exception;

public class InterfaceClassException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InterfaceClassException(String msg) {
        super(msg);
    }

    public InterfaceClassException(String msg, Throwable t) {
        super(msg, t);
    }

}
