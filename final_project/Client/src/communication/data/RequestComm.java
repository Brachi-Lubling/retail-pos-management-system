package communication.data;
import data.Inquiry;
import java.io.Serializable;

public class RequestComm implements Serializable {
    private InquiryManagerActions action;
    private Inquiry data;

    public RequestComm(InquiryManagerActions actions, Object data) {
        super();
    }

    public InquiryManagerActions getAction() {
        return action;
    }

    public Inquiry getData() {
        return data;
    }

}
