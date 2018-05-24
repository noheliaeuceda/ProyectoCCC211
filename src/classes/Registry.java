package classes;

import javafx.scene.control.ListView;

import java.io.EOFException;
import java.io.File;
import java.io.RandomAccessFile;

public class Registry {

    private File file;
    private ListView fxList;
    private AvailList aList;
    private Metadata metadata;

    public Registry(String filename, ListView fxList) {
        aList = new AvailList();
        this.fxList = fxList;
        this.file = new File(filename + ".txt");
        // TODO buscar el nombre del archivo de metadata
        metadata = new Metadata(filename + "Metadata.txt");
        load();
    }

    public void changeMetadata(Field... fields){
        metadata = new Metadata(file.getName() + "Metadata.txt", fields);
    }

    public void add(String... fields) {
        Record record = parse(fields);
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
            aList.init = false;
        }
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
                tempField = new Field(f);
                tempRecord.add(tempField, data.substring(pos, pos + f.size));
                pos += f.size;
            }
            aList.add(tempRecord);
            fxList.getItems().add(tempRecord);
        } catch (LongLengthException e){
            System.out.println("Error creating record! " + e.getMessage());
        }
    }

    public Record parse(String... fields){
        // TODO suponiendo que el orden en que son recibidos los argumentos es el mismo que el de la metadata
        int pos = 0;
        Record tempRecord = null;
        Field tempField;
        try {
            tempRecord = new Record();
            for (Field f : metadata.getFieldsData()){
                tempField = new Field(f);
                tempRecord.add(tempField, fields[pos]);
                pos++;
            }
        } catch (LongLengthException e){
            System.out.println("Error creating record! " + e.getMessage());
        } catch (Exception e) {
            System.out.println(1);
            e.printStackTrace();
        }
        return tempRecord;
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

    public void printAvailList(){
        aList.print();
    }
}
