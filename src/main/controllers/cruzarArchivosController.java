package main.controllers;

import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class cruzarArchivosController extends  mainController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
    }
}
