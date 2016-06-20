package dataBase;

import javafx.util.Pair;
import model.core.ambulance.Ambulance;
import model.core.condition.Condition;
import model.core.medicine.MedicineAbs;

import java.util.List;

/**
 * Created by Marcin on 2016-01-21.
 */
public interface IDBquery {
    ///INSERT
    Pair<Integer, Integer> insertMedicineToDB(MedicineAbs medicine, Integer ambulanceID);
    Integer insertAmbulanceToDB(Ambulance ambulance);

    ///SELECT
    List<MedicineAbs> selectAllMedicineFromDB(Integer ambulanceID);
    List<Ambulance> selectAllAmbulanceFromDB();

    ///DELETE
    boolean deleteMedicineFromDB(Integer medicineID);
    boolean deleteAmbulanceFromDB(Integer ambID);

    //UPDATE
    void updateMedicinesToDB(List<Condition> conditionsToUpdate);



}
