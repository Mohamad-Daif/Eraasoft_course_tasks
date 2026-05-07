package exception;

public class ItemNotFound extends RuntimeException {

    ExceptionModel exceptionModel;

    public ItemNotFound(ExceptionModel model) {
        super(model.getMessage());
        exceptionModel = model;
    }

    public ExceptionModel getExceptionModel() {
        return exceptionModel;
    }
}
