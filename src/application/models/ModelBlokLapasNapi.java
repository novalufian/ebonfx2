package application.models;

import javafx.scene.control.Button;

public class ModelBlokLapasNapi {

    String id, nama, blok, kamar, status;
    Button book;

    public ModelBlokLapasNapi(String id, String nama, String blok, String kamar, String status, Button book) {
        this.id = id;
        this.nama = nama;
        this.blok = blok;
        this.kamar = kamar;
        this.status = status;
        this.book = book;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBlok() {
        return blok;
    }

    public void setBlok(String blok) {
        this.blok = blok;
    }

    public String getKamar() {
        return kamar;
    }

    public void setKamar(String kamar) {
        this.kamar = kamar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Button getBook() {
        return book;
    }

    public void setBook(Button book) {
        this.book = book;
    }
}
