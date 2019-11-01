package application.models;

import javafx.scene.control.Button;

public class ModelDataNapi {
    public String getNapiid() {
        return napiid;
    }

    public void setNapiid(String napiid) {
        this.napiid = napiid;
    }

    public String getNapifoto() {
        return napifoto;
    }

    public void setNapifoto(String napifoto) {
        this.napifoto = napifoto;
    }

    public String getNapinoreg() {
        return napinoreg;
    }

    public void setNapinoreg(String napinoreg) {
        this.napinoreg = napinoreg;
    }

    public String getNapinama() {
        return napinama;
    }

    public void setNapinama(String napinama) {
        this.napinama = napinama;
    }

    public String getNapikamar() {
        return napikamar;
    }

    public void setNapikamar(String napikamar) {
        this.napikamar = napikamar;
    }

    public Button getUpdate() {
        return update;
    }

    public void setUpdate(Button update) {
        this.update = update;
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    String napiid;
    String napifoto;
    String napinoreg;
    String napinama;
    String napikamar;
    Button update, view, delete;

    public ModelDataNapi(String napiid, String napifoto, String napinoreg, String napinama, String napikamar, Button update, Button view, Button delete) {
        this.napiid = napiid;
        this.napifoto = napifoto;
        this.napinoreg = napinoreg;
        this.napinama = napinama;
        this.napikamar = napikamar;
        this.update = update;
        this.view = view;
        this.delete = delete;
    }






}
