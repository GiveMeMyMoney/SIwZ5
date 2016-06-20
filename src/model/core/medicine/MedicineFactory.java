package model.core.medicine;

/**
 * Created by Marcin on 2016-01-20.
 */

import model.core.condition.Condition;
import model.core.medicine.types.Dressing;
import model.core.medicine.types.Other;
import model.core.medicine.types.Painkiller;

import java.util.logging.Logger;

/**
 * 2 wzorzec projektowy Fabryka(Factory).
 */
public class MedicineFactory {
    private static Logger logger = Logger.getLogger(MedicineFactory.class.getName());

    public static MedicineAbs getMedicine(Integer medID, ECategory type, String name, String dateExpiration, String dateIntroduction, int codeEan, Condition condition)
    {
        logger.info("Typ: " + type);
        if(type != null) {
            switch (type) {
                case DRESSING:
                    Dressing dressing = new Dressing(medID, type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return dressing;
                case PAINKILLER:
                    Painkiller painkiller = new Painkiller(medID, type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return painkiller;
                case OTHERS:
                    Other other = new Other(medID, type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return other;
            }
        }
        return null;
    }

    public static MedicineAbs getMedicine(ECategory type, String name, String dateExpiration, String dateIntroduction, int codeEan, Condition condition)
    {
        logger.info("Typ: " + type);
        if(type != null) {
            switch (type) {
                case DRESSING:
                    Dressing dressing = new Dressing(type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return dressing;
                case PAINKILLER:
                    Painkiller painkiller = new Painkiller(type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return painkiller;
                case OTHERS:
                    Other other = new Other(type, name, dateExpiration, dateIntroduction, codeEan, condition);
                    return other;
            }
        }
        return null;
    }
}
