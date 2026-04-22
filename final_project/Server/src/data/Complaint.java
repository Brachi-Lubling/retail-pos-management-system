package data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Complaint extends Inquiry{

    protected String assignedBranch;


    public  void fillData(String description,String assignedBranch) {
        super.fillData(description);
        this.assignedBranch=assignedBranch;
    }

    @Override
    public void handling(){
        System.out.println("Complaint inquiry code: "+this.code);
    }}

