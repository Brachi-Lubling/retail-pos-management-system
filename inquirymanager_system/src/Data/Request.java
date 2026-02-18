package Data;

public class Request extends Inquiry
{
    @Override
    public void fillDataByUser() {
        super.fillDataByUser();
    }

    @Override
    public void handling(){
        System.out.println("The system handle in your request the code is: "+code);
    }
}
