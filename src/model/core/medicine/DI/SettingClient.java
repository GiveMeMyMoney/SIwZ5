package model.core.medicine.DI;

import model.core.medicine.ECategory;

/**
 * Created by Marcin on 2016-01-22.
 */

/**
 * 4. wzorzec Wstrzykiwanie zaleznosci (Dependency Injection)
 * Sluzy do zmieniania typu kategori przydatne np. podczas klikniecia
 * w dany btn i wyswietleniu wszystkich lekow tej kategorii...
 */
public class SettingClient implements ISettingClient {
    ECategory type = null;

    public SettingClient() {
        this.type = ECategory.get(1);
    }

    //SETTERs:
    @Override
    public void setType(ECategory type) {
        if (type != null) {
            this.type = type;
        } else {
            throw new NullPointerException("type == null!");
        }
    }

    //GETTERs:
    @Override
    public ECategory getType() {
        if (type != null) {
            return type;
        } else {
            throw new NullPointerException("Nie wybrano typu!");
        }
    }
}
