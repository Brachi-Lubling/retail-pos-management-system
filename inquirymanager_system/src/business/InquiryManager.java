package business;

import Data.Complaint;
import Data.Inquiry;
import Data.Question;
import Data.Request;
import business.InquiryHandling;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class InquiryManager {

    private Queue<Inquiry> inquiryQueue = new LinkedList<Inquiry>();
    private Inquiry currentInquiry;


    public void inquiryCreation( ) {
        int x;
        Scanner sc = new Scanner(System.in);
        System.out.println(" Please press the appropriate key.");
        System.out.println("1-question 2-request 3-complain");
        x = sc.nextInt();
        switch (x) {
            case 1:
                currentInquiry = new Question();
                break;
            case 2:
                currentInquiry = new Request();
                break;
            case 3:
                currentInquiry = new Complaint();
                break;
            default:
                System.out.println("Inactive key, please press again.");
                return;
        }
        currentInquiry.fillDataByUser();
        inquiryQueue.add(currentInquiry);

    }

    public void processInquiryManager() {

        while (!inquiryQueue.isEmpty()) {

            Inquiry inquiry = inquiryQueue.poll();

            InquiryHandling inquiryHandling = new InquiryHandling(inquiry);
            inquiryHandling.start();

            try {
                inquiryHandling.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }
    }

}




