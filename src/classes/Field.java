package classes;

public class Field {

    public int size;
    public String type;
    public String name;
    private String content;
    public boolean primaryKey;

    public Field(int size, String type, String name, boolean primaryKey) {
        content = null;
        this.size = size;
        this.type = type;
        this.name = name;
        this.primaryKey = primaryKey;
    }

    public Field(Field clone){
        this.size = clone.size;
        this.type = clone.type;
        this.name = clone.name;
        this.primaryKey = clone.primaryKey;
        this.content = clone.getContent();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) throws LongLengthException {
        if (content.length() > size)
            throw new LongLengthException(
                    "Field length (" + content.length() + ") is bigger than max size allowed (" + size + ")!"
            );
        else
            this.content = content;
    }

    @Override
    public String toString() {
        if (content == null)
            return "metadata";
        return String.format("%-" + size + "s", content);
    }
}
