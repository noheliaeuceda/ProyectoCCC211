package classes;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

public class Metadata {

    // Metadata file structure:
    // Number of fields
    // size
    // type
    // name
    // is primary key

    private File file;
    private ArrayList<Field> fieldsData;
    private int length;
    private String deleted;

    public Metadata(String filename) {
        file = new File(filename);
        fieldsData = new ArrayList<>();
        length = 3;
        deleted = String.format("%" + (length - 2) + "s", "\n").replace(' ', '*');
        loadMetadata();
    }

    public Metadata(String filename, Field... fields) {
        file = new File(filename);
        fieldsData = new ArrayList<>();
        length = 3;
        deleted = String.format("%" + (length - 2) + "s", "\n").replace(' ', '*');
        createMetadata(fields);
    }

    private void loadMetadata() {
        RandomAccessFile raFile;
        int fieldCount, tSize;
        boolean tPrimaryKey;
        String tType, tName;
        try {
            raFile = new RandomAccessFile(file.getName(), "rw");
            fieldCount = Integer.valueOf(raFile.readUTF());

            for (int i = 0; i < fieldCount; i++) {
                tSize = Integer.valueOf(raFile.readUTF().trim());
                tType = raFile.readUTF().trim();
                tName = raFile.readUTF().trim();
                tPrimaryKey = Boolean.valueOf(raFile.readUTF().trim());
                addField(new Field(tSize, tType, tName, tPrimaryKey));
            }

            raFile.close();
        } catch (Exception e) {
            System.out.println("Error writing to file " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createMetadata(Field... fields) {
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file.getName(), "rw");
            raFile.writeUTF(Integer.toString(fields.length) + '\n');

            for (int i = 0; i < fields.length; i++) {
                raFile.writeUTF(Integer.toString(fields[i].size));
                raFile.writeUTF(fields[i].type);
                raFile.writeUTF(fields[i].name);
                raFile.writeUTF(Boolean.toString(fields[i].primaryKey) + '\n');
                addField(fields[i]);
            }

            raFile.close();
        } catch (Exception e) {
            System.out.println("Error writing to file " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addField(Field field) {
        fieldsData.add(field);
        length += field.size;
        deleted = String.format("%" + (length - 2) + "s", "\n").replace(' ', '*');
    }

    public int getLength() {
        return length;
    }

    public String getDeleted() {
        return deleted;
    }

    public ArrayList<Field> getFieldsData(){
        return fieldsData;
    }
}
