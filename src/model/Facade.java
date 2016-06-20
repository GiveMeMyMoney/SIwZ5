package model;

import dataBase.DBquery;
import javafx.util.Pair;
import model.core.condition.Condition;
import model.core.medicine.DI.ISettingClient;
import model.core.medicine.DI.SettingClient;
import model.core.medicine.ECategory;
import model.core.medicine.CommodityAbs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Created by Marcin on 2016-01-20.
 */

/**
 * 3 wzorzec: Fasada (Facade)
 */
public class Facade implements IFacade {    //TODO poprawic interface!
    private static Logger logger = Logger.getLogger(Facade.class.getName());
    private static final String NAME_PACKAGES = "PACKAGES";
    private static final String NAME_SACHETS = "SACHETS";
    private static final String NAME_PILLS = "PILLS";
    //ArrayList<Contact> contacts = new ArrayList<>();

    private static volatile Facade instance = null;
    private static DBquery dbQuery = null;
    private ISettingClient settings = new SettingClient();

    List<CommodityAbs> medicines = new ArrayList<>();
    List<Condition> listConditionToUpdate = new ArrayList<>();

    public static Facade getInstance() {
        if (instance == null) {
            synchronized (Facade.class) {
                if (instance == null) {
                    logger.info("Tworze instancje Facade");
                    instance = new Facade();
                    try {
                        dbQuery = DBquery.getInstance();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

    private Facade() {

    }

    ///DI

    @Override
    public void setType(ECategory type) {
        settings.setType(type);
    }
    ///endregion


    ///INSERT to DB
    @Override
    public void insertCommodityToDB(CommodityAbs commodity) {
        if (commodity != null) {
            Pair<Integer, Integer> pairConIdMedId = dbQuery.insertCommodityToDB(commodity);
            if (pairConIdMedId != null) {
                commodity.setConID(pairConIdMedId.getKey());
                commodity.setMedID(pairConIdMedId.getValue());
                medicines.add(commodity);
            }
        }
    }

    ///endregion

    ///SELECT from DB
    public void initAllMedicine() {
        medicines = dbQuery.selectAllCommodityFromDB();
        //medicinesCopy = medicines.cl
    }

    public List<ECategory> getAllCategories() {
        return ECategory.getAllCategories();
    }
    ///endregion

    ///DELETE from DB

    ///endregion

    ///UPDATE to DB
    @Override
    public void updateConditionsToDB() {
        dbQuery.updateMedicinesToDB(listConditionToUpdate);

        //listConditionToUpdate.clear();
    }


    ///endregion

    ///medicines
    public List<CommodityAbs> getAllMedicineByType() {
        List<CommodityAbs> medicinesWithType = new ArrayList<>();
        for (CommodityAbs medicine : medicines) {
            if (medicine.getType().equals(settings.getType())) {
                medicinesWithType.add(medicine);
            }
        }
        return medicinesWithType;
    }

    public void updateTempCommodityArray(Integer medID, int newValue, Condition condition) {
        for (CommodityAbs commodity : medicines) {
            if (Objects.equals(commodity.getMedID(), medID)) {
                commodity.setQuantity(newValue);
                listConditionToUpdate.add(condition);
                break;
            }
        }
        //CommodityAbs medicineAbs = medicinesCopy.get(0);
    }
    ///endregion


}
