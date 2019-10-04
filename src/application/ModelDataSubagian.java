package application;

import javafx.scene.control.Button;

public class ModelDataSubagian {
    int no;
    String nama, warna;
    Button view;

    public ModelDataSubagian(int no, String nama, String warna, Button view) {
        this.no = no;
        this.nama = nama;
        this.warna = warna;
        this.view = view;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }
}
