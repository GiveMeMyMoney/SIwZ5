package model.core.medicine;

/**
 * Created by Marcin on 2016-01-20.
 */

import model.core.condition.Condition;
import model.core.medicine.types.Building;
import model.core.medicine.types.Other;
import model.core.medicine.types.Electronic;

import java.util.logging.Logger;

/**
 * 2 wzorzec projektowy Fabryka(Factory).
 */
public class CommodityFactory {
    private static Logger logger = Logger.getLogger(CommodityFactory.class.getName());

    public static CommodityAbs getMedicine(Integer medID, ECategory type, String name, String dateExpiration, String dateIntroduction, int codeEan, Condition condition)
    {
        logger.info("Typ: " + type);
        if(type != null) {
            switch (type) {
                case BUILDING:
                    Building building = new Building(medID, type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return building;
                case ELECTRONIC:
                    Electronic electronic = new Electronic(medID, type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return electronic;
                case OTHERS:
                    Other other = new Other(medID, type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return other;
            }
        }
        return null;
    }

    public static CommodityAbs getMedicine(ECategory type, String name, String dateExpiration, String dateIntroduction, int codeEan, Condition condition)
    {
        logger.info("Typ: " + type);
        if(type != null) {
            switch (type) {
                case BUILDING:
                    Building building = new Building(type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return building;
                case ELECTRONIC:
                    Electronic electronic = new Electronic(type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return electronic;
                case OTHERS:
                    Other other = new Other(type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return other;
            }
        }
        return null;
    }
}
