package dataBase;

import javafx.util.Pair;
import model.core.condition.Condition;
import model.core.medicine.CommodityFactory;
import model.core.medicine.ECategory;
import model.core.medicine.CommodityAbs;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Marcin on 2016-01-21.
 */
public class DBquery implements IDBquery {
    private static Logger logger = Logger.getLogger(DBquery.class.getName());

    private Connection connection;
    private PreparedStatement prepStmt;

    private static volatile DBquery instance = null;

    public static DBquery getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DBquery.class) {
                if (instance == null) {
                    logger.info("Tworze instancje DBquery");
                    instance = new DBquery();
                }
            }
        }
        return instance;
    }

    public DBquery() {
        try {
            connection = DBconnection.getInstance();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
    }

    ///INSERT methods:
    /**
     * metoda sluzaca do ladowania Commodity do BD.
     * @param commodity
     * @see CommodityAbs
     */
    @Override
    public Pair<Integer, Integer> insertCommodityToDB(CommodityAbs commodity) {
        logger.info("insertCommodityToDB: " + commodity.toString());
        //TODO do zmiany na tranzakcje bo ta co jest nie dziala dodaje do stan
        int i=0;
        Integer conID = null, medID = null;
        ResultSet rsConditionID = null, rsMedicineID = null;
        PreparedStatement insertMedicine = null, insertCondition = null, insertAmbulanceMedicineCondition = null;
        try {
            //connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            connection.setAutoCommit(true);
            //insert Medicine
            //TODO na potrzeby dzialania najpierw zrobic LEK pozniej STAN
            insertMedicine = connection.prepareStatement("insert into lek values (null, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            //----//
            int idCategory = commodity.getType().getId();
            i = 0;
            insertMedicine.setInt(++i, idCategory);
            insertMedicine.setString(++i, commodity.getName());
            insertMedicine.setInt(++i, commodity.getCodeEan());
            insertMedicine.setString(++i, commodity.getDateExpiration());
            insertMedicine.setString(++i, commodity.getDateIntroduction());
            //----//
            insertMedicine.execute();

            rsMedicineID = insertMedicine.getGeneratedKeys();
            medID = rsMedicineID.getInt(1);

            if (rsMedicineID.next()) {
                //insert Condition
                insertCondition = connection.prepareStatement("insert into stan values (null, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
                //----//
                i = 0;
                insertCondition.setInt(++i, commodity.getCondition().getQuantity());
                insertCondition.setInt(++i, commodity.getCondition().getSachets());
                insertCondition.setInt(++i, commodity.getCondition().getPills());
                //----//
                insertCondition.execute();

                rsConditionID = insertCondition.getGeneratedKeys();
                conID = rsConditionID.getInt(1);

                if (rsConditionID.next()) {
                    //insert
                    insertAmbulanceMedicineCondition = connection.prepareStatement("insert into karetka_lek values (null, ?, ?, ?);");
                    //----//
                    i = 0;
                    insertAmbulanceMedicineCondition.setInt(++i, ambulanceID);
                    insertAmbulanceMedicineCondition.setInt(++i, medID);
                    insertAmbulanceMedicineCondition.setInt(++i, conID);
                    //----//

                    insertAmbulanceMedicineCondition.execute();
                }
                //connection.commit();
            }
            if (conID != null && medID != null) {
                return new Pair<>(conID, medID);
            } else {
                throw new SQLException("ConID: " + conID + "medID: " + medID);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
            /*try {
                logger.info("Cofniecie tranzakcji w insertCommodityToDB()");
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }*/
            return null;
        }
        //TODO TRANZAKCJA
        finally {
            /*try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
            /*if (rsMedicineID != null) {
                try {
                    rsMedicineID.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                    ex.printStackTrace();
                }
            }
            if (insertMedicine != null) {
                try {
                    insertMedicine.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                    ex.printStackTrace();
                }
            }*/
        }
    }
    ///endregion

    ///SELECT methods:
    /**
     * Metoda zwracajaca wszystkie towary z danej kategorii (TRANZAKCJA)
     * @return ArrayList<CommodityAbs>
     */
    @Override
    public List<CommodityAbs> selectAllCommodityFromDB() {
        ResultSet resultAmbulanceMedicine = null, resultCondition = null, resultMedicine = null;
        List<CommodityAbs> medicineByType = new ArrayList<>();
        PreparedStatement selectAmbulanceMedicine = null, selectCondition = null, selectMedicine = null;
        try {
            //najpierw pobieram do result wszystkie rekordy o zadanym ID ambulance:
            //TODO 1 sprawdzenie czy w ogole dziala bez podzialu na kategorie
            //TODO moze pobrac wszystkie nie zaleznie od kategori do listy a nastepnie po kliknieciu w btn
            //TODO getByCategory.......
            selectAmbulanceMedicine = connection.prepareStatement("SELECT kl_lek_id, kl_stan_id FROM karetka_lek WHERE kl_kar_id = ?");
            selectAmbulanceMedicine.setInt(1, ambulanceID);
            resultAmbulanceMedicine = selectAmbulanceMedicine.executeQuery();
            while (resultAmbulanceMedicine.next()) {
                //Condition
                selectCondition = connection.prepareStatement("SELECT * FROM stan WHERE stan_id = ?");
                selectCondition.setInt(1, resultAmbulanceMedicine.getInt("kl_stan_id"));
                resultCondition = selectCondition.executeQuery();
                Condition condition = new Condition(resultCondition.getInt("stan_id"), resultCondition.getInt("stan_opak"), resultCondition.getInt("stan_saszetki"), resultCondition.getInt("stan_tabletki"));

                //Medicine
                selectMedicine = connection.prepareStatement("SELECT * FROM lek WHERE lek_id = ?");
                selectMedicine.setInt(1, resultAmbulanceMedicine.getInt("kl_lek_id"));
                resultMedicine = selectMedicine.executeQuery();
                CommodityAbs medicine = CommodityFactory.getMedicine(resultMedicine.getInt("lek_id"), ECategory.get(resultMedicine.getInt("lek_kt_id")), resultMedicine.getString("lek_nazwa"), resultMedicine.getString("lek_data_waznosci"),
                        resultMedicine.getString("lek_data_wprow"), resultMedicine.getInt("lek_kod_ean"), condition);
                if (medicine != null) {
                    logger.info("Medicine: " + medicine.toString());
                    medicineByType.add(medicine);
                } else {
                    logger.info("Err Medicine == null");
                }
            }
            return medicineByType;
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, e1);
            e1.printStackTrace();
            return null;
        }
    }

    ///endregion

    ///DELETE methods:


    ///endregion

    ///DELETE methods:
    //TODO zrobic stan w tabeli commodity i po sprawie
    @Override
    public void updateMedicinesToDB(List<Condition> conditionsToUpdate) {
        //TODO albo stworzyc do tego widok(bo te TRANZAKCJE SA ZUE!)
        PreparedStatement updateCondition = null;
        try {
            for (Condition condition : conditionsToUpdate) {
                updateCondition = connection.prepareStatement("UPDATE stan SET stan_opak = ?, stan_saszetki = ?, stan_tabletki = ? WHERE stan_id = ?");
                //----//
                int i = 0;
                updateCondition.setInt(++i, condition.getQuantity());
                updateCondition.setInt(++i, condition.getOrder());
                updateCondition.setInt(++i, condition.getConID());
                //----//
                updateCondition.execute();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }

    }


    ///endregion


}
