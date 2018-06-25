package main.classes;

import javafx.scene.control.ListView;
import main.classes.btree.Btree;
import main.classes.btree.FileIndex;
import main.exceptions.LongLengthException;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

    private Btree btree;
    private int rrn;
    private File file;
    private AvailList aList;
    private Metadata metadata;
    private ArrayList<Record> records;

    public FileManager(File file) {
        btree = new Btree(6);
        rrn = 0;
        this.file = file;
        records = new ArrayList<>();
        metadata = new Metadata(new File(file.getPath() + ".metadata"));
        aList = new AvailList(metadata.getFirstDeleted(), metadata.getLength(), file);
        if (!file.exists())
            touch();
    }

    public void loadTree() {
        Record tempRecord;
        FileIndex index;
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file, "r");
            try {
                while (true) {
                    tempRecord = parse(raFile.readUTF());
                    if (tempRecord == null)
                        continue;
                    index = new FileIndex(tempRecord.getPK(), rrn);
                    btree.insert(index.getId(), index);
                    rrn++;
                }
            } catch (EOFException e) { }
            raFile.close();
        } catch (Exception e) {
            System.out.println("Error! " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeFile() {
        for (Record record : records)
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
        records.clear();
    }

    public void remove(int pos, String pk) {
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
            btree.delete(pk);
            raFile.close();
        } catch (Exception e) {
            System.out.println("Error writing to file " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Record parse(String data) {
        if (data.charAt(0) == '*')
            return null;

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
        } catch (LongLengthException e) {
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

    private boolean freePK(String pk) {
        return btree.searchNode(pk) == null;
    }

    public void loadList(ListView list) {
        Record tempRecord;
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file, "r");
            try {
                while (true) {
                    tempRecord = parse(raFile.readUTF());
                    list.getItems().add(tempRecord);
                }
            } catch (EOFException e) { }

            raFile.close();
        } catch (Exception e) {
            System.out.println("Error reading from file " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Object[] search(String pk) {
        FileIndex nodo = btree.searchNode(pk);
        if (nodo == null)
            return null;

        int pos = nodo.getRrn();
        Record record;
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file, "r");
            raFile.seek(metadata.getLength() * pos);
            record = parse(raFile.readUTF());
            if (record != null) {
                raFile.close();
                return new Object[]{pos, record};
            } else {
                raFile.close();
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error! " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean addRecord(Record record) {
//        TODO guardar en el archivo cada vez que se realice una operacion, entonces no ocupariamos guardar en el arbol aqui
        if (freePK(record.getPK())) {
//            FileIndex index = new FileIndex(record.getPK(), rrn);
            records.add(record);
//            btree.insert(index.getId(), index);
            return true;
        }
        return false;
    }

    public void setRecord(int pos, Record record) {
        rewrite(record, pos);
    }

    public void save() {
        metadata.writeMetadata();
        writeFile();
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

    public void merge(FileManager first, FileManager second) {
        first.getMetadata().copy(metadata);
        RandomAccessFile mainFile, firstFile, secondFile;
        Record record;
        String txtRecord;
        try {
            mainFile = new RandomAccessFile(file, "rw");
            firstFile = new RandomAccessFile(first.getFile(), "r");
            secondFile = new RandomAccessFile(second.getFile(), "r");

            // leer el primer archivo
            try {
                while (true) {
                    txtRecord = firstFile.readUTF();
                    record = parse(txtRecord);
                    if (record == null || !freePK(record.getPK()))
                        continue;
                    mainFile.writeUTF(txtRecord);
                }
            } catch (EOFException e) { }

            // leer el segundo archivo
            try {
                while (true) {
                    txtRecord = secondFile.readUTF();
                    record = parse(txtRecord);
                    if (record == null || !freePK(record.getPK()))
                        continue;
                    if (txtRecord.charAt(0) != '*')
                        mainFile.writeUTF(txtRecord);
                }
            } catch (EOFException e) { }

            mainFile.close();
            firstFile.close();
            secondFile.close();
        } catch (Exception e) {
        }
    }

    public boolean hasRecords() {
        if (records.size() > 0)
            return true;

        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file, "r");
            raFile.readUTF();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public File getFile() {
        return file;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public String getFilename() {
        return file.getName();
    }

    public Metadata getMetadata() {
        return metadata;
    }

}
