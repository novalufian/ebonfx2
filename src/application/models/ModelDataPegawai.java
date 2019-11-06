package application.models;

import javafx.scene.control.Button;

import java.sql.Blob;

public class ModelDataPegawai {

    public ModelDataPegawai(String id, String nip, String nama, String bagian, String sk, Blob ttd, Button view, Button update, Button delete) {
        this.id = id;
        this.nip = nip;
        this.nama = nama;
        this.bagian = bagian;
        this.sk = sk;
        this.ttd = ttd;
        this.view = view;
        this.update = update;
        this.delete = delete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBagian() {
        return bagian;
    }

    public void setBagian(String bagian) {
        this.bagian = bagian;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }

    public Button getUpdate() {
        return update;
    }

    public void setUpdate(Button update) {
        this.update = update;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public Blob getTtd() {
        return ttd;
    }

    public void setTtd(Blob ttd) {
        this.ttd = ttd;
    }


    String id, nip, nama, bagian, sk;
    Blob ttd;
    Button view, update, delete;

}
