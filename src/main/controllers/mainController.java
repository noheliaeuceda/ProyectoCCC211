package main.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import main.classes.FileManager;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import main.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    protected FileManager fileManager;
    public Label statusBarLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        if (fileManager == null)
            statusBarLabel.setText("No hay archivo abierto.");
        else
            statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
    }

    public void menuArchivoNuevo() throws IOException {
        // crear un archivo en blanco
        TextInputDialog dialog = new TextInputDialog("telefonos");
        dialog.setTitle("Nuevo Archivo");
        dialog.setHeaderText("Ingrese el nombre del nuevo archivo (sin extensiones):");
        dialog.setGraphic(null);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().equals("")) {
            fileManager = new FileManager(new File(result.get() + ".ed2"));
            statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
        }
    }

    public void menuArchivoCargar() throws IOException {
        // file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Cargar Archivo");

        // filtros del filechooser para solo mostrar archivos *.ed2
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "Archivo Estructuras de Datos 2 (*.ed2)", "*.ed2");
        fileChooser.getExtensionFilters().add(filter);

        // directorio inicial, lo seteamos al directorio del proyecto
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File opened = fileChooser.showOpenDialog(Main.getPrimaryStage());

        // cargar el archivo
        if (opened != null) {
            Main.setFileManager(new FileManager(opened));
            changeStage("../view/main.fxml", "Pantalla Principal", 370, 442);
        } else {
            System.out.println("Error ?");
        }
    }

    public void menuArchivoSalvar() {
//      TODO esto
//      Salvará la definición de campos como encabezado del archivo y luego colocará los datos de los registros
        if (fileManager != null) {
            System.out.println("guardar todos los cambios");
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuArchivoCerrar() throws IOException {
        Main.setFileManager(null);
        changeStage("../view/main.fxml", "Pantalla Principal", 370, 442);
    }

    public void menuArchivoSalir() {
        Main.getPrimaryStage().close();
    }

    public void menuCamposAgregar() throws IOException {
        if (fileManager != null) {
            Main.setFileManager(fileManager);
            changeStage("../view/agregarCampos.fxml", "Agregar Campos", 370, 442);
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuCamposListar() throws IOException {
        if (fileManager != null) {
            Main.setFileManager(fileManager);
            changeStage("../view/mostrarCampos.fxml", "Agregar Campos", 370, 442);
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuCamposModificar() {
        if (fileManager != null) {
            // nueva ventana
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuCamposBorrar() throws IOException {
        if (fileManager != null) {
            Main.setFileManager(fileManager);
            changeStage("../view/borrarCampos.fxml", "Borrar Campos", 370, 442);
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuRegistrosCargar() throws IOException {
        menuArchivoCargar();
    }

    public void menuRegistrosIntroducir() {

    }

    public void menuRegistrosModificar() {

    }

    public void menuRegistrosBorrar() {

    }

    public void menuRegistrosListar() {

    }

    public void menuRegistrosCruzar() {

    }

    public void menuRegistrosBuscar() {

    }

    public void menuIndicesCrear() {

    }

    public void menuIndicesReindexar() {

    }

    public void menuEstandarizacionExcel() {

    }

    public void menuEstandarizacionXML() {

    }

    public static void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alerta");
        alert.setHeaderText("Error al realizar la operacion");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void changeStage(String fxml, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage primaryStage = Main.getPrimaryStage();
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();
    }

}
