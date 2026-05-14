//package communication.data;
//
//import data.Inquiry;
//import java.io.Serializable;
//
//public class RequestComm implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    private InquiryManagerActions action;
//<<<<<<< HEAD
//    private Object  data;
//
//    public RequestComm(InquiryManagerActions action, Object data) {
//=======
//    private Object requestParameters;
//
//    public RequestComm(InquiryManagerActions action, Object... requestParameters) {
//>>>>>>> d86e4a5dde952b14dde38842841bb805188297fb
//        this.action = action;
//        this.requestParameters = requestParameters;
//    }
//
//    public InquiryManagerActions getAction() {
//        return action;
//    }
//
//<<<<<<< HEAD
//    public Object  getData() {
//        return data;
//=======
//    public Object getData() {
//        return  requestParameters;
//>>>>>>> d86e4a5dde952b14dde38842841bb805188297fb
//    }
//}

package communication.data;

import java.io.Serializable;

public class RequestComm implements Serializable {

    private static final long serialVersionUID = 1L;

    private InquiryManagerActions action;
    private Object[] requestParameters;

    public RequestComm(InquiryManagerActions action, Object... requestParameters) {
        this.action = action;
        this.requestParameters = requestParameters;
    }

    public InquiryManagerActions getAction() {
        return action;
    }

    public Object[] getData() {
        return requestParameters;
    }
}