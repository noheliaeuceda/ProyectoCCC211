package fx;

import classes.LongLengthException;
import classes.Person;
import classes.Registry;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

//-  No es necesario que muestre la construccion del avail list (5% extra de la nota de tareas si lo hace)

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
    public ListView listAvail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registry = new Registry("db.txt", listMain, listAvail);
    }

    public void buttonAddClicked(){
        try {
            Person tempPerson = new Person(
                    textName.getText(),
                    textSurname.getText(),
                    textId.getText(),
                    textAddress.getText(),
                    textPhone.getText(),
                    textGender.getText(),
                    textRace.getText()
            );
            clearFields();
            registry.addPerson(tempPerson);
        } catch (LongLengthException e){
            System.out.println("Error adding element " + e.getMessage());
        }
    }

    public void buttonDeleteClicked(){
        int pos = listMain.getSelectionModel().getSelectedIndex();
        Person selectedPerson = (Person) listMain.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            registry.removePerson(selectedPerson);
            listMain.getItems().set(pos, null);
        }
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
    private void buttonNewFileClicked(){
        
    }

}
