package main.controllers;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.Main;
import main.classes.Field;

import java.net.URL;
import java.util.ResourceBundle;

public class buscarRegistrosController extends mainController {

    public ComboBox comboField;
    public TextField txtPK;
    public ListView listSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
        for (Field f : fileManager.getMetadata().getFieldsData())
            comboField.getItems().add(f);
    }

    public void buscarPressed() {
        String criteria = txtPK.getText();
        Field selectedField = (Field) comboField.getSelectionModel().getSelectedItem();
        if (selectedField != null) {
            fileManager.loadList(listSearch, selectedField, criteria.trim());
            showSuccess("Se encontraron " + listSearch.getItems().size() + " elemento(s)!");
        } else {
            showWarning("Por favor llene todos los campos!");
        }
        txtPK.setText("");
        comboField.getSelectionModel().clearSelection();
    }
}
