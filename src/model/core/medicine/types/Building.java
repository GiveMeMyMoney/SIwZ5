package model.core.medicine.types;

import model.core.condition.Condition;
import model.core.medicine.CommodityAbs;
import model.core.medicine.ECategory;

/**
 * Created by Marcin on 2016-01-20.
 */
public class Building extends CommodityAbs {
    public Building(Integer medID, ECategory type, String name, String dateIntroduction, String dateExpiration, int codeEan, Condition condition) {
        super(medID, type, name, dateIntroduction, dateExpiration, codeEan, condition);
    }

    public Building(ECategory type, String name, String dateIntroduction, String dateExpiration, int codeEan, Condition condition) {
        super(type, name, dateIntroduction, dateExpiration, codeEan, condition);
    }
}
