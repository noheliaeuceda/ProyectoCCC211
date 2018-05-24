package fx;

import classes.Record;
import classes.Registry;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Registry registry;
    public TextField textName;
    public TextField textSurname;
    public TextField textId;
    public TextField textAddress;
    public TextField textPhone;
    public TextField textGender;
    public TextField textRace;
    public ListView listMain;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registry = new Registry("db", listMain);
    }

    public void buttonAddClicked(){
        registry.add(textId.getText(),
                textName.getText(),
                textSurname.getText(),
                textAddress.getText(),
                textPhone.getText(),
                textGender.getText(),
                textRace.getText());
        clearFields();
    }

    public void buttonDeleteClicked(){
        int pos = listMain.getSelectionModel().getSelectedIndex();
        Record selectedRecord = (Record) listMain.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            registry.remove(selectedRecord);
            listMain.getItems().set(pos, null);
        }
    }

    public void buttonPrintAListClicked() {
        registry.printAvailList();
    }

    public void close() {
        registry.update();
    }

    private void clearFields(){
        textName.clear();
        textSurname.clear();
        textId.clear();
        textAddress.clear();
        textPhone.clear();
        textGender.clear();
        textRace.clear();
    }

}
