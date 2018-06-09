package main.classes;

import java.util.ArrayList;

public class Record {

    // TODO no dejar al usuario hacer operaciones hasta que se ingrese una llave primaria
    // TODO verificar que solo hay una llave primaria (se verifica pero en el controlador, no en la clase)

    private ArrayList<Field> fields;

    public Record() {
        fields = new ArrayList<>();
    }

    public Record(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public void add(Field field){
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
