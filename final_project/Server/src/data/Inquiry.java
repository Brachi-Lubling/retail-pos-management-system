package data;


import java.io.*;
import java.time.LocalDateTime;

public abstract class Inquiry implements Serializable {
    protected Integer code;
    protected String description;
    protected LocalDateTime creationDate;


    public void fillData(String description) {
        this.description=description;
        this.creationDate=LocalDateTime.now();
    }

    public void handling(){
        System.out.println("Inquiry inquiry code: "+this.code);
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
