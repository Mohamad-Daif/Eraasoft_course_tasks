package exception;

public class ItemNotFound extends RuntimeException {

    public ItemNotFound() {
        super("Item not found");
    }

}
