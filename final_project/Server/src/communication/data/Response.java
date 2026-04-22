package communication.data;
import java.io.Serializable;

public class Response implements Serializable {
    ResponseStatus status;
    String message;
    Object result;
}
