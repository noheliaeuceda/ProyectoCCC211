package main.controllers;

import javafx.scene.control.ListView;
import main.Main;
import main.classes.Field;

import java.net.URL;
import java.util.ResourceBundle;

public class borrarCamposController extends mainController {

    public ListView listShowFields;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
        for (Field f : fileManager.getMetadata().getFieldsData())
            listShowFields.getItems().add(f);
    }

    public void eliminarPressed() {
        int pos = listShowFields.getSelectionModel().getSelectedIndex();
        Field selectedField = (Field) listShowFields.getSelectionModel().getSelectedItem();
        if (selectedField != null) {
            fileManager.getMetadata().removeField(selectedField);
            listShowFields.getItems().remove(pos);
        }
    }

}
