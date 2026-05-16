package exception;

public class InternalServerError extends RuntimeException {

    public InternalServerError() {
        super("Internal Server Error");
    }

}
