package data;

public class Question extends Inquiry{

    public Question(int code){super(code);}

    @Override
    public void handling(){
        System.out.println("Question inquiry code: "+this.code);
    }

}
