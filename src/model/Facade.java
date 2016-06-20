package model;

import dataBase.DBquery;
import javafx.util.Pair;
import model.core.ambulance.Ambulance;
import model.core.condition.Condition;
import model.core.medicine.DI.ISettingClient;
import model.core.medicine.DI.SettingClient;
import model.core.medicine.ECategory;
import model.core.medicine.MedicineAbs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

    List<MedicineAbs> medicines = new ArrayList<>();
    List<Ambulance> ambulances = new ArrayList<>();
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
    public void setAmbulanceID(Integer ambID) {
        settings.setAmbulanceID(ambID);
    }

    @Override
    public void setType(ECategory type) {
        settings.setType(type);
    }
    ///endregion


    ///INSERT to DB
    @Override
    public void insertMedicineToDB(MedicineAbs medicine) {
        if (medicine != null) {
            Pair<Integer, Integer> pairConIdMedId = dbQuery.insertMedicineToDB(medicine, settings.getAmbulanceID());
            if (pairConIdMedId != null) {
                medicine.setConID(pairConIdMedId.getKey());
                medicine.setMedID(pairConIdMedId.getValue());
                medicines.add(medicine);
            }
        }
    }

    @Override
    public Ambulance insertAmbulanceToDB(Ambulance ambulance) {
        if (ambulance != null) {
            Integer newAmbID = dbQuery.insertAmbulanceToDB(ambulance);
            if (newAmbID != null) {
                ambulance.setAmbID(newAmbID);
                ambulances.add(ambulance);
                Collections.sort(ambulances);
                //TODO compareTo w Ambulance.
                setAmbulanceID(newAmbID);
                setType(ECategory.get(1));
                initAllMedicine();
            } else {
                //huston we have a problem! :D
            }
            return ambulance;
        } else {
            return null;
        }
    }
    ///endregion

    ///SELECT from DB
    public void initAllMedicine() {
        if (settings.getAmbulanceID() != null) {
            medicines = dbQuery.selectAllMedicineFromDB(settings.getAmbulanceID());
        }
        //medicinesCopy = medicines.cl
    }

    @Override
    public void initAllAmbulance() {
        ambulances = dbQuery.selectAllAmbulanceFromDB();
    }

    public List<ECategory> getAllCategories() {
        return ECategory.getAllCategories();
    }
    ///endregion

    ///DELETE from DB
    @Override
    public void deleteMedicineFromDB(MedicineAbs medicine) {
        if (medicine != null) {
            if (dbQuery.deleteMedicineFromDB(medicine.getMedID())) {
                medicines.remove(medicine);
            }

        }
    }

    @Override
    public void deleteAmbulanceFromDB(Ambulance ambulance) {
        if (ambulance != null) {
            if (dbQuery.deleteAmbulanceFromDB(ambulance.getAmbID())) {
                ambulances.remove(ambulance);
            }
        }
    }
    ///endregion

    ///UPDATE to DB
    @Override
    public void updateConditionsToDB() {
        dbQuery.updateMedicinesToDB(listConditionToUpdate);

        //listConditionToUpdate.clear();
    }


    ///endregion

    ///medicines
    public List<Ambulance> getAllAmbulances() {
        return ambulances;
    }

    public List<MedicineAbs> getAllMedicineByType() {
        List<MedicineAbs> medicinesWithType = new ArrayList<>();
        for (MedicineAbs medicine : medicines) {
            if (medicine.getType().equals(settings.getType())) {
                medicinesWithType.add(medicine);
            }
        }
        return medicinesWithType;
    }

    public void updateTempMedicinesArray(Integer medID, int newValue, String columnName, Condition condition) {
        for (MedicineAbs medicine : medicines) {
            if (Objects.equals(medicine.getMedID(), medID)) {
                switch (columnName.toUpperCase()) {
                    case NAME_PACKAGES:
                        medicine.setPackages(newValue);
                        break;
                    case NAME_SACHETS:
                        medicine.setSachets(newValue);
                        break;
                    case NAME_PILLS:
                        medicine.setPills(newValue);
                        break;
                }
                listConditionToUpdate.add(condition);
                break;
            }
        }
        //MedicineAbs medicineAbs = medicinesCopy.get(0);
    }
    ///endregion


}
