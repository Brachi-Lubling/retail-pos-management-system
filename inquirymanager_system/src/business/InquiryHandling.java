package business;

import Data.Complaint;
import Data.Inquiry;
import Data.Question;
import Data.Request;

import java.util.Scanner;


public class InquiryHandling extends Thread {
    private Inquiry currentInquiry;

    public InquiryHandling(Inquiry inquiry) {
        this.currentInquiry = inquiry;
    }


//        currentInquiry.fillDataByUser();}

    @Deprecated
    public void createInquiry(){

}



    @Override
    public void run() {

        if (currentInquiry == null)
            return;
        int num = 5000;
        if (currentInquiry instanceof Question)
            num = 3000;
        try {
            Thread.sleep(num);  // השהיה של 5 שניות
        } catch (InterruptedException e) {
            e.printStackTrace(); // טיפול במקרה שהתוכנית קיבלה הפרעה
        }
        currentInquiry.handling();
    }
}

