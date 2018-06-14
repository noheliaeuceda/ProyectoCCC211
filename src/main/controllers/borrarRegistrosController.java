package main.controllers;

import javafx.scene.control.TextField;
import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class borrarRegistrosController extends mainController {

    public TextField txtCriteria;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
    }

    public void eliminarPressed() {
        Object[] result = fileManager.search(txtCriteria.getText().trim());
        if (result == null) {
            showWarning("No existe ningun registro con esa llave primaria!");
        } else {
            fileManager.remove((int)result[0]);
            showSuccess("Operacion realizada con exito!");
        }
        txtCriteria.setText("");
    }
}
