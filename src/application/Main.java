package application;

import application.connectifity.ConnectionClass;
import javafx.application.Application;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Main extends Application {

    private static Stage currentStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));

        currentStage = primaryStage;

        currentStage.setTitle("Hello World");
        currentStage.setScene(new Scene(root, 600, 400));
        currentStage.show();
        currentStage.setOnCloseRequest(event -> {
            System.out.println("close app");
            System.out.println(ShareVariable.getCartBookingNapi().size());
            clearCart();
        });
//        currentStage.setMaximized(true);
    }

    public static Stage getStage(){
        return currentStage;
    }


    public static void main(String[] args) {
        launch(args);
    }

    void clearCart(){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();
        ObservableList<ModelBlokLapasNapi> napi = ShareVariable.getCartBookingNapi();
        napi.forEach(item ->{
            try {
                String sql = "UPDATE data_napi SET napi_booked = ?, napi_booked_by = ? WHERE napi_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setBoolean(1, false);
                statement.setString(2, "");
                statement.setString(3, item.getId());
                 int rs = statement.executeUpdate();
                 if (rs > 0){

                 }
            }catch (Exception e){}


        });

    }
}
