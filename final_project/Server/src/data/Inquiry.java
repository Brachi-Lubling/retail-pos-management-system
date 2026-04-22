package data;


import java.io.*;
import java.time.LocalDateTime;

public abstract class Inquiry implements Serializable {
    protected Integer code;
    protected String description;
    protected LocalDateTime creationDate;

    public Inquiry(int code)
    {
        this.code=code;
    }

    public void fillData(String description) {
        this.description=description;
        this.creationDate=LocalDateTime.now();
    }

    public void handling(){
        System.out.println("Inquiry inquiry code: "+this.code);
    }

}
