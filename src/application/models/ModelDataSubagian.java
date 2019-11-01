package application.models;

import javafx.scene.control.Button;

public class ModelDataSubagian {
    int no;
    String id;
    String nama;
    String warna;
    Button view;

    public ModelDataSubagian(int no, String id, String nama, String warna, Button view) {
        this.no = no;
        this.id = id;
        this.nama = nama;
        this.warna = warna;
        this.view = view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
