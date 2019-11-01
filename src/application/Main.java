package application;

import application.connectifity.ConnectionClass;
import application.connectifity.Template_error;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;

public class Main extends Application {

    private static Stage currentStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));

        currentStage = primaryStage;

        currentStage.setTitle("Hello World");
        currentStage.setScene(new Scene(root, 600, 400));
        currentStage.show();
    }

    public static Stage getStage(){
        return currentStage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
