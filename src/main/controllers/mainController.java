package main.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
            Main.setFileManager(new FileManager(new File(
                    System.getProperty("user.dir") + "/files/" + result.get() + ".ed2")));
            changeStage("../view/main.fxml", "Pantalla Principal", 370, 442);
        }
    }

    public void menuArchivoCargar() throws IOException {
        File opened = openFile();
        if (opened != null) {
            Main.setFileManager(new FileManager(opened));
            Main.getFileManager().loadTree();
            showSuccess("Archivo abierto con exito!");
            changeStage("../view/main.fxml", "Pantalla Principal", 370, 442);
        }
    }

    public void menuArchivoSalvar() {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else {
            fileManager.save();
            showSuccess("Archivo guardado con exito!");
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
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else if (fileManager.hasRecords()) {
            showWarning("No puede modificar los campos si ya existen registros!");
        } else {
            changeStage("../view/agregarCampos.fxml", "Agregar Campos", 370, 482);
        }
    }

    public void menuCamposMostrar() throws IOException {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else {
            changeStage("../view/mostrarCampos.fxml", "Mostrar Campos", 370, 442);
        }
    }

    public void menuCamposModificar() throws IOException {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else if (fileManager.hasRecords()) {
            showWarning("No puede modificar los campos si ya existen registros!");
        } else {
            changeStage("../view/modificarCampos.fxml", "Modificar Campos", 800, 482);
        }
    }

    public void menuCamposBorrar() throws IOException {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else if (fileManager.hasRecords()) {
            showWarning("No puede modificar los campos si ya existen registros!");
        } else {
            changeStage("../view/borrarCampos.fxml", "Borrar Campos", 370, 442);
        }
    }

    public void menuRegistrosCargar() throws IOException {
        changeStage("../view/cargarRegistros.fxml", "Cargar Registros", 370, 442);
    }

    public void menuRegistrosAgregar() throws IOException {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else if (!fileManager.getMetadata().hasPK()) {
            showWarning("No ha ingresado un campo de llave primaria!");
        } else {
            changeStage("../view/agregarRegistros.fxml", "Agregar Registros", 370, 482);
        }
    }

    public void menuRegistrosModificar() throws IOException {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else if (!fileManager.getMetadata().hasPK()) {
            showWarning("No ha ingresado un campo de llave primaria!");
        } else if (fileManager.getRecords().size() > 0) {
            if (showConfirmation("Para realizar esta operacion tiene que guardar los cambios en el archivo, desea continuar?")) {
                fileManager.save();
                changeStage("../view/modificarRegistros.fxml", "Modificar Registros", 370, 482);
            }
        } else {
            changeStage("../view/modificarRegistros.fxml", "Modificar Registros", 370, 482);
        }
    }

    public void menuRegistrosBorrar() throws IOException {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else if (!fileManager.getMetadata().hasPK()) {
            showWarning("No ha ingresado un campo de llave primaria!");
        } else if (fileManager.getRecords().size() > 0) {
            if (showConfirmation("Para realizar esta operacion tiene que guardar los cambios en el archivo, desea continuar?")) {
                fileManager.save();
                changeStage("../view/borrarRegistros.fxml", "Borrar Registros", 370, 482);
            }
        } else {
            changeStage("../view/borrarRegistros.fxml", "Borrar Registros", 370, 482);
        }
    }

    public void menuRegistrosMostrar() throws IOException {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else if (!fileManager.getMetadata().hasPK()) {
            showWarning("No ha ingresado un campo de llave primaria!");
        } else if (fileManager.getRecords().size() > 0) {
            if (showConfirmation("Para realizar esta operacion tiene que guardar los cambios en el archivo, desea continuar?")) {
                fileManager.save();
                changeStage("../view/mostrarRegistros.fxml", "Mostrar Registros", 370, 482);
            }
        } else {
            changeStage("../view/mostrarRegistros.fxml", "Mostrar Registros", 370, 482);
        }
    }

    public void menuRegistrosCruzar() throws IOException {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else if (!fileManager.getMetadata().hasPK()) {
            showWarning("No ha ingresado un campo de llave primaria!");
        } else if (fileManager.getRecords().size() > 0) {
            if (showConfirmation("Para realizar esta operacion tiene que guardar los cambios en el archivo, desea continuar?")) {
                fileManager.save();
                changeStage("../view/cruzarArchivos.fxml", "Cruzar Registros", 370, 482);
            }
        } else {
            changeStage("../view/cruzarArchivos.fxml", "Cruzar Registros", 370, 482);
        }
    }

    public void menuRegistrosBuscar() throws IOException {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else if (!fileManager.getMetadata().hasPK()) {
            showWarning("No ha ingresado un campo de llave primaria!");
        } else if (fileManager.getRecords().size() > 0) {
            if (showConfirmation("Para realizar esta operacion tiene que guardar los cambios en el archivo, desea continuar?")) {
                fileManager.save();
                changeStage("../view/buscarRegistros.fxml", "Buscar Registros", 370, 482);
            }
        } else {
            changeStage("../view/buscarRegistros.fxml", "Buscar Registros", 370, 482);
        }
    }

    public void menuIndicesCrear() {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else {
            fileManager.loadTree();
            showSuccess("Se ha indexado en el arbol!");
        }
    }

    public void menuIndicesReindexar() {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else {
            fileManager.loadTree();
            showSuccess("Se ha indexado en el arbol!");
        }
    }

    public void menuEstandarizacionExcel() {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else if (fileManager.getRecords().size() > 0) {
            if (showConfirmation("Para realizar esta operacion tiene que guardar los cambios en el archivo, desea continuar?")) {
                fileManager.save();
                if (fileManager.exportToCSV())
                    showSuccess("Archivo de Excel guardado con exito!");
                else
                    showWarning("Hubo un error al crear el archivo!");
            }
        } else {
            if (fileManager.exportToCSV())
                showSuccess("Archivo de Excel guardado con exito!");
            else
                showWarning("Hubo un error al crear el archivo!");
        }
    }

    public void menuEstandarizacionXML() {
        if (fileManager == null) {
            showWarning("No tiene ningun archivo abierto!");
        } else if (fileManager.getRecords().size() > 0) {
            if (showConfirmation("Para realizar esta operacion tiene que guardar los cambios en el archivo, desea continuar?")) {
                fileManager.save();
                if (fileManager.exportToXML())
                    showSuccess("Archivo de XML guardado con exito!");
                else
                    showWarning("Hubo un error al crear el archivo!");
            }
        } else {
            if (fileManager.exportToXML())
                showSuccess("Archivo de XML guardado con exito!");
            else
                showWarning("Hubo un error al crear el archivo!");
        }
    }

    public void close() {
        fileManager = Main.getFileManager();
        if (fileManager != null && fileManager.getRecords().size() > 0
                && showConfirmation("Desea guardar los cambios en el archivo?")) {
            fileManager.save();
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

    public static boolean showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cuadro de Confirmacion");
        alert.setHeaderText("Por favor confirme la operacion");
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public void changeStage(String fxml, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage primaryStage = Main.getPrimaryStage();
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();
    }

    public File openFile() {
        // file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Cargar Archivo");

        // filtros del filechooser para solo mostrar archivos *.ed2
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "Archivo Estructuras de Datos 2 (*.ed2)", "*.ed2");
        fileChooser.getExtensionFilters().add(filter);

        // directorio inicial, lo seteamos al directorio del proyecto
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        return fileChooser.showOpenDialog(Main.getPrimaryStage());
    }

}
