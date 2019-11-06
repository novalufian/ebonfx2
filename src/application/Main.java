package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends Application {

    private static Stage currentStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));

        currentStage = primaryStage;

        currentStage.setTitle("Hello World");
        currentStage.setScene(new Scene(root, 600, 400));
        currentStage.show();
        currentStage.setMaximized(true);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
    }

    public static Stage getStage(){
        return currentStage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
