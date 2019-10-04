package application;

import javafx.scene.control.Button;

public class ModelDataPegawai {

    String nip, nama, bagian, sk;
    Button action;

    public ModelDataPegawai(String nip, String nama, String bagian, String sk, Button action) {
        this.nip = nip;
        this.nama = nama;
        this.bagian = bagian;
        this.sk = sk;
        this.action = action;
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

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        this.action = action;
    }
}
