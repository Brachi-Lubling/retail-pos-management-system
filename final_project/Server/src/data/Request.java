package data;

public class Request extends Inquiry{

    public Request(int code) {super(code);}

    @Override
    public void handling(){
        System.out.println("Request inquiry code: "+this.code);
    }


}
