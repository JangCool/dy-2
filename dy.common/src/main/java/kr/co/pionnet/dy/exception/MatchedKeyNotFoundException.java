package kr.co.pionnet.dy.exception;

public class MatchedKeyNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MatchedKeyNotFoundException(String message) {
        super(message);
    }

}
