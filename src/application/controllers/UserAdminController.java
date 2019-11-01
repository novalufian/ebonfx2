package application.controllers;

import application.Main;
import application.models.ModelLoginUser;
import application.models.ShareVariable;
import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserAdminController implements Initializable {

    private static String curentLocalTab = "client";
    private static ConnectionClass connectionClass = new ConnectionClass();
    private static Connection connection = connectionClass.getConnection();

    @FXML
    private Tab clientTab;

    @FXML
    private TableView<ModelLoginUser> clientTabel;

    @FXML
    private TableColumn<ModelLoginUser, Integer> clientNO;

    @FXML
    private TableColumn<ModelLoginUser, String> clientNIP;

    @FXML
    private TableColumn<ModelLoginUser, String> cleintNama;

    @FXML
    private TableColumn<ModelLoginUser, String> clientUsername;

    @FXML
    private TableColumn<ModelLoginUser, String> clientRole;

    @FXML
    private TableColumn<ModelLoginUser, Button> clinetView;

    @FXML
    private TableColumn<ModelLoginUser, Button> clientUpdate;

    @FXML
    private TableColumn<ModelLoginUser, Button> clientDelete;

    @FXML
    private Tab adminTab;

    @FXML
    private TextField searchUser;

    @FXML
    private Button abtnSearch;

    @FXML
    private Button tambahBaru;

    @FXML
    private AnchorPane mainAnchorpane;

    @FXML
    void chooseAdminTab(ActionEvent event) {
        curentLocalTab = "admin";
    }

    @FXML
    void chooseClientTab(ActionEvent event) {
        curentLocalTab = "client";
    }

    @FXML
    private Button kembali;

    @FXML
    void doKembali(ActionEvent event) {
        try {
            Stage curentStage = Main.getStage();
            Parent dashboard = FXMLLoader.load(getClass().getResource(ShareVariable.getSharehome()));
            curentStage.setScene(new Scene(dashboard));
            curentStage.show();
//            curentStage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doSearchUser(ActionEvent event) {

    }

    @FXML
    void liveSearchUser(KeyEvent event) {

    }
    @FXML
    void doTambahUser(ActionEvent event) {
        try{
            ShareVariable.setLoginid(null);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/views/data_user_admin_tambah.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent, 600, 450));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    void reset(){
        generateDataTable();
        generateTableClient();
    }

    void generateTableClient(){
        clientNO.setCellValueFactory(new PropertyValueFactory<>("no"));
        clientNIP.setCellValueFactory(new PropertyValueFactory<>("nip"));
        cleintNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        clientUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        clientRole.setCellValueFactory(new PropertyValueFactory<>("roleuser"));
        clinetView.setCellValueFactory(new PropertyValueFactory<>("view"));
        clientUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));
        clientDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        createaBtnView("view");
        createaBtnView("update");
        createaBtnView("delete");
    }

    void generateDataTable(){
        ObservableList<ModelLoginUser> dataloginClient = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM login " +
                    "LEFT JOIN data_pegawai ON data_pegawai.user_id = login.user_id " +
                    "WHERE login.published = 1 ";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            int number = 1;
            while (rs.next()){
                String role = "Admin";
                if (!rs.getBoolean("user_login_role")){
                    role = "Client";
                }
                dataloginClient.add(new ModelLoginUser(
                        number++,
                        rs.getString("login_id"),
                        rs.getString("nip_pegawai"),
                        rs.getString("nama_pegawai"),
                        rs.getString("username"),
                        rs.getString("password"),
                        role,
                        new Button("view"),
                        new Button("update"),
                        new Button("delete")
                ));

            }

            clientTabel.setItems(dataloginClient);


            FilteredList<ModelLoginUser> filteredList = new FilteredList<>(dataloginClient, b -> true);
            searchUser.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(model_barang -> {
                    if (newValue == null || newValue.isEmpty()){
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (model_barang.getNip().indexOf(lowerCaseFilter) != -1){
                        return true;
                    }else if(model_barang.getNama().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                        return true;
                    }else if(model_barang.getUsername().toLowerCase().indexOf(lowerCaseFilter) != -1){
                        return true;
                    }else{
                        return false;
                    }
                });
            });

            SortedList<ModelLoginUser> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(clientTabel.comparatorProperty());

            clientTabel.setItems(sortedList);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void createaBtnView(String typebtn){
        Callback<TableColumn<ModelLoginUser, Button>, TableCell<ModelLoginUser, Button>> cellFactory =
                new Callback<TableColumn<ModelLoginUser, Button>, TableCell<ModelLoginUser, Button>>() {

                    @Override
                    public TableCell<ModelLoginUser, Button> call(TableColumn<ModelLoginUser, Button> param) {

                        final TableCell<ModelLoginUser, Button> cell = new TableCell<ModelLoginUser, Button>(){

                            final  Button btn = new Button(typebtn);

                            @Override
                            public void updateItem(Button item, boolean empty){
                                super.updateItem(item, empty);

                                if (empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    btn.setOnAction(event -> {
                                        ModelLoginUser userlogin = getTableView().getItems().get(getIndex());
                                        System.out.println(userlogin.getId());
                                        initBtnAction(typebtn, userlogin.getId(), userlogin);
                                        if (typebtn == "update"){}
                                    });

                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        switch (typebtn){
            case "update" :
                clinetView.setCellFactory(cellFactory);
                break;
            case "view" :
                clientUpdate.setCellFactory(cellFactory);
                break;
            case "delete":
                clientDelete.setCellFactory(cellFactory);
                break;
        }

    }

    void initBtnAction(String typebtn, String id, ModelLoginUser obj){
        switch (typebtn) {
            case "update":
                acationUpdate(id, obj);
                break;
            case "view":
                actionView(id, obj);
                break;
            case "delete":
                actionDelete(id);
                break;
        }
    }

    void actionView(String id, ModelLoginUser obj){}

    void acationUpdate(String id, ModelLoginUser obj){

        try{
            ShareVariable.setLoginid(id);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/data_user_admin_tambah.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent, 600, 500));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void actionDelete(String id){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText("Hapus data user login");
        alert.setContentText("Apakah anda yakin menghapus data ini ?");

        Optional<ButtonType> result = alert.showAndWait();
        System.out.println("from delete "+ id);
        if (result.get() == ButtonType.OK){
            try{
                String sql = "UPDATE login SET published = 0 WHERE login_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, id);
                int rs = statement.executeUpdate();
                if (rs > 0){
                    reset();
                    alert.close();
                    Alert myalert = new Alert(Alert.AlertType.INFORMATION);
                    myalert.setTitle("Sukses");
                    myalert.setContentText("data berhasil dihapus");
                    myalert.show();
                }else{
                    alert.close();
                }
            }catch (Exception e){
                Alert myalert = new Alert(Alert.AlertType.ERROR);
                myalert.setTitle("connection error");
                myalert.setContentText("coba periksa koneksi anda");
                myalert.show();
                e.printStackTrace();
            }
        }else{
            alert.close();
        }


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reset();

        mainAnchorpane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        mainAnchorpane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
    }
}
