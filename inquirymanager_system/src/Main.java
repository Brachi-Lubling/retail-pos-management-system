import business.InquiryHandling;
import business.InquiryManager;
public class Main {

    public static void main(String[] args) {

        System.out.println("Hello world!");


     //   InquiryHandling handling1 = new InquiryHandling();
        //Thread handling2 = new InquiryHandling();
       // InquiryHandling handling3 = new InquiryHandling();
      //  InquiryHandling handling4 = new InquiryHandling();
     //   handling1. createInquiry();
        //((InquiryHandling) handling2).createInquiry();
      //  handling3.createInquiry();
       // handling4.createInquiry();

       // handling1.start();
        //handling2.start();
      //  handling3.start();
       // handling4.start();

       // handling1.run();
      //  handling2.run();
     //   handling3.run();
        //handling4.run();

        InquiryManager inquiryManager = new InquiryManager();
        InquiryManager inquiryManager1 = new InquiryManager();
        InquiryManager inquiryManager2 = new InquiryManager();
        inquiryManager.inquiryCreation();
        inquiryManager1.inquiryCreation();
        inquiryManager2.inquiryCreation();
        inquiryManager.processInquiryManager();
        inquiryManager1.processInquiryManager();
        inquiryManager2.processInquiryManager();









    }
}