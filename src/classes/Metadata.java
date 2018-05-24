package classes;

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

    private File file;
    private int fieldCount;
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

    public Metadata(String filename, int fieldCount, ArrayList<Field> fields) {
        file = new File(filename);
        this.fieldCount = fieldCount;
        fieldsData = new ArrayList<>();
        length = 3;
        deleted = String.format("%" + (length - 2) + "s", "\n").replace(' ', '*');
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
            // TODO truncar
            raFile.writeUTF(Integer.toString(fields.size()) + '\n');

            for (Field f : fields){
                raFile.writeUTF(Integer.toString(f.size));
                raFile.writeUTF(f.type);
                raFile.writeUTF(f.name);
                raFile.writeUTF(Boolean.toString(f.primaryKey) + '\n');
                addField(f);
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
