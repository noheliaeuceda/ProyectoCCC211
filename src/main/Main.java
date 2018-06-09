package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.classes.FileManager;

public class Main extends Application {

    private static Stage primaryStage;
    private static FileManager fileManager;

    @Override
    public void start(Stage pStage) throws Exception {
        setPrimaryStage(pStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Main screen");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    public static void setFileManager(FileManager fileManager) {
        Main.fileManager = fileManager;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
