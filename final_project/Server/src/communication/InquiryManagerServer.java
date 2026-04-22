package communication;

import repository.InquiryRepository;
import repository.NextCodeValRepository;
import service.InquiryManager;

import java.net.ServerSocket;
import java.net.Socket;

public class InquiryManagerServer {

    private ServerSocket server;

    public InquiryManagerServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("server started on port " + port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startServer() {

        InquiryManager manager =
                new InquiryManager(new InquiryRepository(), new NextCodeValRepository());

        while (true) {

            try {
                Socket clientSocket = server.accept();
                System.out.println("client connected");

                HandleClient handler = new HandleClient(clientSocket, manager);
                handler.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
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