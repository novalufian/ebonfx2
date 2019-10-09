package application;

import javafx.scene.control.Button;

public class ModelBlok {
    public ModelBlok(Integer no, String id, String blok, Button action) {
        this.no = no;
        this.id = id;
        this.blok = blok;
        this.action = action;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
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

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        this.action = action;
    }

    Integer no;
    String id, blok;
    Button action;

}
