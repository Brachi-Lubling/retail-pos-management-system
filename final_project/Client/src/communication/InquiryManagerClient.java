//package communication;
//
//import communication.data.InquiryManagerActions;
//import communication.data.RequestComm;
//import communication.data.Response;
//import data.Complaint;
//import data.Inquiry;
//import data.Question;
//import data.Request;
//
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.util.List;
//import java.util.Scanner;
//
//public class InquiryManagerClient
//{
//
//    private Socket socket;
//    private ObjectOutputStream oos;
//    private ObjectInputStream ois;
//    private Scanner scanner = new Scanner(System.in);
//
//    public InquiryManagerClient(Socket socket) {
//        this.socket = socket;
//        try {
//            // חשוב: קודם Output ואז Input
//            this.oos = new ObjectOutputStream(socket.getOutputStream());
//            this.ois = new ObjectInputStream(socket.getInputStream());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void runMenu() {
//
//        while (true) {
//<<<<<<< HEAD
//            System.out.println("press 1-create inquiry, 2-get all inquiries in queue,3- get count by month");
//=======
//            System.out.println("press 1-create inquiry, 2-get all inquiries in queue,3-get inquiry status");
//>>>>>>> d86e4a5dde952b14dde38842841bb805188297fb
//            int choice = scanner.nextInt();
//            scanner.nextLine();
//
//            switch (choice) {
//                case 1:
//                    inquiryCreation();
//                    break;
//                case 2:
//                    getAllInqueries();
//                    break;
//                case 3:
//<<<<<<< HEAD
//                    getInquiriesCountByMonth();
//                    break;
//=======
//                    getInquiryStatus();
//>>>>>>> d86e4a5dde952b14dde38842841bb805188297fb
//                default:
//                    System.out.println("the number is not valid");
//            }
//        }
//    }
//
//    private void getInquiryStatus() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("insert inquiry id to get it status");
//        int id=scanner.nextInt();
//        createCommunication(InquiryManagerActions.GET_INQUIRY_STATUS,id);
//    }
//
//    public void inquiryCreation() {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("press 1-question, 2-request, 3-complaint 0-to exit");
//        int choice = scanner.nextInt();
//        scanner.nextLine();
//
//
//        while (choice != 0) {
//            Inquiry currentInquiry = createOneInquiry(choice);
//
//            if (currentInquiry != null) {
//                createCommunication(InquiryManagerActions.ADD_INQUIRY, currentInquiry);
//            }
//
//            System.out.println("press 1-question, 2-request, 3-complaint 0-to exit");
//            choice = scanner.nextInt();
//            scanner.nextLine();
//        }
//    }
//
//    public Inquiry createOneInquiry(int choice) {
//        Inquiry currentInquiry;
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("insert description");
//        String description = scanner.nextLine();
//
//        switch (choice) {
//            case 1:
//                currentInquiry = new Question();
//                ((Question) currentInquiry).fillData(description);
//                break;
//
//            case 2:
//                currentInquiry = new Request();
//                ((Request) currentInquiry).fillData(description);
//                break;
//
//            case 3:
//                System.out.println("insert branch");
//                String branch = scanner.nextLine();
//                currentInquiry = new Complaint();
//                ((Complaint) currentInquiry).fillData(description, branch);
//                break;
//
//            default:
//                System.out.println("the choice is not valid");
//                return null;
//        }
//
//        return currentInquiry;
//    }
//
//    private void getAllInqueries() {
//        createCommunication(InquiryManagerActions.ALL_INQUIRY);
//    }
//
//    private void createCommunication(InquiryManagerActions action, Object... data) {
//        try {
//<<<<<<< HEAD
//            RequestComm request = new RequestComm(action, (Object) data);
//=======
//            RequestComm request = new RequestComm(action,data);
//>>>>>>> d86e4a5dde952b14dde38842841bb805188297fb
//
//            oos.writeObject(request);
//            oos.flush();
//
//            Response response = (Response) ois.readObject();
//
//            System.out.println("Status: " + response.getStatus());
//            System.out.println("Message: " + response.getMessage());
//
//            if (response.getResult() != null) {
//                System.out.println("Result: " + response.getResult());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    private void getInquiriesCountByMonth()
//    {
//        try
//        {
//            System.out.println("Enter month:");
//            int month = scanner.nextInt();
//            scanner.nextLine();
//            RequestComm request = new RequestComm(InquiryManagerActions.GET_INQUIRIES_COUNT_BY_MONTH, month);
//            oos.writeObject(request);
//            oos.flush();
//            Response response = (Response) ois.readObject();
//            System.out.println("Status: " + response.getStatus());
//            System.out.println("Message: " + response.getMessage());
//            if (response.getResult() != null)
//            {
//                System.out.println("Result: " + response.getResult());
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//}

