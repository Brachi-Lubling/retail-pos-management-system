package handleStoreFiles.testSaveFile;

import handleStoreFiles.HandleFiles;

import java.io.IOException;
import java.util.Arrays;

public class TestingHandleFiles
{
    public static void main(String[] args) throws IOException
    {
        PersonForTestSaving p1 = new PersonForTestSaving("1234","aaa");
        PersonForTestSaving p2 = new PersonForTestSaving("5432","bbb");
        PersonForTestSaving p3 = new PersonForTestSaving("9999","ccc");
        PersonForTestSaving p4 = new PersonForTestSaving("0090","ccdc");

        HandleFiles handleFiles = new HandleFiles();

        handleFiles.saveFile(p3);
        handleFiles.saveFiles(Arrays.asList(p1,p2,p3,p4));
        handleFiles.deleteFile(p2);

        System.out.println( "p1:"+p1.getFileName()+" "+p1.getFolderName());
        System.out.println( "p2:"+p2.getFileName()+" "+p2.getFolderName());
        System.out.println( "p3:"+p3.getFileName()+" "+p3.getFolderName());
        System.out.println( "p4:"+p4.getFileName()+" "+p4.getFolderName());
    }




}
//package HandleStoreFiles.testSaveFile;
//
//import HandleStoreFiles.HandleFiles;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//public class TestingHandleFiles {
//    public static void main(String[] args) throws IOException {
//        PersonForTestSaving p1 = new PersonForTestSaving("1234","aaa");
//        PersonForTestSaving p2 = new PersonForTestSaving("5432","bbb");
//        PersonForTestSaving p3 = new PersonForTestSaving("9999","ccc");
//        PersonForTestSaving p4 = new PersonForTestSaving("0090","ddd");
//
//        HandleFiles handleFiles = new HandleFiles();
//        handleFiles.saveFile(p3);
//
//        handleFiles.saveFiles(Arrays.asList(p1,p2,p3,p4));
//
//        handleFiles.deleteFile(p2);
//
//
//        System.out.println( "p1:"+p1.getFileName()+" "+p1.getFolderName());
//        System.out.println( "p2:"+p2.getFileName()+" "+p2.getFolderName());
//        System.out.println( "p3:"+p3.getFileName()+" "+p3.getFolderName());
//        System.out.println( "p4:"+p4.getFileName()+" "+p4.getFolderName());
//    }
//
//
//}
