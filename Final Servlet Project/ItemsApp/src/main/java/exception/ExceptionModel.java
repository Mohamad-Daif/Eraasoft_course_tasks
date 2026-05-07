package exception;

public class ExceptionModel {

    private String message;
    private int statusCode;

    public ExceptionModel() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ExceptionModel(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;

    }

    @Override
    public String toString() {
        return "{" +
                "message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
