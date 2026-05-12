package communication.data;

import data.Inquiry;
import java.io.Serializable;

public class RequestComm implements Serializable {

    private static final long serialVersionUID = 1L;

    private InquiryManagerActions action;
    private Object  data;

    public RequestComm(InquiryManagerActions action, Object data) {
        this.action = action;
        this.data = data;
    }

    public InquiryManagerActions getAction() {
        return action;
    }

    public Object  getData() {
        return data;
    }
}