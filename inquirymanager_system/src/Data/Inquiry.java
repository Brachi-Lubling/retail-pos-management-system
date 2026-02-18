package Data;

import java.time.LocalDateTime;
import java.util.Scanner;


public class Inquiry {
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

}
