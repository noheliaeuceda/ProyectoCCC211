package main.controllers;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.Main;
import main.classes.Field;

import java.net.URL;
import java.util.ResourceBundle;

public class borrarRegistrosController extends mainController {

    public TextField txtCriteria;
    public ComboBox comboField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
        for (Field f : fileManager.getMetadata().getFieldsData())
            comboField.getItems().add(f);
    }

    public void eliminarPressed() {
        String criteria = txtCriteria.getText();
        Field selectedField = (Field) comboField.getSelectionModel().getSelectedItem();
        if (selectedField != null && !criteria.equals("")) {
            fileManager.delete(selectedField, criteria.trim());
            showSuccess("Operacion realizada con exito!");
        } else {
            showWarning("Por favor llene todos los campos!");
        }
        txtCriteria.setText("");
        comboField.getSelectionModel().clearSelection();
    }
}
