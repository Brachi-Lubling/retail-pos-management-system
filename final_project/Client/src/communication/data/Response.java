package communication.data;
import java.io.Serializable;

public class Response implements Serializable {
    private ResponseStatus status;
    private String message;
    private Object result;

    public Response(Object result, ResponseStatus status, String message) {
        this.result=result;
        this.message=message;
        this.status=status;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }
}
