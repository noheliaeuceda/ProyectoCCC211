package main.controllers;

import javafx.scene.control.ListView;
import main.Main;
import main.classes.Field;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mostrarCamposController extends mainController {

    public ListView listShowFields;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();

        for (Field f : fileManager.getMetadata().getFieldsData()) {
            listShowFields.getItems().add(f);
        }

        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
    }

    public void returnPressed () throws IOException {
        changeStage("../view/main.fxml", "Pantalla Principal", 370, 442);
    }
}
