package communication;

import communication.data.RequestComm;
import communication.data.Response;
import communication.data.ResponseStatus;
import data.Inquiry;
import service.InquiryManager;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HandleClient extends Thread{
    Socket clientSocket;
    InquiryManager inquiryManager;

    public HandleClient(Socket clientSocket,InquiryManager inquiryManager){
        this.clientSocket=clientSocket;
        this.inquiryManager=inquiryManager;
    }

    @Override
    public void run(){
        this.handleClientRequest();
    }

    private void handleClientRequest() {
        try {
            RequestComm request =getRequest();
            Object result=handleActionRequest(request);
            Response response=createResponse(result);
            respondToClient(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void respondToClient(Response response) {
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(response);
            oos.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response createResponse(Object result) {
        String message;
        ResponseStatus status;
        if(result==null) {
            status=ResponseStatus.FAIL;
            message="oops something went wrong";
        }
        else {
            status=ResponseStatus.SUCCESS;
            message="enjoy";
        }
        return new Response(result,status,message);
    }

    private Object handleActionRequest(RequestComm request) {
        switch (request.getAction()){
            case ALL_INQUIRY: return getAllInquiries();
            case ADD_INQUIRY: return addInquiry(request.getData());
            default: return null;
        }
    }

    private Object addInquiry(Inquiry inquiry) {
        return inquiryManager.addInquiry(inquiry);
    }

    private Object getAllInquiries() {
        return inquiryManager.getAllInquiries();
    }

    private RequestComm getRequest() throws IOException, ClassNotFoundException {
        return (RequestComm) new ObjectInputStream(clientSocket.getInputStream()).readObject();
    }
}
