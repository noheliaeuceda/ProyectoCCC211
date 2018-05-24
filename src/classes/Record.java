package classes;

import java.util.ArrayList;

public class Record {

    // TODO no dejar al usuario hacer operaciones hasta que se ingrese una llave primaria
    // TODO verificar que solo hay una llave primaria

    private ArrayList<Field> fields;

    public Record() {
        fields = new ArrayList<>();
    }

    public void add(Field field, String content) throws LongLengthException {
        field.setContent(content);
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

    @Override
    public String toString(){
        // TODO cambiar el toString por algo mas legible
        StringBuilder result = new StringBuilder();
        String pkStr = "";

        for (Field field : fields)
            if (field.primaryKey)
                pkStr = field.toString();
            else
                result.append(field.toString());

        return pkStr + result.toString();
    }

}
