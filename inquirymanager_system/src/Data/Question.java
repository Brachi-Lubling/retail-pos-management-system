package Data;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Question extends Inquiry
{
    @Override
    public void fillDataByUser() {
        super.fillDataByUser();
    }

    @Override
    public void handling()
    {
        System.out.println("The system handle in your question the code is: "+code);
    }

}


