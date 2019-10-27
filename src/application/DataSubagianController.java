package application;

import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DataSubagianController implements Initializable {

    private static String bagianid = null;

    @FXML
    private TableView<ModelDataSubagian> tabelSubagian;

    @FXML
    private TableColumn<ModelDataSubagian, Integer> no;

    @FXML
    private TableColumn<ModelDataSubagian, String> nama;

    @FXML
    private TableColumn<ModelDataSubagian, String> warna;

    @FXML
    private TableColumn<ModelDataSubagian, Button> action;

    @FXML
    private TextField namaSubagian;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button simpan;

    @FXML
    private Button reset;

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
    void pilihWarna(ActionEvent event) {

    }

    @FXML
    void resetBtn(ActionEvent event) {
        resetAction();
    }

    void resetAction() {
        simpan.setText("Tambah data baru");
        namaSubagian.setText("");
        bagianid = null;
        dataTable();
        generateViewTable();

    }

    @FXML
    void simpan(ActionEvent event) {
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try{
            int rs  = 0;
            if (simpan.getText() != "Ubah data"){
                String sql = "INSERT INTO master_subagian (nama, bagian_warna) VALUES (?,?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, namaSubagian.getText());
                statement.setString(2, Integer.toHexString(colorPicker.getValue().hashCode()).toString());
                rs = statement.executeUpdate();
            }else{
                String sql = "UPDATE master_subagian SET nama = ? , bagian_warna = ? WHERE subagian_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, namaSubagian.getText());
                statement.setString(2,Integer.toHexString(colorPicker.getValue().hashCode()).toString());
                statement.setString(3, bagianid);
                rs = statement.executeUpdate();
            }

            System.out.println(rs);
            if (rs > 0 ){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("selamat data anda berhasil disimpan");
                alert.showAndWait();
                resetAction();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("data gagal disimpan");
                alert.showAndWait();
            }

        }catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("data agagal disimpan, terjadi kesalah koneksi. coba cek koneksi anda");
        }

    }

    void initTable(){
        initColumn();
    }

    void initColumn(){
        no.setCellValueFactory(new PropertyValueFactory<>("no"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        warna.setCellValueFactory(new PropertyValueFactory<>("warna"));
        action.setCellValueFactory(new PropertyValueFactory<ModelDataSubagian, Button>("view"));
        createaBtnView("update");
    }

    void generateViewTable(){
        initTable();
        dataTable();
    }

    void dataTable(){
        ObservableList<ModelDataSubagian> data = FXCollections.observableArrayList();

        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try{
            String sql = "SELECT * FROM master_subagian";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            int x = 1;
            while (rs.next()){
                data.add(new ModelDataSubagian(
                        x++,
                        rs.getString("subagian_id"),
                        rs.getString("nama"),
                        rs.getString("bagian_warna"),
                        new Button("view")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        tabelSubagian.setItems(data);
    }

    void createaBtnView(String typebtn){
        Callback<TableColumn<ModelDataSubagian, Button>, TableCell<ModelDataSubagian, Button>> cellFactory =
                new Callback<TableColumn<ModelDataSubagian, Button>, TableCell<ModelDataSubagian, Button>>() {

                    @Override
                    public TableCell<ModelDataSubagian, Button> call(TableColumn<ModelDataSubagian, Button> param) {

                        final TableCell<ModelDataSubagian, Button> cell = new TableCell<ModelDataSubagian, Button>(){

                            final  Button btn = new Button(typebtn);

                            @Override
                            public void updateItem(Button item, boolean empty){
                                super.updateItem(item, empty);

                                if (empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    btn.setOnAction(event -> {
                                        ModelDataSubagian subag = getTableView().getItems().get(getIndex());
                                        bagianid = subag.getId();
                                        initBtnAction(typebtn, subag);
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
                action.setCellFactory(cellFactory);
                break;
        }

    }

    void initBtnAction(String type , ModelDataSubagian subag){
        simpan.setText("Ubah data");
        namaSubagian.setText(subag.getNama());
        colorPicker.setValue(Color.valueOf(subag.getWarna()));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateViewTable();
    }
}
