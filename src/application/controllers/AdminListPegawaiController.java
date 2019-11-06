
package application.controllers;

import application.connectifity.ConnectionClass;
import application.models.ModelDataPegawai;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdminListPegawaiController implements Initializable {

    DataUserAdminTambahController tambag_controller;

    public AdminListPegawaiController(DataUserAdminTambahController controller) {
        tambag_controller = controller;
    }

    @FXML
    private TextField textSearch;

    @FXML
    private TableView<ModelDataPegawai> tablePegawai;

    @FXML
    private TableColumn<ModelDataPegawai, String> nip;

    @FXML
    private TableColumn<ModelDataPegawai, String> nama;

    @FXML
    private TableColumn<ModelDataPegawai, String> subagian;

    @FXML
    private TableColumn<ModelDataPegawai, Button> action;

    void generateTable(){
        nip.setCellValueFactory(new PropertyValueFactory<>("nip"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        subagian.setCellValueFactory(new PropertyValueFactory<>("bagian"));
        action.setCellValueFactory( new PropertyValueFactory<>("view"));
        createaBtnView("add");
    }

    void generateTableData(){
        ObservableList<ModelDataPegawai> data = FXCollections.observableArrayList();
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try{
            String sql = "SELECT * FROM data_pegawai " +
                    "INNER JOIN master_subagian ON data_pegawai.subag_pegawai = master_subagian.subagian_id " +
                    "WHERE data_pegawai.pegawai_published = 1";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                String jk = "laki-laki";
                if (!rs.getBoolean("jenis_kelamin_pegawai")){
                    jk = "perempuan";
                }
                data.add(new ModelDataPegawai(
                        rs.getString("user_id"),
                        rs.getString("nip_pegawai"),
                        rs.getString("nama_pegawai"),
                        rs.getString("nama"),
                        jk,
                        rs.getBlob("ttd_pegawai"),
                        new Button("view"),
                        new Button("update"),
                        new Button("delete")

                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        tablePegawai.setItems(data);

        FilteredList<ModelDataPegawai> filteredList = new FilteredList<>(data, b -> true);
        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(model_barang -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (model_barang.getNama().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(model_barang.getNip().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                    return true;
                }else if(model_barang.getBagian().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else{
                    return false;
                }
            });
        });

        SortedList<ModelDataPegawai> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tablePegawai.comparatorProperty());

        tablePegawai.setItems(sortedList);
    }

    void createaBtnView(String typebtn){
        Callback<TableColumn<ModelDataPegawai, Button>, TableCell<ModelDataPegawai, Button>> cellFactory =
                new Callback<TableColumn<ModelDataPegawai, Button>, TableCell<ModelDataPegawai, Button>>() {

                    @Override
                    public TableCell<ModelDataPegawai, Button> call(TableColumn<ModelDataPegawai, Button> param) {

                        final TableCell<ModelDataPegawai, Button> cell = new TableCell<ModelDataPegawai, Button>(){

                            final  Button btn = new Button(typebtn);

                            @Override
                            public void updateItem(Button item, boolean empty){
                                super.updateItem(item, empty);

                                if (empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    btn.setOnAction(event -> {
                                        ModelDataPegawai pegawai = getTableView().getItems().get(getIndex());
                                        tambag_controller.pegawai.setText(pegawai.getNama());
                                        tambag_controller.userid = pegawai.getId();

                                        Stage stage = (Stage) textSearch.getScene().getWindow();
                                        stage.close();

                                    });

                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };


                action.setCellFactory(cellFactory);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateTable();
        generateTableData();
        System.out.println("AdminListPegawaiController.initialize");
    }
}
