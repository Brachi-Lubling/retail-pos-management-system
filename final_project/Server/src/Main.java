import communication.HandleClient;
import repository.InquiryRepository;
import repository.NextCodeValRepository;
import service.InquiryManager;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        int port = 8888;

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Server started on port " + port);

            InquiryRepository inquiryRepository = new InquiryRepository();
            NextCodeValRepository codeRepo = new NextCodeValRepository();
            InquiryManager manager = new InquiryManager(inquiryRepository, codeRepo);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

                HandleClient handler = new HandleClient(clientSocket, manager);
                handler.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}