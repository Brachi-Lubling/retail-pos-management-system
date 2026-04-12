package handleStoreFiles.testSaveFile;

import handleStoreFiles.HandleFilesReflection;

public class TestingHandleFiles {

    public static void main(String[] args) {

        HandleFilesReflection handleFiles = new HandleFilesReflection();

        PersonForTestSaving p5 =
                new PersonForTestSaving("123456789", "success BH!");

        // שמירה לקובץ
        handleFiles.saveCSV(p5, p5.getId() + ".csv");

        // קריאה מהקובץ
        PersonForTestSaving readP5 =
                (PersonForTestSaving) handleFiles.readCsv("123456789.csv");

        System.out.println(readP5);
    }
}