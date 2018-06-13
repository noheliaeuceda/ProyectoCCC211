package main.classes;

import main.exceptions.LongLengthException;

public class Field {

    public int size;
    public String type;
    public String name;
    private String content;
    public boolean primaryKey;

    public Field(int size, String type, String name, boolean primaryKey) {
        content = null;
        this.size = size;
        this.type = type.trim();
        this.name = name.trim();
        this.primaryKey = primaryKey;
    }

    public Field(Field clone, String content) throws LongLengthException {
        this.size = clone.size;
        this.type = clone.type;
        this.name = clone.name;
        this.primaryKey = clone.primaryKey;
        setContent(content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) throws LongLengthException {
        content = content.trim();
        if (content.length() > size)
            throw new LongLengthException(
                    "Field length (" + content.length() + ") is bigger than max size allowed (" + size + ")!"
            );
        else
            this.content = content;
    }

    public boolean equals(Field other) {
        if (other == null)
            return false;

        return primaryKey == other.primaryKey
                && size == other.size
                && type.equals(other.type)
                && name.equals(other.name);
    }

    @Override
    public String toString() {
        if (content == null)
            return "Field name: " + name + ", type: " + type + ", size: " + size + ", is primary key: " + primaryKey;
        else
            return String.format("%-" + size + "s", content);
    }
}
