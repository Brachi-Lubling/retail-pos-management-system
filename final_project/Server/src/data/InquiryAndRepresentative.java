package data;

public class InquiryAndRepresentative {

    private Representative currentrepresentative;
    private Inquiry currentInquiry;

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
