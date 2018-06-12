package main.controllers;

import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class agregarRegistrosController extends mainController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
        // generar el interfaz de todos los campos
    }

    public void agregarPressed() {

    }
}
