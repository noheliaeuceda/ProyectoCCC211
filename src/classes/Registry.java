package classes;

import javafx.scene.control.ListView;

import java.io.EOFException;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Registry {

    private File file;
    private ListView fxList;
    private ListView fxListAvail;
    private AvailList database;

    public Registry(String filename, ListView fxList, ListView listAvail) {
        this.database = new AvailList();
        this.fxList = fxList;
        this.fxListAvail = listAvail;
        this.file = new File(filename);
        load();
    }

    public void addPerson(Person person){
        boolean pkRepeated = false;
        Node temp = database.first;
        Person p;
        while (temp != null){
            p = temp.content;
            if (!temp.available && p.getId().equals(person.getId())){
                pkRepeated = true;
                break;
            }
            temp = temp.next;
        }

        if (!pkRepeated) {
            int pos = database.add(person);
            if (pos == -1) {
                append(person);
                fxList.getItems().add(person);
            } else {
                rewrite(person, pos);
                fxList.getItems().set(pos, person);
            }
        }
    }

    public void removePerson(Person person) {
        database.remove(person);
        System.out.println(person);;
    }

    public void load(){
        int deleted = 0;
        if (file.exists()) {
            ArrayList<String> readBuffer = new ArrayList<>();
            RandomAccessFile raFile;
            try {
                raFile = new RandomAccessFile(file.getName(), "r");
                try {
                    String line;
                    while (true) {
                        line = raFile.readUTF();
                        if (readBuffer.size() >= 10)
                            translate(readBuffer);
                        readBuffer.add(line);
                    }
                } catch (EOFException e) { }

                if (!readBuffer.isEmpty())
                    translate(readBuffer);

                raFile.close();
            } catch (Exception e) {
                System.out.println("Error reading from file " + e.getMessage());
                e.printStackTrace();
            }

            Node temp = database.first;
            while (temp != null) {
//                if (temp.content != null)
                    fxList.getItems().add(temp.content);
                temp = temp.next;
            }
            database.init = false;
        }
    }

    public void translate(ArrayList<String> textData){
        int pos;
        String tName, tSurname, tId, tAddress, tPhone, tGender, tRace;
        Person tempPerson;

        for (String data : textData){
            if (data.equals(Person.DELETED)) {
                database.add(null);
            } else {
                pos = 0;
                tId = data.substring(pos, pos + Person.ID_SIZE).trim();
                pos += Person.ID_SIZE;
                tName = data.substring(pos, pos + Person.NAME_SIZE).trim();
                pos += Person.NAME_SIZE;
                tSurname = data.substring(pos, pos + Person.SURNAME_SIZE).trim();
                pos += Person.SURNAME_SIZE;
                tAddress = data.substring(pos, pos + Person.ADDRESS_SIZE).trim();
                pos += Person.ADDRESS_SIZE;
                tPhone = data.substring(pos, pos + Person.PHONE_SIZE).trim();
                pos += Person.PHONE_SIZE;
                tGender = data.substring(pos, pos + Person.GENDER_SIZE).trim();
                pos += Person.GENDER_SIZE;
                tRace = data.substring(pos, pos + Person.RACE_SIZE).trim();

                try {
                    tempPerson = new Person(tName, tSurname, tId, tAddress, tPhone, tGender, tRace);
                    database.add(tempPerson);
                } catch (LongLengthException e) {
                    System.out.println("Error adding element " + e.getMessage());
                }
            }
        }
        textData.clear();
    }

    public void append(Person person){
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file.getName(), "rw");
            raFile.seek(raFile.length());
            raFile.writeUTF(person.prettyString() + '\n');
            raFile.close();
        } catch (Exception e) {
            System.out.println("Error writing to file " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void rewrite(Person person, int pos){
        RandomAccessFile raFile;
        try {
            raFile = new RandomAccessFile(file.getName(), "rw");
            raFile.seek(Person.LENGTH * pos);
            raFile.writeUTF(person.prettyString() + '\n');
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
            Node temp = database.first;

            while (temp != null){
                if (temp.available){
                    raFile.seek(Person.LENGTH * temp.pos);
                    raFile.writeUTF(Person.DELETED);
                }
                temp = temp.next;
            }

            raFile.close();
        } catch (Exception e) {
            System.out.println("Error writing to file " + e.getMessage());
            e.printStackTrace();
        }
    }
}
