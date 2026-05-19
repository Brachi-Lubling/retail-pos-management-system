package data;

import service.InquiryManager;

import java.io.Serializable;

public class InquiryAndRepresentative extends Thread implements Serializable {

    private Representative currentRepresentative;
    private Inquiry currentInquiry;

    public InquiryAndRepresentative(Representative currentrepresentative, Inquiry currentInquiry) {
        this.currentRepresentative = currentrepresentative;
        this.currentInquiry = currentInquiry;
    }
    public Representative getCurrentRepresentative() {
        return currentRepresentative;
    }

    public void setCurrentRepresentative(Representative currentRepresentative) {
        this.currentRepresentative = currentRepresentative;
    }

    public Inquiry getCurrentInquiry() {
        return currentInquiry;
    }

    public void setCurrentInquiry(Inquiry currentInquiry) {
        this.currentInquiry = currentInquiry;
    }

    @Override
    public void run() {
        try {
            handling();
        } finally {
            InquiryManager.getInstance().returnAgentToQueue(currentRepresentative);
            InquiryManager.getInstance().closeInquiry(currentInquiry);
            InquiryManager.getInstance().decreaseActiveInquiriesCounter();
        }
    }

    private void handling() {
        try {
            if (currentInquiry instanceof Question)
                this.setPriority(Thread.MAX_PRIORITY);
            else
                this.setPriority(Thread.MIN_PRIORITY);

            if (currentInquiry instanceof Request)
                Thread.sleep(3000);
            else
                Thread.sleep(5000);
            currentInquiry.handling();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