package communication;

import communication.data.InquiryManagerActions;
import communication.data.RequestComm;
import communication.data.Response;
import data.Complaint;
import data.Inquiry;
import data.Question;
import data.Request;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class InquiryManagerClient
{

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Scanner scanner = new Scanner(System.in);

    public InquiryManagerClient(Socket socket) {
        this.socket = socket;
        try {
            // חשוב: קודם Output ואז Input
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runMenu() {

        while (true) {
            System.out.println("press 1-create inquiry, 2-get all inquiries in queue, 3-get count by month, 4-get inquiry status, 5-cancel inquiry");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    inquiryCreation();
                    break;

                case 2:
                    getAllInqueries();
                    break;

                case 3:
                    getInquiriesCountByMonth();
                    break;

                case 4:
                    getInquiryStatus();
                    break;

                case 5:
                    cancelInquiry();
                    break;

                default:
                    System.out.println("the number is not valid");
            }
        }
    }

    private void cancelInquiry() {
        System.out.println("insert inquiry code to cancel");
        int code = scanner.nextInt();
        scanner.nextLine();
        createCommunication(InquiryManagerActions.CANCEL_INQUIRY, code);
    }

    private void getInquiryStatus() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("insert inquiry id to get it status");

        int id = scanner.nextInt();

        createCommunication(InquiryManagerActions.GET_INQUIRY_STATUS, id);
    }

    public void inquiryCreation() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("press 1-question, 2-request, 3-complaint 0-to exit");

        int choice = scanner.nextInt();
        scanner.nextLine();

        while (choice != 0) {

            Inquiry currentInquiry = createOneInquiry(choice);

            if (currentInquiry != null) {
                createCommunication(InquiryManagerActions.ADD_INQUIRY, currentInquiry);
            }

            System.out.println("press 1-question, 2-request, 3-complaint 0-to exit");

            choice = scanner.nextInt();
            scanner.nextLine();
        }
    }

    public Inquiry createOneInquiry(int choice) {

        Inquiry currentInquiry;

        Scanner scanner = new Scanner(System.in);

        System.out.println("insert description");

        String description = scanner.nextLine();

        switch (choice) {

            case 1:

                currentInquiry = new Question();

                ((Question) currentInquiry).fillData(description);

                break;

            case 2:

                currentInquiry = new Request();

                ((Request) currentInquiry).fillData(description);

                break;

            case 3:

                System.out.println("insert branch");

                String branch = scanner.nextLine();

                currentInquiry = new Complaint();

                ((Complaint) currentInquiry).fillData(description, branch);

                break;

            default:

                System.out.println("the choice is not valid");

                return null;
        }

        return currentInquiry;
    }

    private void getAllInqueries() {
        createCommunication(InquiryManagerActions.ALL_INQUIRY);
    }

    private void createCommunication(InquiryManagerActions action, Object... data) {

        try {

            RequestComm request = new RequestComm(action, data);

            oos.writeObject(request);

            oos.flush();

            Response response = (Response) ois.readObject();

            System.out.println("Status: " + response.getStatus());

            System.out.println("Message: " + response.getMessage());

            if (response.getResult() != null) {
                System.out.println("Result: " + response.getResult());
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void getInquiriesCountByMonth()
    {
        try
        {
            System.out.println("Enter month:");

            int month = scanner.nextInt();

            scanner.nextLine();

            RequestComm request = new RequestComm(
                    InquiryManagerActions.GET_INQUIRIES_COUNT_BY_MONTH,
                    month
            );

            oos.writeObject(request);

            oos.flush();

            Response response = (Response) ois.readObject();

            System.out.println("Status: " + response.getStatus());

            System.out.println("Message: " + response.getMessage());

            if (response.getResult() != null)
            {
                System.out.println("Result: " + response.getResult());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}