package main.controllers;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import main.Main;
import main.classes.Field;
import main.classes.FileManager;

import java.net.URL;
import java.util.ResourceBundle;

public class agregarCamposController extends mainController {

    public TextField txtName;
    public TextField txtSize;
    public RadioButton rbTypeString;
    public RadioButton rbTypeInt;
    public RadioButton rbTypeFloat;
    public RadioButton rbYesPk;
    public RadioButton rbNotPk;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
    }

    public void agregarCampoPressed() {
        int size;
        boolean primaryKey;
        String name, type;

        try {
            size = Integer.valueOf(txtSize.getText());
            name = txtName.getText();
            primaryKey = rbYesPk.isSelected();

            if (rbTypeFloat.isSelected())
                type = "float";
            else if (rbTypeInt.isSelected())
                type = "int";
            else
                type = "string";

            if (fileManager.getMetadata().hasPK() && primaryKey) {
                showWarning("Ya existe una llave primaria!");
            } else {
                fileManager.getMetadata().addField(new Field(size, type, name, primaryKey));
                // TODO hacer un metodo para cargar
                fileManager.save();
                Main.setFileManager(new FileManager(fileManager.getFile()));
                showSuccess("Campo agregado con exito!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error!" + e.getMessage());
        }

        txtName.setText("");
        txtSize.setText("");
        rbTypeString.setSelected(true);
        rbNotPk.setSelected(true);
    }

}
