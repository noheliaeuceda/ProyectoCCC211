package main.classes;

import java.util.ArrayList;
import java.util.Formatter;

public class Record {

    private ArrayList<Field> fields;

    public Record() {
        fields = new ArrayList<>();
    }

    public void add(Field field) {
        fields.add(field);
    }

    public String getPK() {
        String pk = "";
        for (Field f : fields)
            if (f.primaryKey) {
                pk = f.toString().trim();
                break;
            }
        return pk;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public String prettyString() {
        StringBuilder result = new StringBuilder();
        String pkStr = "";

        for (Field field : fields)
            if (field.primaryKey)
                pkStr = field.toString();
            else
                result.append(field.toString());

        return pkStr + result.toString();
    }

    public String toCSV() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            result.append(fields.get(i).getContent());
            if (i != fields.size() - 1)
                result.append(',');
        }
        result.append('\n');
        return result.toString();
    }

    public String toXML(String recordName) {
        StringBuilder result = new StringBuilder();
        Formatter formatter = new Formatter(result);
        formatter.format("\t<%s id=\"%s\">\n", recordName, getPK());
        for (Field f : fields)
            if (!f.primaryKey)
                formatter.format("\t\t<%s>%s</%s>\n",
                        f.name.replace(' ', '-'),
                        f.getContent(),
                        f.name.replace(' ', '-'));
        formatter.format("\t</%s>\n", recordName);
        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String pkStr = "";

        for (Field field : fields)
            if (field.primaryKey) {
                pkStr = field.prettyString();
            } else {
                result.append(", ");
                result.append(field.prettyString());
            }
        return pkStr + result.toString();
    }

}
