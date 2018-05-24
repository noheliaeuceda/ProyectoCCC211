package fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setOnHidden(e -> close(controller));

        primaryStage.setTitle("Registry");
        primaryStage.setScene(new Scene(root, 800, 524));
        primaryStage.show();
    }

    public void close(Controller controller){
        controller.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
