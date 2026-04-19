package handleStoreFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.*;
import java.util.List;

public class HandleFiles{
    public boolean saveFile(ForSaving forSaving)
    {
        File folder = new File(forSaving.getFolderName());
        if(!folder.exists()){
            folder.mkdir();
        }

        try(BufferedWriter bw =new BufferedWriter(new FileWriter(forSaving.getFileName())))
        {
            bw.write(forSaving.getData());
            return true;
        }catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

    }


    public void deleteFile(ForSaving forSaving)
    {
        File file=new File(forSaving.getFolderName(),forSaving.getFileName());
        if(file.exists())
        {
            file.delete();
            System.out.println("File"+forSaving.getFileName()+"deleted");
        }
    }

    public void updateFile(ForSaving forSaving)
    {
        saveFile(forSaving);
    }

    public void saveFiles(List<ForSaving> forSavingList)
    {
        for(int i=0;i<forSavingList.size();i++)
            saveFile(forSavingList.get(i));
    }

}
