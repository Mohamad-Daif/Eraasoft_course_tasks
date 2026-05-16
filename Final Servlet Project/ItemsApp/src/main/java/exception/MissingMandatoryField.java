package exception;

public class MissingMandatoryField extends RuntimeException {

    public MissingMandatoryField() {
        super("Missing mandatory Field");
    }

}
