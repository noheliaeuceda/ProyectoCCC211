package main.controllers;

import javafx.scene.control.ComboBox;
import main.Main;
import main.classes.FileManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class cargarRegistrosController extends mainController {

    public ComboBox comboTestFiles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        if (fileManager == null)
            statusBarLabel.setText("No hay archivo abierto.");
        else
            statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
        comboTestFiles.getItems().add("telefonos.ed2");
        comboTestFiles.getItems().add("estudiantes.ed2");
    }

    public void abrirPressed() throws IOException {
        String filename = (String) comboTestFiles.getSelectionModel().getSelectedItem();
        if (filename != null) {
            Main.setFileManager(new FileManager(new File("tests/" + filename)));
            Main.getFileManager().loadTree();
            changeStage("../view/main.fxml", "Pantalla Principal", 370, 442);
            showSuccess("Archivo abierto con exito!");
        } else {
            showWarning("Por favor seleccione un archivo!");
        }
    }
}
