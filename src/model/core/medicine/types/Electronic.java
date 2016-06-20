package model.core.medicine.types;

import model.core.condition.Condition;
import model.core.medicine.CommodityAbs;
import model.core.medicine.ECategory;

/**
 * Created by Marcin on 2016-01-21.
 */
public class Electronic extends CommodityAbs {
    public Electronic(Integer medID, ECategory type, String name, String dateIntroduction, String dateExpiration, int codeEan, Condition condition) {
        super(medID, type, name, dateIntroduction, dateExpiration, codeEan, condition);
    }

    public Electronic(ECategory type, String name, String dateIntroduction, String dateExpiration, int codeEan, Condition condition) {
        super(type, name, dateIntroduction, dateExpiration, codeEan, condition);
    }
}
