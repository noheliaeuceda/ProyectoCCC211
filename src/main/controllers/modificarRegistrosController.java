package main.controllers;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import main.Main;
import main.classes.Field;
import main.classes.Record;
import main.exceptions.LongLengthException;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class modificarRegistrosController extends mainController {

    // TODO (?) ver si se puede modificar la llave primaria

    public VBox vboxMain;
    public TextField txtPK;
    private TextField txtModPK;
    private ArrayList<TextField> textFields;
    private int modifiedPos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextField textField;
        Region padding;
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());

        textFields = new ArrayList<>();
        for (Field f : fileManager.getMetadata().getFieldsData()) {
            // crear el textfield
            textField = new TextField();
            textFields.add(textField);
            vboxMain.getChildren().add(new Label(f.toString()));

            // crear el espacio en blanco entre elementos
            padding = new Region();
            padding.setPadding(new Insets(6, 0, 6, 0));
            vboxMain.getChildren().add(padding);

            vboxMain.getChildren().add(textField);

            // crear mas espacio en blanco entre elementos
            padding = new Region();
            padding.setPadding(new Insets(6, 0, 6, 0));
            vboxMain.getChildren().add(padding);

            if (f.primaryKey)
                txtModPK = textField;
        }
        vboxMain.setDisable(true);
    }

    public void selectPressed() {
        Object[] result = fileManager.search(txtPK.getText());
        if (result == null) {
            showWarning("No existe un registro con esa llave primaria!");
        } else {
            Record record = (Record) result[1];
            modifiedPos = (int) result[0];
            vboxMain.setDisable(false);
            txtModPK.setDisable(true);

            String temp;
            for (int i = 0; i < textFields.size(); i++) {
                temp = record.getFields().get(i).getContent();
                textFields.get(i).setText(temp);
            }
        }
        txtPK.setText("");
    }

    public void modificarPressed() {
        Field field;
        String text;
        boolean error = false;
        Record record = new Record();

        for (int i = 0; i < textFields.size(); i++) {
            field = fileManager.getMetadata().at(i);
            text = textFields.get(i).getText();

            if (text.equals("") || text.charAt(0) == '*' || text.length() > field.size) {
                error = true;
                break;
            }

            if (field.type.equals("int")) {
                try {
                    Integer.valueOf(text);
                } catch (NumberFormatException e) {
                    error = true;
                    break;
                }
            } else if (field.type.equals("float")) {
                try {
                    Float.valueOf(text);
                } catch (NumberFormatException e) {
                    error = true;
                    break;
                }
            }
            try {
                record.add(new Field(field, text));
            } catch (LongLengthException e) {
                System.out.println("Something went terribly wrong! " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (!error) {
            fileManager.setRecord(modifiedPos, record);
            showSuccess("Registro agregado con exito!");
        } else {
            showWarning("Por favor no deje campos en blanco o ingrese asteriscos al inicio de un campo!");
        }

        vboxMain.setDisable(true);
        for (TextField textField : textFields)
            textField.setText("");
    }
}
