package main.controllers;

import javafx.scene.control.ListView;
import main.Main;
import main.classes.Field;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class borrarCamposController extends mainController {

    public ListView listShowFields;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();

        for (Field f : fileManager.getMetadata().getFieldsData()) {
            listShowFields.getItems().add(f);
        }

        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
    }

    public void eliminarPressed() {
        int pos = listShowFields.getSelectionModel().getSelectedIndex();
        Field selectedField = (Field) listShowFields.getSelectionModel().getSelectedItem();
        if (selectedField != null) {
            fileManager.getMetadata().removeField(selectedField);
            listShowFields.getItems().remove(pos);
        }
    }

    public void returnPressed () throws IOException {
        changeStage("../view/main.fxml", "Pantalla Principal", 370, 442);
    }

}
