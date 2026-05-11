package communication;

import communication.data.*;
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
            System.out.println("client disconnected: " + e.getClass().getSimpleName());
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Object handleActionRequest(RequestComm request) {

        if (request == null || request.getAction() == null) {
            return null;
        }

        switch (request.getAction()) {

            case ALL_INQUIRY:
                return inquiryManager.getAllInquiries();

            case ADD_INQUIRY:
                Object[] data = (Object[]) request.getData();

                if (data == null || data.length == 0) {
                    return null;
                }

                Inquiry inquiry = (Inquiry) data[0];
                return inquiryManager.addInquiry(inquiry);

            default:
                return null;
        }
    }

    private Response createResponse(Object result) {

        if (result == null) {
            return new Response(null, ResponseStatus.FAIL, "operation failed");
        }

        return new Response(result, ResponseStatus.SUCCESS, "ok");
    }
}