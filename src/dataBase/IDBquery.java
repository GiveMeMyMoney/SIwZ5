package dataBase;

import javafx.util.Pair;
import model.core.condition.Condition;
import model.core.medicine.CommodityAbs;

import java.util.List;

/**
 * Created by Marcin on 2016-01-21.
 */
public interface IDBquery {
    ///INSERT
    Pair<Integer, Integer> insertCommodityToDB(CommodityAbs commodity);

    ///SELECT
    List<CommodityAbs> selectAllCommodityFromDB();

    ///DELETE

    //UPDATE
    void updateMedicinesToDB(List<Condition> conditionsToUpdate);



}
