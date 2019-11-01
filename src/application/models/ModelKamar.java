package application.models;

import javafx.scene.control.Button;

public class ModelKamar {
    public ModelKamar( Integer no, String id, String blok, String kamar, Button action) {
        this.id = id;
        this.blok = blok;
        this.kamar = kamar;
        this.no = no;
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        this.action = action;
    }

    String id, blok, kamar;
    Integer no;
    Button action;



}
