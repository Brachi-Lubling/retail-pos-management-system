package business;

import Data.Inquiry;
import Data.Question;
import Data.Request;
import Data.Complaint;
import java.util.Scanner;
public class InquiryHandling extends Thread {
    private Inquiry currentInquiry;

    public void createInquiry(){
        int x;
        Scanner sc = new Scanner(System.in);
        System.out.println(" Please press the appropriate key.");
        System.out.println("1-question 2-request 3-complain");
        x = sc.nextInt();
        switch (x)
        {
            case 1:
                currentInquiry=new Question();
                this.setPriority(Thread.MIN_PRIORITY);
                break;
            case 2:
                currentInquiry=new Request();
                this.setPriority(Thread.MAX_PRIORITY);
                break;
            case 3:
                currentInquiry=new Complaint();
                this.setPriority(Thread.MAX_PRIORITY);
                break;
            default:
                System.out.println("Inactive key, please press again.");
                return;
        }

        currentInquiry.fillDataByUser();


    }
    @Override
    public void run(){
        if (currentInquiry == null)
            return;
        int num=5000;
        if(currentInquiry instanceof Question)
            num=3000;
        try {
            Thread.sleep(num);  // השהיה של 5 שניות
        } catch (InterruptedException e) {
            e.printStackTrace(); // טיפול במקרה שהתוכנית קיבלה הפרעה
        }
        currentInquiry.handling();
    }
}
