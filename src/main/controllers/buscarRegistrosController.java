package main.controllers;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class buscarRegistrosController extends mainController {

    public TextField txtPK;
    public ListView listSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
    }

    public void buscarPressed() {
        Object[] result = fileManager.search(txtPK.getText().trim());
        if (result == null) {
            showWarning("No existe ningun registro con esa llave primaria!");
        } else {
            listSearch.getItems().add("Posicion: " + result[0] + ", Informacion: " + result[1]);
            showSuccess("Operacion realizada con exito!");
        }
        txtPK.setText("");
    }

    public void limpiarPressed() {
        listSearch.getItems().clear();
    }
}
