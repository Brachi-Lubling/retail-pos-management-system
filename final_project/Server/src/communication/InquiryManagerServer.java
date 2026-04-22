package communication;

import java.net.ServerSocket;
import java.net.Socket;

public class InquiryManagerServer {

    private ServerSocket server;

    public InquiryManagerServer(int port) {
        try {
            System.out.println("starting server on port " + port);
            server = new ServerSocket(port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startServer() {
        try {
            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("connected client: " + clientSocket.getRemoteSocketAddress());
                HandleClient handler = new HandleClient(clientSocket);
                handler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        try {
            server.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}