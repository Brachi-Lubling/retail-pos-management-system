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
        this.code=nextCodeVal++;
        System.out.println("Enter description for inquiry: ");
        this.description=sc.nextLine();
        this.creationDate= LocalDateTime.now();
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

}
