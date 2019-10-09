package application;

import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Stage curentStage;

    @FXML
    private TextField password;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextField username;

    @FXML
    private Button btnLogin;

    @FXML
    private ComboBox<String> roleuser;

    ResultSet rsLogin = null;

    void setValueCOmboBox(){
        ObservableList newdata = FXCollections.observableArrayList("Admin", "Client");
        roleuser.setItems(newdata);
        roleuser.getSelectionModel().select(0);
    }

    public void loginAction(ActionEvent actionEvent) throws SQLException, IOException {

        String pass = password.getText();
        String uname = username.getText();
        int role = 0;
        if(roleuser.getSelectionModel().getSelectedItem() == "Admin"){
            role = 1;
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
            preparedStatement.setInt(3, role);
            rsLogin = preparedStatement.executeQuery();

            if (rsLogin.next()){
                curentStage = Main.getStage();
                curentStage.setMaximized(true);

                Parent dashboard = FXMLLoader.load(getClass().getResource("views/dashboard.fxml"));
                curentStage.setScene(new Scene(dashboard));
                curentStage.show();

                ShareVariable.setUserid(rsLogin.getString(2));
                ShareVariable.setUserRole(role);

            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login gagal");
                alert.setHeaderText("username atau password anda salah");
                alert.showAndWait();
            }

        }catch (Exception e ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Look, an Exception Dialog");
            alert.setContentText("Could not find connection");

            Exception ex = e;

// Create expandable Exception.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setValueCOmboBox();
    }
}
