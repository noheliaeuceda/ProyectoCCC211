package main.controllers;

import javafx.scene.control.ListView;
import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class mostrarRegistrosController extends mainController {

    public ListView listShowRecords;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
        fileManager.loadList(listShowRecords);
    }

}
