package kr.co.pionnet.dy.exception;

public class UtilityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UtilityException(String msg) {
        super(msg);
    }

    public UtilityException(String msg, Throwable t) {
        super(msg, t);
    }

}
