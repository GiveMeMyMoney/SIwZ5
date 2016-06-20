package model.core.ambulance;

/**
 * Created by Marcin on 2016-01-20.
 */
public class Ambulance implements Comparable<Ambulance> {
    Integer ambID;
    String registration, mark, model;

    public Ambulance(String registration, String mark, String model) {
        this.registration = registration;
        this.mark = mark;
        this.model = model;
    }

    public Ambulance(Integer ambID, String registration, String model, String mark) {
        this.ambID = ambID;
        this.registration = registration;
        this.model = model;
        this.mark = mark;
    }

    //METHODs:

    //GETTERs:
    public Integer getAmbID() {
        return ambID;
    }
    public String getRegistration() {
        return registration;
    }
    public String getMark() {
        return mark;
    }
    public String getModel() {
        return model;
    }

    //SETTERs:
    public void setAmbID(Integer ambID) {
        this.ambID = ambID;
    }
    public void setRegistration(String registration) {
        this.registration = registration;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return registration;
    }

    @Override
    public int compareTo(Ambulance a) {
        return a.getRegistration().compareTo(this.registration) * -1;
    }
}
