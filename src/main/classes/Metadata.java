package main.classes;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Metadata {

    // Metadata file structure:
    // Number of fields
    // size
    // type
    // name
    // is primary key
    // Pos of first deleted element

    private File file;
    private int fieldCount;
    private ArrayList<Field> fieldsData;
    private int length;
    private int firstDeleted;

    public Metadata(String filename) {
        file = new File(filename);
        fieldsData = new ArrayList<>();
        length = 3;
        firstDeleted = -1;
        loadMetadata();
    }

    public Metadata(String filename, int fieldCount, ArrayList<Field> fields) {
        file = new File(filename);
        this.fieldCount = fieldCount;
        fieldsData = new ArrayList<>();
        length = 3;
        createMetadata(fields);
    }

    private void loadMetadata() {
        if (file.exists()) {
            RandomAccessFile raFile;
            int tSize;
            boolean tPrimaryKey;
            String tType, tName;
            try {
                raFile = new RandomAccessFile(file.getName(), "r");
                fieldCount = Integer.valueOf(raFile.readUTF().trim());

                for (int i = 0; i < fieldCount; i++) {
                    tSize = Integer.valueOf(raFile.readUTF().trim());
                    tType = raFile.readUTF().trim();
                    tName = raFile.readUTF().trim();
                    tPrimaryKey = Boolean.valueOf(raFile.readUTF().trim());
                    addField(new Field(tSize, tType, tName, tPrimaryKey));
                }
                firstDeleted = Integer.valueOf(raFile.readUTF().trim());
                raFile.close();
            } catch (Exception e) {
                System.out.println("Error reading from file " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void createMetadata(ArrayList<Field> fields) {
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file.getName(), "rw");
            // TODO truncate
            raFile.writeUTF(Integer.toString(fields.size()) + '\n');

            for (Field f : fields){
                raFile.writeUTF(Integer.toString(f.size));
                raFile.writeUTF(f.type);
                raFile.writeUTF(f.name);
                raFile.writeUTF(Boolean.toString(f.primaryKey) + '\n');
                addField(f);
            }
            raFile.writeUTF("-1");
            raFile.close();
        } catch (Exception e) {
            System.out.println("Error writing to file " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update(int head) {
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file.getName(), "rw");
            raFile.readUTF();
            for (int i = 0; i < fieldCount; i++) {
                raFile.readUTF();
                raFile.readUTF();
                raFile.readUTF();
                raFile.readUTF();
            }
            raFile.writeUTF(Integer.toString(head));
            raFile.close();
        } catch (Exception e) {
            System.out.println("Error reading from file " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addField(Field field) {
        fieldsData.add(field);
        length += field.size;
    }

    public int getFirstDeleted(){
        return firstDeleted;
    }

    public int getLength() {
        return length;
    }

    public String getDeleted() {
        return "*-1" + String.format("%" + (length - 5) + "s", "\n");
    }

    public String getDeleted(int pos) {
        String strPos = Integer.toString(pos);
        return "*" + strPos + String.format("%" + (length - 3 - strPos.length()) + "s", "\n");
    }

    public int getFieldCount(){
        return fieldCount;
    }

    public boolean exists(){
        return file.exists();
    }

    public Field at(int pos) {
        if (pos < 0 || pos >= fieldCount)
            return null;
        else
            return fieldsData.get(pos);
    }

    public ArrayList<Field> getFieldsData(){
        return fieldsData;
    }
}
