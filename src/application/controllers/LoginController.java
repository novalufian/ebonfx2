package application.controllers;

import application.Main;
import application.connectifity.Template_error;
import application.models.ShareVariable;
import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    Template_error error_template = new Template_error();

    private Stage curentStage;

    @FXML
    private TextField password;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextField username;

    @FXML
    private ComboBox<String> userRole;

    @FXML
    private Button btnLogin;

    ResultSet rsLogin = null;

    @FXML
    private Button config;

    @FXML
    void doOpenConfig(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/views/config.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent, 500, 400));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
            error_template.error(e);
        }
    }

    public void loginAction(ActionEvent actionEvent) throws SQLException, IOException {
        String pass = password.getText();
        String uname = username.getText();
        Boolean role = false;
        String myhome = "/application/views/home-client.fxml";
        if(userRole.getSelectionModel().getSelectedItem() == "admin"){
            myhome = "/application/views/home.fxml";
            role = true;
        }
        System.out.println(uname + " dan "+ pass);


        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        System.out.println(connection);

//        String sql = "INSERT INTO table1 VALUES('"+uname +"')";

        try {
            String sqlLogin = "SELECT * FROM login WHERE username = ? AND password = ? AND user_login_role = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlLogin);
            preparedStatement.setString(1, uname);
            preparedStatement.setString(2, pass);
            preparedStatement.setBoolean(3, role);
            rsLogin = preparedStatement.executeQuery();

            if (rsLogin.next()){
                curentStage = Main.getStage();
                Parent dashboard = FXMLLoader.load(getClass().getResource(myhome));
                curentStage.setScene(new Scene(dashboard, 1200, 700));
                curentStage.setMaximized(true);
                curentStage.show();

                ShareVariable.setUserid(rsLogin.getString(2));
                ShareVariable.setSharehome(myhome);
                ShareVariable.setUserRole(userRole.getSelectionModel().getSelectedItem());
                ShareVariable.setUsername(uname);

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
            error_template.error(e);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList data = FXCollections.observableArrayList("user", "admin");
        userRole.setItems(data);

        rootAnchorPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        rootAnchorPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
    }
}
