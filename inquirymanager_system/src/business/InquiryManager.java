package business;

import Data.Complaint;
import Data.Inquiry;
import Data.Question;
import Data.Request;
import Data.Representative;

import handleStoreFiles.HandleFiles;
import handleStoreFiles.HandleFilesReflection;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class InquiryManager {

    private ArrayList<Representative> representatives;
    private Queue<Inquiry> inquiryQueue = new LinkedList<>();
    private Inquiry currentInquiry;
    private HandleFiles handleFiles = new HandleFiles();

    private Scanner scanner = new Scanner(System.in);

    public InquiryManager() {
        representatives = new ArrayList<>();
    }

    public void defineRepresentative() {

        HandleFilesReflection fileHandler = new HandleFilesReflection();

        while (true) {

            System.out.println("Enter representative name or exit:");
            String name = scanner.nextLine();

            if (name.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.println("Enter ID:");
            String id = scanner.nextLine();

            Representative rep = new Representative(name, id);

            representatives.add(rep);

            String folderPath = "Representative";
            new File(folderPath).mkdirs();

            String filePath = folderPath + "/" + rep.getEmployeeCode() + ".csv";

            fileHandler.saveCSV(rep, filePath);

            System.out.println("Saved: " + rep);
        }
    }

    public void loadRepresentatives() {

        HandleFilesReflection fileHandler = new HandleFilesReflection();

        File folder = new File("Representative");

        if (!folder.exists()) {
            return;
        }

        File[] files = folder.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {

            if (file.getName().endsWith(".csv")) {

                Representative rep =
                        (Representative) fileHandler.readCsv(file.getPath());

                representatives.add(rep);
            }
        }
    }

    public void inquiryCreation() {

        System.out.println("Please press the appropriate key.");
        System.out.println("1-question 2-request 3-complain");

        int x = scanner.nextInt();
        scanner.nextLine(); // ניקוי buffer

        switch (x) {
            case 1:
                currentInquiry = new Question();
                break;
            case 2:
                currentInquiry = new Request();
                break;
            case 3:
                currentInquiry = new Complaint();
                break;
            default:
                System.out.println("Inactive key, please press again.");
                return;
        }

        currentInquiry.fillDataByUser();

        currentInquiry.setCode(Inquiry.getNextCodeVal());
//begore change
        //inquiryQueue.add(currentInquiry);
       // handleFiles.saveFile(currentInquiry);


       // כאן השתנה
        boolean saved = handleFiles.saveFile(currentInquiry);
        if (saved) {
            Inquiry.incrementNextCodeVal();
            inquiryQueue.add(currentInquiry);
        } else {
            System.out.println("Save failed, inquiry not created.");
        }
    }

    public void processInquiryManager() {

        while (!inquiryQueue.isEmpty()) {

            Inquiry inquiry = inquiryQueue.poll();

            InquiryHandling inquiryHandling = new InquiryHandling(inquiry);
            inquiryHandling.start();

            try {
                inquiryHandling.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}