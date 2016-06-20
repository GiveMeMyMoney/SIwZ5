package model.core.medicine;

import model.core.condition.Condition;

/**
 * Created by Marcin on 2016-01-20.
 */
public abstract class CommodityAbs {
    ECategory type = null;  //kategoria leku
    String name, dateExpiration, dateIntroduction;
    int codeEan;
    Integer medID;
    Condition condition = null;

    public CommodityAbs(Integer medID, ECategory type, String name, String dateIntroduction, String dateExpiration, int codeEan, Condition condition) {
        this.medID = medID;
        this.type = type;
        this.name = name;
        this.dateIntroduction = dateIntroduction;
        this.dateExpiration = dateExpiration;
        this.codeEan = codeEan;
        this.condition = condition;
    }

    public CommodityAbs(ECategory type, String name, String dateIntroduction, String dateExpiration, int codeEan, Condition condition) {
        this.type = type;
        this.name = name;
        this.dateExpiration = dateExpiration;
        this.dateIntroduction = dateIntroduction;
        this.codeEan = codeEan;
        this.condition = condition;
    }

    //METHODs:

    //GETTERs:
    public Integer getMedID() {
        return medID;
    }
    public ECategory getType() {
        return type;
    }
    public String getName() {
        return name;
    }
    public String getDateExpiration() {
        return dateExpiration;
    }
    public String getDateIntroduction() {
        return dateIntroduction;
    }
    public int getCodeEan() {
        return codeEan;
    }

    //Condition:
    public Integer getConID() {
        return condition.getConID();
    }
    public Condition getCondition() {
        return condition;
    }
    public int getQuantity() {
        return condition.getQuantity();
    }
    public int getOrder() {
        return condition.getOrder();
    }
    public boolean isEMPTY() {
        return condition.isEMPTY();
    }

    //SETTERs:
    public void setMedID(Integer medID) {
        this.medID = medID;
    }
    public void setType(ECategory type) {
        this.type = type;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    public void setDateIntroduction(String dateIntroduction) {
        this.dateIntroduction = dateIntroduction;
    }
    public void setCodeEan(int codeEan) {
        this.codeEan = codeEan;
    }
    //Condition:
    public void setConID(Integer conID) {
        if (conID != null) {
            condition.setConID(conID);
        } else {
            throw new NullPointerException("ID dla tego condition: " + condition.toString() + "jest null");
        }
    }
    public void setCondition(Condition condition) {
        this.condition = condition;
    }
    public void setQuantity(int quant) {
        condition.setQuantity(quant);
    }
    public void setOrder(int order) {
        condition.setOrder(order);
    }

    @Override
    public String toString() {
        return "CommodityAbs{" +
                "ID=" + medID +
                "type=" + type +
                ", name='" + name + '\'' +
                ", dateExpiration='" + dateExpiration + '\'' +
                ", dateIntroduction='" + dateIntroduction + '\'' +
                ", codeEan=" + codeEan +
                ", condition=" + condition.toString() +
                '}';
    }
}
