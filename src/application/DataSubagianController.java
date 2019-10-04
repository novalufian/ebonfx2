package application;

import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DataSubagianController implements Initializable {

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
    void pilihWarna(ActionEvent event) {

    }

    void initTable(){
        initColumn();
    }

    void initColumn(){
        no.setCellValueFactory(new PropertyValueFactory<>("no"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        warna.setCellValueFactory(new PropertyValueFactory<>("warna"));
        action.setCellValueFactory(new PropertyValueFactory<>("view"));
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateViewTable();
    }
}
