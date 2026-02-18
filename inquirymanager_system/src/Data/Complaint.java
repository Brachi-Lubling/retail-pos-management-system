package Data;

import java.util.Scanner;

public class Complaint extends Inquiry
{
    private String assignedBranch;

    @Override
    public void fillDataByUser() {
        super.fillDataByUser();
        System.out.println("Enter assignedBranch for inquiry: ");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Branch Name: ");
        this.assignedBranch=sc.nextLine();
    }

    @Override
    public void handling(){
        System.out.println("The system handle in your com[lain the code is: "+code);
    }

}
