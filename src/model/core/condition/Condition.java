package model.core.condition;

/**
 * Created by Marcin on 2016-01-22.
 */
public class Condition {
    private boolean EMPTY;  //TODO to sie przyda do wyswietlania czerwonego gdy brak.
    int quantity, order;
    Integer conID;

    public Condition(Integer conID, int quantity) {
        this.conID = conID;
        this.quantity = quantity;
        this.order = 0;
        checkIfEmpty();
    }

    public Condition(int quantity) {
        this.quantity = quantity;
        this.order = 0;
        checkIfEmpty();
    }

    public Condition(Condition condition) {
        this.quantity = condition.getQuantity();
        this.order = 0;
        checkIfEmpty();
    }

    void checkIfEmpty() {
        EMPTY = (quantity == 0);
    }

    //GETTERs:
    public Integer getConID() {
        if (conID != null) {
            return conID;
        } else {
            throw new NullPointerException("Nie ma ConID dla aktualnego Condition: " + toString());
        }
    }
    public int getQuantity() {
        return quantity;
    }
    public boolean isEMPTY() {
        return EMPTY;
    }

    public int getOrder() {
        return order;
    }

    //SETTERs:
    public void setConID(Integer conID) {
        this.conID = conID;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        checkIfEmpty();
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setEMPTY(boolean EMPTY) {
        this.EMPTY = EMPTY;
    }

    @Override
    public String toString() {
        return "Condition{" +
                ", conID=" + conID +
                ", quantity=" + quantity +
                "EMPTY=" + EMPTY +
                '}';
    }
}
