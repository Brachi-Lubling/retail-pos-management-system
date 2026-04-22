package data;

public class Question extends Inquiry{


    @Override
    public void handling(){
        System.out.println("Question inquiry code: "+this.code);
    }

    @Override
    public  void fillData(String description,String assignedBranch) {
        super.fillData(description,"Question");
    }
}
