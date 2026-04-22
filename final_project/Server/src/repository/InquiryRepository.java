package repository;

import data.Inquiry;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InquiryRepository {

    File folder = new File("data");

    public Collection<Inquiry> readAll() {

        List<Inquiry> res = new ArrayList<>();

        if (!folder.exists()) {
            return res;
        }

        File[] subFolders = folder.listFiles();
        if (subFolders == null) return res;

        for (File subFolder : subFolders) {
            readSubFileInquiries(subFolder, res);
        }

        return res;
    }

    private static void readSubFileInquiries(File subFolder, List<Inquiry> res) {

        File[] files = subFolder.listFiles();
        if (files == null) return;

        for (File file : files) {
            readOneInquiry(file, res);
        }
    }

    private static void readOneInquiry(File file, List<Inquiry> res) {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {

            Inquiry inquiry = (Inquiry) in.readObject();
            res.add(inquiry);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(Inquiry inquiry) {

        try {
            File typeFolder = new File(folder, inquiry.getType().name().toLowerCase());
            if (!typeFolder.exists()) {
                typeFolder.mkdirs();
            }

            File file = new File(typeFolder, inquiry.getCode() + ".bin");

            try (ObjectOutputStream out =
                         new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(inquiry);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}