package kr.co.pionnet.dy.exception;

public class AgentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AgentException(String msg) {
        super(msg);
    }

    public AgentException(String msg, Throwable t) {
        super(msg, t);
    }

}
