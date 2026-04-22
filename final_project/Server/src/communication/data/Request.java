package communication.data;
import data.Inquiry;
import java.io.Serializable;

public class Request implements Serializable {
    private InquiryManagerActions action;
    private Inquiry data;

    public InquiryManagerActions getAction() {
        return action;
    }

    public Inquiry getData() {
        return data;
    }
}
