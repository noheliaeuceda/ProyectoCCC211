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

    public Metadata(File file) {
        this.file = file;
        fieldCount = 0;
        fieldsData = new ArrayList<>();
        length = 3;
        firstDeleted = -1;
        if (file.exists())
            loadMetadata();
    }

    private void loadMetadata() {
        RandomAccessFile raFile;
        int tSize;
        boolean tPrimaryKey;
        String tType, tName;
        try {
            raFile = new RandomAccessFile(file, "r");
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

    private void writeMetadata() {
        // TODO truncate file
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file, "rw");
            raFile.writeUTF(Integer.toString(fieldsData.size()) + '\n');

            for (Field f : fieldsData) {
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

    public void updateLastDeleted(int head) {
        // actualiza la posicion del ultimo espacio eliminado en el available list
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file, "rw");
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

    public void setField(int pos, Field field) {
        fieldsData.set(pos, field);
    }

    public boolean hasPK() {
        for (Field f : fieldsData)
            if (f.primaryKey)
                return true;
        return false;
    }

    public void removeField(Field field) {
        fieldsData.remove(field);
    }

    public String toCSV() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < fieldsData.size(); i++) {
            result.append(fieldsData.get(i).name);
            if (i != fieldsData.size() - 1)
                result.append(',');
        }
        result.append('\n');
        return result.toString();
    }

    public int getFirstDeleted() {
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

    public ArrayList<Field> getFieldsData() {
        return fieldsData;
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public boolean exists() {
        return file.exists();
    }

    public Field at(int pos) {
        if (pos < 0 || pos >= fieldCount)
            return null;
        else
            return fieldsData.get(pos);
    }

}
