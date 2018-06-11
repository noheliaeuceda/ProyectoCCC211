package main.controllers;

import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.Main;
import main.classes.Field;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class modificarCamposController extends mainController {

    public VBox vboxModify;
    public ListView listShowFields;
    public TextField txtName;
    public TextField txtSize;
    public RadioButton rbTypeString;
    public RadioButton rbTypeInt;
    public RadioButton rbTypeFloat;
    public RadioButton rbYesPk;
    public RadioButton rbNotPk;

    private int modifyPos = -1;
    private boolean modifyingPK = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = Main.getFileManager();
        statusBarLabel.setText("Archivo abierto: " + fileManager.getFilename());
        for (Field f : fileManager.getMetadata().getFieldsData())
            listShowFields.getItems().add(f);
        vboxModify.setDisable(true);
    }

    public void selectPressed() {
        int pos = listShowFields.getSelectionModel().getSelectedIndex();
        Field selectedField = (Field) listShowFields.getSelectionModel().getSelectedItem();
        if (selectedField != null) {
            modifyPos = pos;
            modifyingPK = selectedField.primaryKey;

            txtName.setText(selectedField.name);
            txtSize.setText(Integer.toString(selectedField.size));
            rbYesPk.setSelected(selectedField.primaryKey);

            if (selectedField.type.equals("float"))
                rbTypeFloat.setSelected(true);
            else if (selectedField.type.equals("int"))
                rbTypeInt.setSelected(true);
            else
                rbTypeString.setSelected(true);

            vboxModify.setDisable(false);
        }
    }

    public void modificarPressed() {
        int size;
        boolean primaryKey;
        String name, type;

        try {
            size = Integer.valueOf(txtSize.getText());
            name = txtName.getText();
            primaryKey = rbYesPk.isSelected();

            if (rbTypeFloat.isSelected())
                type = "float";
            else if (rbTypeInt.isSelected())
                type = "int";
            else
                type = "string";

            if (fileManager.getMetadata().hasPK() && primaryKey && !modifyingPK) {
                showWarning("Ya existe una llave primaria!");
            } else {
                Field t = new Field(size, type, name, primaryKey);
                fileManager.getMetadata().setField(modifyPos, t);
                listShowFields.getItems().set(modifyPos, t);
                showSuccess("Campo modificado con exito!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error!" + e.getMessage());
        }
        vboxModify.setDisable(true);
        txtName.setText("");
        txtSize.setText("");
        rbTypeString.setSelected(true);
        rbNotPk.setSelected(true);
    }

    public void returnPressed () throws IOException {
        changeStage("../view/main.fxml", "Pantalla Principal", 370, 442);
    }

}
