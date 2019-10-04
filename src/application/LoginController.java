package application;

import application.connectifity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    private Stage curentStage;

    @FXML
    private TextField password;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextField username;

    @FXML
    private Button btnLogin;

    ResultSet rsLogin = null;

    public void loginAction(ActionEvent actionEvent) throws SQLException, IOException {
        String pass = password.getText();
        String uname = username.getText();
        System.out.println(uname + " dan "+ pass);


        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        System.out.println(connection);

//        String sql = "INSERT INTO table1 VALUES('"+uname +"')";

        try {
            String sqlLogin = "SELECT * FROM login WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlLogin);
            preparedStatement.setString(1, uname);
            preparedStatement.setString(2, pass);
            rsLogin = preparedStatement.executeQuery();

            if (rsLogin.next()){
                curentStage = Main.getStage();
                curentStage.setMaximized(true);

                Parent dashboard = FXMLLoader.load(getClass().getResource("views/dashboard.fxml"));
                curentStage.setScene(new Scene(dashboard));
                curentStage.show();

                ShareVariable.setUserid(rsLogin.getString(2));

            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login gagal");
                alert.setHeaderText("username atau password anda salah");
                alert.showAndWait();
            }

        }catch (Exception e ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login gagal");
            alert.setHeaderText("Tolong periksa koneksi anda");
            alert.showAndWait();
            e.printStackTrace();
        }





    }


}
