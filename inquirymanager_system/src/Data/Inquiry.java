package Data;

import handleStoreFiles.ForSaving;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Inquiry implements ForSaving {
    protected  static Integer nextCodeVal = 0;
    protected   int code;
    protected  String description;
    protected  LocalDateTime creationDate;

    public  void fillDataByUser()
    {
        Scanner sc=new Scanner(System.in);

        System.out.println("Enter description for inquiry: ");
        this.description=sc.nextLine();
        this.creationDate= LocalDateTime.now();
    }


    public static int getNextCodeVal() {
        return nextCodeVal;
    }

    public static void incrementNextCodeVal() {
        nextCodeVal++;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void handling()
    {
        System.out.println("The system handle in your inquiry the code is: "+code);
    }



    @Override
    public String getFolderName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getFileName() {
        return code+"";
    }

    @Override
    public String getData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return code+","+description+","+creationDate.format(formatter);
    }

    @Override
    public String getId() {
        return "";
    }

}
