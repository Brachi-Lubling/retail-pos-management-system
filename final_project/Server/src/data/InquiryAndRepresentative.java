package data;

import java.io.Serializable;

public class InquiryAndRepresentative implements Serializable {

    private Representative currentrepresentative;
    private Inquiry currentInquiry;

    public InquiryAndRepresentative(Representative currentrepresentative, Inquiry currentInquiry) {
        this.currentrepresentative = currentrepresentative;
        this.currentInquiry = currentInquiry;
    }
    public Representative getCurrentrepresentative() {
        return currentrepresentative;
    }

    public void setCurrentrepresentative(Representative currentrepresentative) {
        this.currentrepresentative = currentrepresentative;
    }

    public Inquiry getCurrentInquiry() {
        return currentInquiry;
    }

    public void setCurrentInquiry(Inquiry currentInquiry) {
        this.currentInquiry = currentInquiry;
    }
}
