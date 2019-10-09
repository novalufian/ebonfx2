package application;

import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;

public class DataUserAdminTambahController implements Initializable {

    public static ConnectionClass connectionClass = new ConnectionClass();
    public static Connection connection = connectionClass.getConnection();

    @FXML
    private TextField pegawai;

    @FXML
    private Button searchButton;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private ComboBox<String> roleUser;

    @FXML
    private Button btnSimpan;

    @FXML
    void doSImpan(ActionEvent event) {
        try{
            Boolean x = true;
            if (roleUser.getSelectionModel().getSelectedItem() == "Client"){
                x = false;
            }

            int rs = 0;

            if(btnSimpan.getText() == "Simpan user"){
                String sql = "INSERT INTO login (login_id, user_id, username, password, user_login_role) " +
                        "VALUES (?,?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, "LGN-"+ System.currentTimeMillis() +new Random().nextInt(99999999));
                statement.setString(2, pegawai.getText());
                statement.setString(3, username.getText());
                statement.setString(4, password.getText());
                statement.setBoolean(5,x);
                rs = statement.executeUpdate();
            }else{
                String sql = "UPDATE login SET user_id = ?, username = ? , password = ? , user_login_role = ? WHERE login_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, pegawai.getText());
                statement.setString(2, username.getText());
                statement.setString(3, password.getText());
                statement.setBoolean(4,x);
                statement.setString(5, ShareVariable.getLoginid());
                rs = statement.executeUpdate();
            }

            System.out.println(rs);

            if (rs > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText("Selamat data sudah berhasil ditambahkan");
                alert.showAndWait();
                reset();
                Stage stage = (Stage) btnSimpan.getScene().getWindow();
                stage.close();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Peringatan");
                alert.setHeaderText("data gagal ditambah");
                alert.showAndWait();
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Koneksi error");
            alert.setHeaderText("periksa koneksi anda ");
            alert.showAndWait();
            e.printStackTrace();
        }

    }

    void reset(){
        ObservableList roleuser = FXCollections.observableArrayList("Admin", "Client");
        roleUser.setItems(roleuser);
        roleUser.getSelectionModel().select(0);

        pegawai.setText("");
        username.setText("");
        password.setText("");

        btnSimpan.setText("Simpan user");


        if (ShareVariable.getLoginid() != null){
//            ModelLoginUser data = ShareVariable.getLoginUserData();
            try{
                String sql = "SELECT * FROM login WHERE login_id = ? ";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, ShareVariable.getLoginid());
                ResultSet rs = statement.executeQuery();
                while (rs.next()){
                    Boolean role = rs.getBoolean("user_login_role");
                    String x = "Admin";
                    if(!role){
                        x = "client";
                    }
                    pegawai.setText(rs.getString("user_id"));
                    username.setText(rs.getString("username"));
                    password.setText(rs.getString("password"));
                    roleUser.getSelectionModel().select(x);
                    btnSimpan.setText("Update user");

                }
            }catch (Exception e){

            }
        }

        try{
            BorderPane curentBorderpane = ShareVariable.getMainDashboardBoderpane();
            Parent stage_area = FXMLLoader.load(getClass().getResource("views/user_admin.fxml"));
            curentBorderpane.setCenter(stage_area);
        }catch(Exception e){

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reset();
    }
}
