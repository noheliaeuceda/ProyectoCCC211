package main.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import main.classes.Field;
import main.classes.FileManager;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import main.Main;
import main.classes.Record;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

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
            Main.setFileManager(new FileManager(new File(result.get() + ".ed2")));
            changeStage("../view/main.fxml", "Pantalla Principal", 370, 442);
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
        }
    }

    public void menuArchivoSalvar() {
        if (fileManager != null) {
            fileManager.save();
            showSuccess("Archivo guardado con exito!");
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
            changeStage("../view/agregarCampos.fxml", "Agregar Campos", 370, 482);
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuCamposListar() throws IOException {
        if (fileManager != null) {
            changeStage("../view/mostrarCampos.fxml", "Agregar Campos", 370, 442);
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuCamposModificar() throws IOException {
        if (fileManager != null) {
            changeStage("../view/modificarCampos.fxml", "Modificar Campos", 800, 482);
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuCamposBorrar() throws IOException {
        if (fileManager != null) {
            changeStage("../view/borrarCampos.fxml", "Borrar Campos", 370, 442);
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuRegistrosCargar() throws IOException {
        changeStage("../view/cargarRegistros.fxml", "Cargar Registros", 370, 442);
    }

    public void menuRegistrosAgregar() throws IOException {
        if (fileManager != null) {
            changeStage("../view/agregarRegistros.fxml", "Agregar Registros", 370, 482);
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuRegistrosModificar() {
        if (fileManager != null) {

        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuRegistrosBorrar() throws IOException {
        if (fileManager != null) {
            changeStage("../view/borrarRegistros.fxml", "Borrar Registros", 370, 482);
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuRegistrosMostrar() throws IOException {
        if (fileManager != null) {
            changeStage("../view/mostrarRegistros.fxml", "Mostrar Registros", 370, 482);
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuRegistrosCruzar() {
        if (fileManager != null) {

        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuRegistrosBuscar() {
        if (fileManager != null) {

        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuIndicesCrear() {
        if (fileManager != null) {

        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuIndicesReindexar() {
        if (fileManager != null) {

        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuEstandarizacionExcel() {
        if (fileManager != null) {
            if (fileManager.exportToCSV())
                showSuccess("Archivo de Excel guardado con exito!");
            else
                showWarning("Hubo un error al crear el archivo!");
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public void menuEstandarizacionXML() {
        if (fileManager != null) {
//            generarPrueba();
        } else {
            showWarning("No tiene ningun archivo abierto!");
        }
    }

    public static void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alerta");
        alert.setHeaderText("Error al realizar la operacion");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alerta");
        alert.setHeaderText("Operacion exitosa!");
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

    public void generarPrueba() {
        try {
            Record tempRecord;
            int numero = 11800000;
            String nombre = "Nombre #";
            String apellido = "Apellido #";
            int clases = 0;
            float pagos = 0.15f;
            for (int i = 0; i < 10000; i++) {
                tempRecord = new Record();
                tempRecord.add(new Field(fileManager.getMetadata().getFieldsData().get(0), Integer.toString(numero + i)));
                tempRecord.add(new Field(fileManager.getMetadata().getFieldsData().get(1), nombre));
                tempRecord.add(new Field(fileManager.getMetadata().getFieldsData().get(2), apellido + i));
                tempRecord.add(new Field(fileManager.getMetadata().getFieldsData().get(3), Integer.toString(clases)));
                tempRecord.add(new Field(fileManager.getMetadata().getFieldsData().get(4), Float.toString(pagos)));
                if (clases > 75)
                    clases = 0;
                if (pagos > 500000)
                    pagos = 0.13f;
                clases++;
                pagos += 1234.37;
                fileManager.addRecord(tempRecord);
            }
        } catch (Exception e) { }
    }

}
