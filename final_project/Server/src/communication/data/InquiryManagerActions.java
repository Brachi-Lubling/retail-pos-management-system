package communication.data;

import java.io.Serializable;

public enum InquiryManagerActions implements Serializable {
    ALL_INQUIRY,
    ADD_INQUIRY,
    GET_INQUIRIES_COUNT_BY_MONTH,
    GET_INQUIRY_STATUS,
    CANCEL_INQUIRY,
    AGENT_LOGIN,
    AGENT_LOGOUT

}