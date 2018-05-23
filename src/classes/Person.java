package classes;

public class Person {

    public static final int NAME_SIZE = 30;
    public static final int SURNAME_SIZE = 30;
    public static final int ID_SIZE = 15;
    public static final int ADDRESS_SIZE = 100;
    public static final int PHONE_SIZE = 20;
    public static final int GENDER_SIZE = 20;
    public static final int RACE_SIZE = 20;
    public static final int LENGTH =
            NAME_SIZE + SURNAME_SIZE + ID_SIZE + ADDRESS_SIZE + PHONE_SIZE + GENDER_SIZE + RACE_SIZE + 3;
    public static final String DELETED = String.format("%" + (LENGTH - 2) + "s", "\n").replace(' ', '*');


    private String name;
    private String surname;
    private String id;
    private String address;
    private String phone;
    private String gender;
    private String race;

    public Person(String name, String surname, String id, String address, String phone, String gender, String race)
            throws LongLengthException {
        setName(name);
        setSurname(surname);
        setId(id);
        setAddress(address);
        setPhone(phone);
        setGender(gender);
        setRace(race);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws LongLengthException {
        if (name.length() > NAME_SIZE)
            throw new LongLengthException(
                    "Name length (" + name.length() + ") is bigger than max size allowed (" + NAME_SIZE + ")!"
            );
        else
            this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) throws LongLengthException {
        if (surname.length() > SURNAME_SIZE)
            throw new LongLengthException(
                    "Surname length (" + surname.length() + ") is bigger than max size allowed (" + SURNAME_SIZE + ")!"
            );
        else
            this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) throws LongLengthException {
        if (id.length() > ID_SIZE)
            throw new LongLengthException(
                    "Id length (" + id.length() + ") is bigger than max size allowed (" + ID_SIZE + ")!"
            );
        else
            this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) throws LongLengthException {
        if (address.length() > ADDRESS_SIZE)
            throw new LongLengthException(
                    "Address length (" + address.length() + ") is bigger than max size allowed (" + ADDRESS_SIZE + ")!"
            );
        else
            this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws LongLengthException {
        if (phone.length() > PHONE_SIZE)
            throw new LongLengthException(
                    "Phone length (" + phone.length() + ") is bigger than max size allowed (" + PHONE_SIZE + ")!"
            );
        else
            this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) throws LongLengthException {
        if (gender.length() > GENDER_SIZE)
            throw new LongLengthException(
                    "Gender length (" + gender.length() + ") is bigger than max size allowed (" + GENDER_SIZE + ")!"
            );
        else
            this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) throws LongLengthException {
        if (race.length() > RACE_SIZE)
            throw new LongLengthException(
                    "Race length (" + race.length() + ") is bigger than max size allowed (" + RACE_SIZE + ")!"
            );
        else
            this.race = race;
    }

    public String toString(){
        return "ID: " + id + ", " + name + " " + surname + ", " + gender + " " + race;
    }

    public String prettyString(){
        String tId = String.format("%-" + ID_SIZE + "s", id);
        String tName = String.format("%-" + NAME_SIZE + "s", name);
        String tSurname = String.format("%-" + SURNAME_SIZE + "s", surname);
        String tAddress = String.format("%-" + ADDRESS_SIZE + "s", address);
        String tPhone = String.format("%-" + PHONE_SIZE + "s", phone);
        String tGender = String.format("%-" + GENDER_SIZE + "s", gender);
        String tRace = String.format("%-" + RACE_SIZE + "s", race);
        return tId + tName + tSurname + tAddress + tPhone + tGender + tRace;
    }
}
