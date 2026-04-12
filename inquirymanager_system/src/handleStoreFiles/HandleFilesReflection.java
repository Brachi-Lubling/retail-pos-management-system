package handleStoreFiles;

import java.io.*;
import java.lang.reflect.Field;
import java.util.StringJoiner;

public class HandleFilesReflection extends HandleFiles {

    public String getCSVDataRecursive(Object obj) {
        if (obj == null) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(",");

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value == null) {
                    joiner.add("null");
                }
                else if (isSimpleType(field.getType())) {
                    joiner.add("\"" + value.toString() + "\"");
                }
                else if (field.getType().getPackageName().startsWith("java")) {
                    joiner.add("\"" + value.toString() + "\"");
                }
                else {
                    // אובייקט פנימי
                    joiner.add(getCSVDataRecursive(value));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return joiner.toString();
    }

    public boolean saveCSV(Object obj, String filePath) {

        // שם המחלקה ראשון בקובץ
        String data = obj.getClass().getName() + "," + getCSVDataRecursive(obj);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isSimpleType(Class<?> type) {
        return type.isPrimitive() ||
                type == String.class ||
                type == Integer.class ||
                type == Double.class ||
                type == Boolean.class ||
                type == Long.class ||
                type == Float.class ||
                type == Short.class ||
                type == Byte.class ||
                type == Character.class;
    }

    public Object readCsv(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line = reader.readLine();

            if (line == null || line.isEmpty()) {
                return null;
            }

            String[] values = line.split(",");

            // הערך הראשון = שם המחלקה
            String className = values[0].replace("\"", "");

            Class<?> clazz = Class.forName(className);

            // יצירת אובייקט ריק
            Object obj = clazz.getDeclaredConstructor().newInstance();

            Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {

                fields[i].setAccessible(true);

                String value = values[i + 1].replace("\"", "");

                if (value.equals("null")) {
                    fields[i].set(obj, null);
                    continue;
                }

                Class<?> type = fields[i].getType();

                if (type == int.class || type == Integer.class) {
                    fields[i].set(obj, Integer.parseInt(value));
                }
                else if (type == double.class || type == Double.class) {
                    fields[i].set(obj, Double.parseDouble(value));
                }
                else if (type == boolean.class || type == Boolean.class) {
                    fields[i].set(obj, Boolean.parseBoolean(value));
                }
                else if (type == long.class || type == Long.class) {
                    fields[i].set(obj, Long.parseLong(value));
                }
                else {
                    fields[i].set(obj, value);
                }
            }

            return obj;

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}