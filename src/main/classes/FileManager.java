package main.classes;

import main.exceptions.LongLengthException;

import java.io.EOFException;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class FileManager {

    private File file;
    private String filename;
    private AvailList aList;
    private Metadata metadata;

    public FileManager(String filename) {
        this.filename = filename;
        this.file = new File(filename);
        metadata = new Metadata(filename + ".metadata");
        aList = new AvailList(metadata.getFirstDeleted(), metadata.getLength(), filename);
        touch();
//        load lo utilizabamos para cargar la lista en el interfaz, pero ya no mostramos la lista
//        load();
    }

    public void changeMetadata(ArrayList<Field> fields, int fieldCount){
        metadata = new Metadata(filename + ".metadata", fieldCount, fields);
    }

    public void add(ArrayList<Field> fields) {
        Record record = new Record(fields);
        if (!pkExists(record.getPK()) && record.prettyString().charAt(0) != '*') {
            if (aList.isEmpty()) {
                append(record);
            } else {
                int pos = aList.remove();
                rewrite(record, pos);
                if (aList.isEmpty())
                    metadata.update(-1);
                else
                    metadata.update(aList.first.pos);
            }
        }
    }

    public void remove(int pos) {
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file.getName(), "rw");
            if (aList.isEmpty()) {
                raFile.seek(metadata.getLength() * pos);
                raFile.writeUTF(metadata.getDeleted());
                metadata.update(pos);
            } else {
                raFile.seek(metadata.getLength() * pos);
                raFile.writeUTF(metadata.getDeleted());
                raFile.seek(0);
                raFile.seek(metadata.getLength() * aList.last.pos);
                raFile.writeUTF(metadata.getDeleted(pos));
            }
            aList.add(pos);
            raFile.close();
        } catch (Exception e) {
            System.out.println("Error writing to file " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void load() {
        if (file.exists()) {
            Record tempRecord;
            RandomAccessFile raFile;
            try {
                raFile = new RandomAccessFile(file.getName(), "r");
                try {
                    while (true) {
                        tempRecord = parse(raFile.readUTF());
                    }
                } catch (EOFException e) { }

                raFile.close();
            } catch (Exception e) {
                System.out.println("Error reading from file " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private Record parse(String data) {
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
            return tempRecord;
        } catch (LongLengthException e){
            System.out.println("Error creating record! " + e.getMessage());
        }
        return null;
    }

    private void append(Record record) {
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

    private void rewrite(Record record, int pos) {
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

    private void touch() {
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file.getName(), "rw");
            raFile.close();
            raFile = new RandomAccessFile(file.getName() + ".metadata", "rw");
            raFile.close();
        } catch (Exception e) {
            System.out.println("Error with file " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean pkExists(String pk) {
        Record tempRecord;
        if (file.exists()) {
            RandomAccessFile raFile;
            try {
                raFile = new RandomAccessFile(file.getName(), "r");
                try {
                    while (true) {
                        tempRecord = parse(raFile.readUTF());
                        if (tempRecord.getPK().equals(pk))
                            return true;
                    }
                } catch (EOFException e) { }

                raFile.close();
            } catch (Exception e) {
                System.out.println("Error reading from file " + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
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
