package model;

import dataBase.IDBquery;
import model.core.medicine.DI.ISettingClient;
import model.core.medicine.ECategory;
import model.core.medicine.CommodityAbs;

/**
 * Created by Marcin on 2016-01-22.
 */
public interface IFacade {
    /**
     * @see ISettingClient
     */
    void setType(ECategory type);

    /**
     * @see IDBquery
     */
    ///INSERT
    void insertCommodityToDB(CommodityAbs commodity);

    ///SELECT
    //List<CommodityAbs> selectAllCommodityFromDB();

    ///DELETE

    //UPDATE
    void updateConditionsToDB();


}
