package model.core.medicine.DI;

import model.core.medicine.ECategory;

/**
 * Created by Marcin on 2016-01-22.
 */
public interface ISettingClient {
    void setType(ECategory type);
    void setAmbulanceID(Integer ambID);

    ECategory getType();
    Integer getAmbulanceID();
}
