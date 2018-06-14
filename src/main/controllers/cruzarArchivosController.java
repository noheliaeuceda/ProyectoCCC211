package main.controllers;

import javafx.scene.control.TextField;
import main.Main;
import main.classes.FileManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class cruzarArchivosController extends  mainController {

    public TextField txtPath;
    public TextField txtFilename;
    private FileManager otherFM;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
        otherFM = null;
        txtPath.setDisable(true);
    }

    public void abrirPressed() throws IOException {
        File tempFile = openFile();
        if (tempFile != null) {
            otherFM = new FileManager(tempFile);
            if (!otherFM.getMetadata().exists()) {
                showWarning("Este archivo no tiene metadata!");
                otherFM = null;
            } else if (!fileManager.getMetadata().equals(otherFM.getMetadata())) {
                showWarning("Este archivo no tiene la misma metadata que  " + fileManager.getFilename() + "!");
                otherFM = null;
            } else if (tempFile.equals(fileManager.getFile())) {
                showWarning("No puede abrir el mismo archivo dos veces!");
                otherFM = null;
            } else {
                txtPath.setText(tempFile.getCanonicalPath());
            }
        }
    }

    public void mezclarPressed() throws IOException {
        if (otherFM == null) {
            showWarning("Primero debe abrir un archivo secundario!");
        } else if (txtFilename.getText().equals("")) {
            showWarning("Por favor llene todos los campos!");
        } else {
            FileManager result = new FileManager(
                    new File(System.getProperty("user.dir") + "/files/" + txtFilename.getText() + ".ed2"));
            if (result.hasRecords()) {
                showWarning("Ya existe un archivo con el nombre que ingreso!");
            } else {
                result.merge(fileManager, otherFM);
                Main.setFileManager(result);
                changeStage("../view/main.fxml", "Pantalla Principal", 370, 442);
            }
        }
        otherFM = null;
        txtPath.setText("");
        txtFilename.setText("");
    }
}
