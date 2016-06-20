package dataBase;

import javafx.util.Pair;
import model.core.ambulance.Ambulance;
import model.core.condition.Condition;
import model.core.medicine.ECategory;
import model.core.medicine.MedicineAbs;
import model.core.medicine.MedicineFactory;

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
     * metoda sluzaca do ladowania Ambulance do BD.
     * @param ambulance
     * @see Ambulance
     */
    @Override
    public Integer insertAmbulanceToDB(Ambulance ambulance) {
        logger.info("insertAmbulanceToDB: " + ambulance.toString());
        ResultSet rsAmbulanceID = null;
        try {
            prepStmt = connection.prepareStatement("insert into karetka values (null, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            //----//
            int i=0;
            prepStmt.setString(++i, ambulance.getRegistration());
            prepStmt.setString(++i, ambulance.getMark());
            prepStmt.setString(++i, ambulance.getModel());
            //----//
            prepStmt.execute();
            rsAmbulanceID = prepStmt.getGeneratedKeys();
            return rsAmbulanceID.getInt(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * metoda sluzaca do ladowania Medicine do BD.
     * @param medicine - lek ze stanem
     * @see MedicineAbs
     */
    @Override
    public Pair<Integer, Integer> insertMedicineToDB(MedicineAbs medicine, Integer ambulanceID) {
        logger.info("insertMedicineToDB: " + medicine.toString());
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
            int idCategory = medicine.getType().getId();
            i = 0;
            insertMedicine.setInt(++i, idCategory);
            insertMedicine.setString(++i, medicine.getName());
            insertMedicine.setInt(++i, medicine.getCodeEan());
            insertMedicine.setString(++i, medicine.getDateExpiration());
            insertMedicine.setString(++i, medicine.getDateIntroduction());
            //----//
            insertMedicine.execute();

            rsMedicineID = insertMedicine.getGeneratedKeys();
            medID = rsMedicineID.getInt(1);

            if (rsMedicineID.next()) {
                //insert Condition
                insertCondition = connection.prepareStatement("insert into stan values (null, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
                //----//
                i = 0;
                insertCondition.setInt(++i, medicine.getCondition().getPackages());
                insertCondition.setInt(++i, medicine.getCondition().getSachets());
                insertCondition.setInt(++i, medicine.getCondition().getPills());
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
                logger.info("Cofniecie tranzakcji w insertMedicineToDB()");
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
     * Metoda zwracajaca wszystkie leki z danej kategorii i o danym ID ambulansu (TRANZAKCJA)
     * @param ambulanceID
     * @return ArrayList<MedicineAbs>
     */
    @Override
    public List<MedicineAbs> selectAllMedicineFromDB(Integer ambulanceID) {
        logger.info("Ambulance ID: " + ambulanceID);
        ResultSet resultAmbulanceMedicine = null, resultCondition = null, resultMedicine = null;
        List<MedicineAbs> medicineByType = new ArrayList<>();
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
                MedicineAbs medicine = MedicineFactory.getMedicine(resultMedicine.getInt("lek_id"), ECategory.get(resultMedicine.getInt("lek_kt_id")), resultMedicine.getString("lek_nazwa"), resultMedicine.getString("lek_data_waznosci"),
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

    @Override
    public List<Ambulance> selectAllAmbulanceFromDB() {
        logger.info("initAllAmbulance()");
        ResultSet result = null;
        List<Ambulance> ambulances = new ArrayList<>();
        try {
            prepStmt = connection.prepareStatement("SELECT * FROM karetka ORDER BY kar_rejestracja");
            result = prepStmt.executeQuery();
            //pozniej za pomoca Factory tworze konkretny typ(nie musze wiedziec jaki) pojazdu i dopisuje do ArrayListy
            while (result.next()) {
                Ambulance ambulance = new Ambulance(result.getInt("kar_id"), result.getString("kar_rejestracja"), result.getString("kar_marka"), result.getString("kar_model"));
                if (ambulance != null) {
                    logger.info("Ambulance: " + ambulance.toString());
                    ambulances.add(ambulance);
                } else {
                    logger.info("Err Ambulance = null");
                }
            }
            return ambulances;
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, e1);
            e1.printStackTrace();
            return null;
        }
    }

    ///endregion

    ///DELETE methods:
    @Override
    public boolean deleteMedicineFromDB(Integer medicineID) {
        ResultSet rsAmbulanceMedicineConditionID = null;
        PreparedStatement  selectAmbulanceMedicineConditionID = null, deleteCondition = null, deleteMedicine = null,
                deleteAmbulanceMedicineConditionID = null;  //TODO skrocic nazwy.
        try {
            selectAmbulanceMedicineConditionID = connection.prepareStatement("SELECT kl_id, kl_stan_id FROM karetka_lek WHERE kl_lek_id = ?");
            selectAmbulanceMedicineConditionID.setInt(1, medicineID);
            rsAmbulanceMedicineConditionID = selectAmbulanceMedicineConditionID.executeQuery();
            if (rsAmbulanceMedicineConditionID.next()) {
                deleteMedicine = connection.prepareStatement("DELETE FROM lek WHERE lek_id = ?");
                deleteMedicine.setInt(1, medicineID);
                deleteMedicine.execute();

                deleteCondition = connection.prepareStatement("DELETE FROM stan WHERE stan_id = ?");
                deleteCondition.setInt(1, rsAmbulanceMedicineConditionID.getInt("kl_stan_id"));
                deleteCondition.execute();

                deleteAmbulanceMedicineConditionID = connection.prepareStatement("DELETE FROM karetka_lek WHERE kl_id = ?");
                deleteAmbulanceMedicineConditionID.setInt(1, rsAmbulanceMedicineConditionID.getInt("kl_id"));
                deleteAmbulanceMedicineConditionID.execute();
            }
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAmbulanceFromDB(Integer ambID) {
        PreparedStatement deleteAmbulance = null, deleteAmbulanceMedicineConditionID = null;
        try {
            deleteAmbulanceMedicineConditionID = connection.prepareStatement("DELETE FROM karetka_lek WHERE kl_kar_id = ?");
            deleteAmbulanceMedicineConditionID.setInt(1, ambID);
            deleteAmbulanceMedicineConditionID.execute();

            deleteAmbulance = connection.prepareStatement("DELETE FROM karetka WHERE kar_id = ?");
            deleteAmbulance.setInt(1, ambID);
            deleteAmbulance.execute();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
            return false;
        }
    }

    ///endregion

    ///DELETE methods:

    @Override
    public void updateMedicinesToDB(List<Condition> conditionsToUpdate) {
        //TODO albo stworzyc do tego widok(bo te TRANZAKCJE SA ZUE!)
        PreparedStatement updateCondition = null;
        try {
            for (Condition condition : conditionsToUpdate) {
                updateCondition = connection.prepareStatement("UPDATE stan SET stan_opak = ?, stan_saszetki = ?, stan_tabletki = ? WHERE stan_id = ?");
                //----//
                int i = 0;
                updateCondition.setInt(++i, condition.getPackages());
                updateCondition.setInt(++i, condition.getSachets());
                updateCondition.setInt(++i, condition.getPills());
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
