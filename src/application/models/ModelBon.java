package application.models;

import javafx.scene.control.Button;

import java.util.Date;

public class ModelBon {

    public ModelBon(String bonid, String keterangan, String status, String bonSubagian, String jam_masuk, String jam_keluar, String createdAt, Button view, Button proses) {
        this.bonid = bonid;
        this.keterangan = keterangan;
        this.status = status;
        this.bonSubagian = bonSubagian;
        this.jam_masuk = jam_masuk;
        this.jam_keluar = jam_keluar;
        this.createdAt = createdAt;
        this.view = view;
        this.proses = proses;
    }

    public String getBonid() {
        return bonid;
    }

    public void setBonid(String bonid) {
        this.bonid = bonid;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBonSubagian() {
        return bonSubagian;
    }

    public void setBonSubagian(String bonSubagian) {
        this.bonSubagian = bonSubagian;
    }

    public String getJam_masuk() {
        return jam_masuk;
    }

    public void setJam_masuk(String jam_masuk) {
        this.jam_masuk = jam_masuk;
    }

    public String getJam_keluar() {
        return jam_keluar;
    }

    public void setJam_keluar(String jam_keluar) {
        this.jam_keluar = jam_keluar;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }

    public Button getProses() {
        return proses;
    }

    public void setProses(Button proses) {
        this.proses = proses;
    }

    String bonid, keterangan, status, bonSubagian, jam_masuk, jam_keluar, createdAt;
    Button view, proses;
}
