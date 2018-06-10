package main.classes;

import main.exceptions.LongLengthException;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

    private File file;
    private AvailList aList;
    private Metadata metadata;

    public FileManager(File file) {
        this.file = file;
        metadata = new Metadata(new File(file.getPath() + ".metadata"));
        aList = new AvailList(metadata.getFirstDeleted(), metadata.getLength(), file);
        if (!file.exists())
            touch();
//        load lo utilizabamos para cargar la lista en el interfaz, pero ya no mostramos la lista
//        load();
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
                    metadata.updateLastDeleted(-1);
                else
                    metadata.updateLastDeleted(aList.first.pos);
            }
        }
    }

    public void remove(int pos) {
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file, "rw");
            if (aList.isEmpty()) {
                raFile.seek(metadata.getLength() * pos);
                raFile.writeUTF(metadata.getDeleted());
                metadata.updateLastDeleted(pos);
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
                raFile = new RandomAccessFile(file, "r");
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
            return null;
        }
    }

    private void append(Record record) {
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file, "rw");
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
            raFile = new RandomAccessFile(file, "rw");
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
            raFile = new RandomAccessFile(file, "rw");
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
                raFile = new RandomAccessFile(file, "r");
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

    public boolean exportToCSV() {
        FileWriter fileWriter;
        BufferedWriter bfWriter;
        RandomAccessFile raFile;
        File csvFile = new File(file.getName().split("\\.")[0] + ".csv");
        try {
            fileWriter = new FileWriter(csvFile);
            bfWriter = new BufferedWriter(fileWriter);
            raFile = new RandomAccessFile(file, "r");
            bfWriter.write(metadata.toCSV());
            try {
                String line;
                Record tempRecord;
                while (true) {
                    line = raFile.readUTF();
                    tempRecord = parse(line);
                    if (tempRecord != null)
                        bfWriter.write(tempRecord.toCSV());
                }
            } catch (EOFException e) { }

            bfWriter.flush();
            raFile.close();
            fileWriter.close();
            bfWriter.close();
        } catch (Exception e) {
            System.out.println("Error! " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getFilename() {
        return file.getName();
    }

    public Metadata getMetadata() {
        return metadata;
    }

    // deprecados
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
