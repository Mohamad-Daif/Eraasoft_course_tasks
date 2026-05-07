package exception;

public class InternalServerError extends RuntimeException {

    private ExceptionModel exceptionModel;

    public InternalServerError(ExceptionModel model) {
        super(model.getMessage());
        exceptionModel = model;
    }

    public ExceptionModel getExceptionModel() {
        return exceptionModel;
    }
}
