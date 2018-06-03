package fx;

import classes.Field;
import classes.LongLengthException;
import classes.Record;
import classes.FileManager;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

// TODO poner un comboox en los tipo de datos

public class guiController implements Initializable {

    private ArrayList<Field> metaFields;
    private ArrayList<Field> tempFields;
    private boolean hasPK;
    private int metaFieldsNum;
    private int bufferedFields;
    private FileManager fileManager;

    public VBox newFieldsVBox;
    public VBox addFieldsVBox;
    public ComboBox fieldsCombo;
    public ListView listMain;
    public ListView listBuffer;
    public TextField fieldText;
    public TextField databaseText;
    public TextField sizeText;
    public TextField typeText;
    public TextField nameText;
    public Label newFieldLabel;
    public Label addFieldLabel;
    public RadioButton pkYesRadio;
    public RadioButton pkNoRadio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String lastOpened = "db";
        try {
            RandomAccessFile raFile = new RandomAccessFile("lastOpened.txt", "r");
            lastOpened = raFile.readUTF();
            raFile.close();
        } catch (Exception e) {
            System.out.println("Error! " + e.getMessage());
        }
        fileManager = new FileManager(lastOpened, listMain);
        tempFields = new ArrayList<>();
        bufferedFields = 0;
        fieldsCombo.getItems().add(2);
        fieldsCombo.getItems().add(3);
        fieldsCombo.getItems().add(4);
        fieldsCombo.getItems().add(5);
        if (!fileManager.hasMetadata()) {
            addFieldsVBox.setVisible(false);
        } else{
            addFieldLabel.setText("Field " + fileManager.at(0).name + " content:");
        }
        newFieldsVBox.setVisible(false);
    }

    public void buttonAddClicked() {
        Field tField;
        if (!fieldText.getText().equals("")) {
            try {
                tField = new Field(fileManager.at(bufferedFields), fieldText.getText());
                listBuffer.getItems().add(tField);
                tempFields.add(tField);
                addFieldLabel.setText("Field " + fileManager.at(bufferedFields).name + " content:");
                bufferedFields++;
                clearFields();

                if (bufferedFields >= fileManager.getFieldCount()) {
                    fileManager.add(tempFields);
                    listBuffer.getItems().clear();
                    bufferedFields = 0;
                    tempFields = new ArrayList<>();
                }
            } catch (LongLengthException e) {
                System.out.println("Error!" + e.getMessage());
            }
        }
    }

    public void buttonDeleteClicked(){
        int pos = listMain.getSelectionModel().getSelectedIndex();
        Record selectedRecord = (Record) listMain.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            fileManager.remove(pos);
            listMain.getItems().set(pos, null);
        }
    }

    public void buttonPrintAListClicked() {
        fileManager.printAvailList();
    }

    public void buttonMetadataClicked(){
        if (fieldsCombo.getSelectionModel().getSelectedItem() != null && !databaseText.getText().equals("")) {
            try {
                RandomAccessFile raFile = new RandomAccessFile("lastOpened.txt", "rw");
                raFile.writeUTF(databaseText.getText());
                raFile.close();
            } catch (Exception e) {
                System.out.println("Error! " + e.getMessage());
            }

            fileManager = new FileManager(databaseText.getText(), listMain);
            metaFieldsNum = (int) fieldsCombo.getSelectionModel().getSelectedItem();
            newFieldsVBox.setVisible(true);
            metaFields = new ArrayList<>();
            tempFields = new ArrayList<>();
            newFieldLabel.setText("Add field #" + (metaFields.size() + 1) + ":");
            listMain.getItems().clear();
            clearFields();
        }
    }

    public void buttonAddFieldClicked(){
        boolean error = false;
        int tSize = 0;
        String tName, tType;
        boolean tPrimaryKey;

        try {
            tSize = Integer.parseInt(sizeText.getText());
        } catch (NumberFormatException e){
            error = true;
        }

        tName = nameText.getText();
        tType = typeText.getText();
        tPrimaryKey = pkYesRadio.isSelected();

        if (tPrimaryKey && hasPK)
            error = true;
        if (tType.equals("") || tName.equals(""))
            error = true;

        if (!error) {
            metaFields.add(new Field(tSize, tType, tName, tPrimaryKey));
            newFieldLabel.setText("Add field #" + (metaFields.size() + 1) + ":");
            if (pkYesRadio.isSelected())
                hasPK = true;
            clearFields();

            if (metaFields.size() >= metaFieldsNum) {
                // TODO verificar que hay al menos una llave primaria antes de continuar
                tempFields = new ArrayList<>();
                bufferedFields = 0;
                newFieldsVBox.setVisible(false);
                addFieldsVBox.setVisible(true);
                fileManager.changeMetadata(metaFields, metaFieldsNum);
                addFieldLabel.setText("Field " + fileManager.at(0).name + " content:");
            }
        }
    }

    public void close() {
        // closing business
    }

    private void clearFields(){
        databaseText.clear();
        sizeText.clear();
        typeText.clear();
        nameText.clear();
        fieldText.clear();
        pkNoRadio.setSelected(true);
    }

}
