package classes;

import javafx.scene.control.ListView;

import java.io.EOFException;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Registry {

    private File file;
    private String filename;
    private ListView fxList;
    private AvailList aList;
    private Metadata metadata;

    public Registry(String filename, ListView fxList) {
        aList = new AvailList();
        this.filename = filename;
        this.fxList = fxList;
        this.file = new File(filename + ".txt");
        metadata = new Metadata(filename + "Metadata.txt");
        load();
    }

    public void changeMetadata(ArrayList<Field> fields, int fieldCount){
        metadata = new Metadata(filename + "Metadata.txt", fieldCount, fields);
    }

    public void add(ArrayList<Field> fields) {
        Record record = new Record(fields);
        boolean pkRepeated = false;
        Node temp = aList.first;
        while (temp != null) {
            if (!temp.available && record.getPK().equals(temp.id)){
                pkRepeated = true;
                break;
            }
            temp = temp.next;
        }

        if (!pkRepeated) {
            int pos = aList.add(record);
            if (pos == -1) {
                append(record);
                fxList.getItems().add(record);
                fxList.refresh();
            } else {
                rewrite(record, pos);
                fxList.getItems().set(pos, record);
            }
        }
    }

    public void remove(Record record) {
        aList.remove(record);
        update();
    }

    public void load(){
        if (file.exists()) {
            RandomAccessFile raFile;
            try {
                raFile = new RandomAccessFile(file.getName(), "r");
                try {
                    while (true)
                        translate(raFile.readUTF());
                } catch (EOFException e) { }

                raFile.close();
            } catch (Exception e) {
                System.out.println("Error reading from file " + e.getMessage());
                e.printStackTrace();
            }
//             TODO fix
//            aList.init = false;
        }
        aList.init = false;
    }

    public void translate(String data){
        if (data.equals(metadata.getDeleted())) {
            aList.add(null);
            fxList.getItems().add(null);
        } else {
            parse(data);
        }
    }

    public void parse(String data){
        int pos;
        Record tempRecord;
        Field tempField;
        try {
            tempRecord = new Record();
            pos = 0;
            for (Field f : metadata.getFieldsData()) {
                tempField = new Field(f, data.substring(pos, pos + f.size));
                tempRecord.add(tempField);
                pos += f.size;
            }
            aList.add(tempRecord);
            fxList.getItems().add(tempRecord);
        } catch (LongLengthException e){
            System.out.println("Error creating record! " + e.getMessage());
        }
    }

    public void append(Record record){
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file.getName(), "rw");
            raFile.seek(raFile.length());
            raFile.writeUTF(record.prettyString() + '\n');
            raFile.close();
        } catch (Exception e) {
            System.out.println("Error writing to file " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void rewrite(Record record, int pos){
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file.getName(), "rw");
            raFile.seek(metadata.getLength() * pos);
            raFile.writeUTF(record.prettyString() + '\n');
            raFile.close();
        } catch (Exception e) {
            System.out.println("Error writing to file " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update(){
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file.getName(), "rw");
            Node temp = aList.first;

            while (temp != null){
                if (temp.available){
                    raFile.seek(metadata.getLength() * temp.pos);
                    raFile.writeUTF(metadata.getDeleted());
                }
                temp = temp.next;
            }

            raFile.close();
        } catch (Exception e) {
            System.out.println("Error writing to file " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getFieldCount() {
        return metadata.getFieldCount();
    }

    public Field at(int pos) {
        return metadata.at(pos);
    }

    public boolean hasMetadata(){
        return metadata.exists();
    }

    public void printAvailList(){
        aList.print();
    }
}
