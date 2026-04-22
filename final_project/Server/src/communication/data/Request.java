package communication.data;
import data.Inquiry;
import java.io.Serializable;

public class Request implements Serializable {
    InquiryManagerActions action;
    Object data;
}
