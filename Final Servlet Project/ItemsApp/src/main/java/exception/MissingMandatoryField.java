package exception;

public class MissingMandatoryField extends RuntimeException{
    ExceptionModel exceptionModel;

    public MissingMandatoryField(ExceptionModel model) {
        super(model.getMessage());
        exceptionModel = model;
    }

    public ExceptionModel getExceptionModel() {
        return exceptionModel;
    }
}
