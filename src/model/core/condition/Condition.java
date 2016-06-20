package model.core.condition;

/**
 * Created by Marcin on 2016-01-22.
 */
public class Condition {
    private boolean EMPTY;  //TODO to sie przyda do wyswietlania czerwonego gdy brak.
    int packages, sachets, pills;
    Integer conID;

    public Condition(Integer conID, int packages, int sachets, int pills) {
        this.conID = conID;
        this.packages = packages;
        this.sachets = sachets;
        this.pills = pills;
        checkIfEmpty();
    }

    public Condition(int packages, int sachets, int pills) {
        this.packages = packages;
        this.sachets = sachets;
        this.pills = pills;
        checkIfEmpty();
    }

    public Condition(Condition condition) {
        this.packages = condition.getPackages();
        this.sachets = condition.getSachets();
        this.pills = condition.getPills();
        checkIfEmpty();
    }

    void checkIfEmpty() {
        EMPTY = (packages == 0 && sachets == 0 && pills == 0);
    }

    //GETTERs:
    public Integer getConID() {
        if (conID != null) {
            return conID;
        } else {
            throw new NullPointerException("Nie ma ConID dla aktualnego Condition: " + toString());
        }
    }
    public int getPackages() {
        return packages;
    }
    public boolean isEMPTY() {
        return EMPTY;
    }
    public int getSachets() {
        return sachets;
    }
    public int getPills() {
        return pills;
    }

    //SETTERs:
    public void setConID(Integer conID) {
        this.conID = conID;
    }
    public void setPackages(int packages) {
        this.packages = packages;
        checkIfEmpty();
    }
    public void setPills(int pills) {
        this.pills = pills;
        checkIfEmpty();
    }
    public void setSachets(int sachets) {
        this.sachets = sachets;
        checkIfEmpty();
    }
    public void setEMPTY(boolean EMPTY) {
        this.EMPTY = EMPTY;
    }

    @Override
    public String toString() {
        return "Condition{" +
                ", conID=" + conID +
                ", packages=" + packages +
                ", sachets=" + sachets +
                ", pills=" + pills +
                "EMPTY=" + EMPTY +
                '}';
    }
}
