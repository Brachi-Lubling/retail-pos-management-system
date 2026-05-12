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
//            e.printStackTrace(); // חשוב לראות מה קורס

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
                if (!(request.getData() instanceof Inquiry)) {
                    return null;
                }
                return inquiryManager.addInquiry((Inquiry) request.getData());

            case GET_INQUIRIES_COUNT_BY_MONTH:
            {
                Object data = request.getData();

                if (!(data instanceof Integer))
                {
                    System.out.println("ERROR: expected Integer but got " +
                            (data == null ? "null" : data.getClass().getSimpleName()));
                    return null;
                }

                int month = (Integer) data;

                return inquiryManager.getInquiriesCountByMonth(month);
            }

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