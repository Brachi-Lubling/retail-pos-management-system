package data;

public class Request extends Inquiry{


    @Override
    public void handling(){
        System.out.println("Request inquiry code: "+this.code);
    }

    @Override
    public  void fillData(String description,String assignedBranch) {
        super.fillData(description,"Request");
    }

}
