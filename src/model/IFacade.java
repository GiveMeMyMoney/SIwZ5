package model;

import dataBase.IDBquery;
import model.core.ambulance.Ambulance;
import model.core.medicine.DI.ISettingClient;
import model.core.medicine.ECategory;
import model.core.medicine.MedicineAbs;

/**
 * Created by Marcin on 2016-01-22.
 */
public interface IFacade {
    /**
     * @see ISettingClient
     */
    void setType(ECategory type);
    void setAmbulanceID(Integer ambID);

    /**
     * @see IDBquery
     */
    ///INSERT
    void insertMedicineToDB(MedicineAbs medicine);
    Ambulance insertAmbulanceToDB(Ambulance ambulance);

    ///SELECT
    //List<MedicineAbs> selectAllMedicineFromDB();
    void initAllAmbulance();

    ///DELETE
    void deleteMedicineFromDB(MedicineAbs medicine);
    void deleteAmbulanceFromDB(Ambulance ambulance);

    //UPDATE
    void updateConditionsToDB();


}
