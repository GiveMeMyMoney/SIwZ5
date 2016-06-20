package model.core.medicine;

import java.util.*;

/**
 * Created by Marcin on 2016-01-20.
 */
public enum ECategory {
    DRESSING (1),
    PAINKILLER (2),
    OTHERS (3); //i wiele innych...

    private static final Map<Integer,ECategory> lookup = new HashMap<>();
    static {
        for (ECategory c : EnumSet.allOf(ECategory.class)) {
            lookup.put(c.getId(), c);
        }
    }

    public static List<ECategory> getAllCategories() {
        List<ECategory> categories = new ArrayList<>();
        for (ECategory c : EnumSet.allOf(ECategory.class)) {
            categories.add(c);
        }
        return categories;
    }

    private int id;
    ECategory(int id) {
        this.id = id;
    }

    public int getId() { return id; }

    public static ECategory get(int id) {
        return lookup.get(id);
    }

}
