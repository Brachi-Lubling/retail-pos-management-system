package communication;

import communication.data.RequestComm;
import communication.data.Response;
import communication.data.ResponseStatus;
import data.Inquiry;
import service.InquiryManager;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HandleClient extends Thread {

    private Socket clientSocket;
    private InquiryManager inquiryManager;

    public HandleClient(Socket clientSocket, InquiryManager inquiryManager) {
        this.clientSocket = clientSocket;
        this.inquiryManager = inquiryManager;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {

            while (true) {
                RequestComm request = (RequestComm) in.readObject();

                Object result = handleActionRequest(request);
                Response response = createResponse(result);

                out.writeObject(response);
                out.flush();
            }

        } catch (Exception e) {
            System.out.println("client disconnected");
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Response createResponse(Object result) {
        if (result == null) {
            return new Response(null, ResponseStatus.FAIL, "oops something went wrong");
        } else {
            return new Response(result, ResponseStatus.SUCCESS, "enjoy");
        }
    }

    private Object handleActionRequest(RequestComm request) {
        switch (request.getAction()) {
            case ALL_INQUIRY:
                return inquiryManager.getAllInquiries();

            case ADD_INQUIRY:
                return inquiryManager.addInquiry((Inquiry) request.getData());

            default:
                return null;
        }
    }
}