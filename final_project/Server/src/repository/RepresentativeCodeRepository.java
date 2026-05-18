package repository;

import java.io.*;

public class RepresentativeCodeRepository {

    private static final String PATH =
            "data/representative_next_code.txt";

    public synchronized int get() {

        File file = new File(PATH);

        if (!file.exists()) {
            return 1000;
        }

        try (BufferedReader br =
                     new BufferedReader(new FileReader(file))) {

            String line = br.readLine();

            if (line == null || line.isEmpty()) {
                return 1000;
            }

            return Integer.parseInt(line);

        } catch (Exception e) {
            e.printStackTrace();
            return 1000;
        }
    }

    public synchronized void update(int value) {

        try {

            File parent = new File("data");

            if (!parent.exists()) {
                parent.mkdirs();
            }

            try (BufferedWriter bw =
                         new BufferedWriter(new FileWriter(PATH))) {

                bw.write(String.valueOf(value));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}