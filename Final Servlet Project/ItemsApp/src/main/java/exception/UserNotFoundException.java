package exception;

public class UserNotFoundException extends RuntimeException {
    ExceptionModel exceptionModel;

    public UserNotFoundException(ExceptionModel model) {
        super(model.getMessage());
        exceptionModel = model;
    }

    public ExceptionModel getExceptionModel() {
        return exceptionModel;
    }
}
