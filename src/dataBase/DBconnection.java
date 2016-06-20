package dataBase;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Marcin on 2016-01-20.
 */
public class DBconnection {
    private static Logger logger = Logger.getLogger(DBconnection.class.getName());
    private static Connection conn = null;
    private PreparedStatement prepStmt;

    private static volatile DBconnection instance = null;

    /**
     * Singleton ktory tworzy tylko 1 instancje klasy na wszystkich watkach(synchronized).
     * 1. wzorzec
     */
    public static Connection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DBconnection.class) {
                if (instance == null) {
                    logger.info("Tworze instancje DBconnection");
                    instance = new DBconnection();
                }
            }
        }
        return getConn();
    }

    private static Connection getConn() {
        return conn;
    }

    private DBconnection() {
        try
        {
            logger.info("DBconnection construct");
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:wirtualna_apteczka_DB.db");
            logger.info("Success, connection with DB!");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
    }



    ///UPDATE methods:
    /*public void updateAmbulanceToDB(Ambulance ambulance) {
        logger.info("putAmbulanceToDB: " + ambulance.toString());
        try {
            prepStmt = conn.prepareStatement("insert into karetka values (null, ?, ?, ?, ?, ?);");
            //----//
            int i=0;
            prepStmt.setInt(++i, ambulance.getMarka());
            prepStmt.setString(++i, ambulance.getName());
            prepStmt.setString(++i, ambulance.getPrice());
            prepStmt.setString(++i, ambulance.getAvibility());
            prepStmt.setString(++i, ambulance.getAvibility());    //jak zdjecie?
            //----//
            prepStmt.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
    }

    public void updateHospitalToDB(Hospital hospital) {
        logger.info("putAmbulanceToDB: " + hospital.toString());
        try {
            prepStmt = conn.prepareStatement("insert into szpital values (null, ?, ?, ?);");
            //----//
            int i=0;
            prepStmt.setString(++i, hospital.getName());
            prepStmt.setInt(++i, hospital.getPrice());
            prepStmt.setInt(++i, hospital.getAvibility());
            //----//
            prepStmt.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
    }

    public void updateMedicineToDB(Medicine medicine) {
        logger.info("putAmbulanceToDB: " + medicine.toString());
        try {
            prepStmt = conn.prepareStatement("insert into lek values (null, ?, ?, ?, ?, ?, ?);");
            //----//
            int i=0;
            prepStmt.setInt(++i, medicine.getName());
            prepStmt.setString(++i, medicine.getPrice());
            prepStmt.setInt(++i, medicine.getAvibility());
            prepStmt.setString(++i, medicine.getName());
            prepStmt.setString(++i, medicine.getPrice());
            prepStmt.setString(++i, medicine.getAvibility());
            //----//
            prepStmt.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
    }
    ///endregion

    ///DELETE methods:
    public void deleteAmbulanceToDB(Ambulance ambulance) {
        logger.info("Vehicle: " + vehicle.toString());
        if(!vehicle.getAvibility()) {
            return;
        }
        ///nie moge wziac ID poniewaz przy tworzeniu kontaktu nie tworze ID - ktore jest AutoIncrementowane w BD.
        String marka = vehicle.getMarka(), name = vehicle.getName(), price = vehicle.getPrice();
        try {
            prepStmt = conn.prepareStatement("DELETE from Vehicle " +
                    "where veh_marka = '" + marka + "' AND veh_name = '" + name + "' AND veh_price = '" + price + "'");
            //dodawanie "'" zeby dla SQLa wygladalo to jako String.
            prepStmt.execute();
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, e1);
            e1.printStackTrace();
        }
    }

    public void deleteHospitalToDB(Hospital hospital) {
        logger.info("Vehicle: " + vehicle.toString());
        if(!vehicle.getAvibility()) {
            return;
        }
        ///nie moge wziac ID poniewaz przy tworzeniu kontaktu nie tworze ID - ktore jest AutoIncrementowane w BD.
        String marka = vehicle.getMarka(), name = vehicle.getName(), price = vehicle.getPrice();
        try {
            prepStmt = conn.prepareStatement("DELETE from Vehicle " +
                    "where veh_marka = '" + marka + "' AND veh_name = '" + name + "' AND veh_price = '" + price + "'");
            //dodawanie "'" zeby dla SQLa wygladalo to jako String.
            prepStmt.execute();
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, e1);
            e1.printStackTrace();
        }
    }

    public void deleteMedicineToDB(Medicine medicine) {
        logger.info("Vehicle: " + vehicle.toString());
        if(!vehicle.getAvibility()) {
            return;
        }
        ///nie moge wziac ID poniewaz przy tworzeniu kontaktu nie tworze ID - ktore jest AutoIncrementowane w BD.
        String marka = vehicle.getMarka(), name = vehicle.getName(), price = vehicle.getPrice();
        try {
            prepStmt = conn.prepareStatement("DELETE from Vehicle " +
                    "where veh_marka = '" + marka + "' AND veh_name = '" + name + "' AND veh_price = '" + price + "'");
            //dodawanie "'" zeby dla SQLa wygladalo to jako String.
            prepStmt.execute();
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, e1);
            e1.printStackTrace();
        }
    }
    ///endregion

    ///SELECT region


    ///endregion



    *//**
     * metoda pobierajaca wszystkie rekordy z BD o danm TYPE od Vehicle(Pojazdu).
     * @param type
     * @return ArrayList<VehicleAbs>
     *//*
    public ArrayList<VehicleAbs> getAllVehicleByTypeFromDB(String type) {
        logger.info("Type: " + type);
        ResultSet result = null;
        ArrayList<VehicleAbs> vehiclesByType = new ArrayList<>();
        try {
            //najpierw pobieram do result wszystkie rekordy o zadanym type TYPE:
            prepStmt = conn.prepareStatement("SELECT * FROM Vehicle WHERE veh_type = ? order by veh_marka");
            prepStmt.setString(1, type.toLowerCase());
            result = prepStmt.executeQuery();
            VehicleAbs vehicle;
            //pozniej za pomoca Factory tworze konkretny typ(nie musze wiedziec jaki) pojazdu i dopisuje do ArrayListy
            while (result.next()) {
                vehicle = VehicleFactory.getVehicle(result.getInt("veh_id"), result.getString("veh_marka"), result.getString("veh_name"),
                        result.getString("veh_price"), result.getBoolean("veh_avibility"), result.getString("veh_type"));
                if(vehicle != null)
                    logger.info("Vehicle: " + vehicle.toString());
                else
                    logger.info("Err Vehicle = null");
                vehiclesByType.add(vehicle);
            }
            return vehiclesByType;
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, e1);
            e1.printStackTrace();
            return null;
        }
    }

    *//**
     * metoda usuwajaca rekord z BD
     * @param vehicle
     *//*
    public void deleteVehicleFromDB(VehicleAbs vehicle) {
        logger.info("Vehicle: " + vehicle.toString());
        if(!vehicle.getAvibility()) {
            return;
        }
        ///nie moge wziac ID poniewaz przy tworzeniu kontaktu nie tworze ID - ktore jest AutoIncrementowane w BD.
        String marka = vehicle.getMarka(), name = vehicle.getName(), price = vehicle.getPrice();
        try {
            prepStmt = conn.prepareStatement("DELETE from Vehicle " +
                    "where veh_marka = '" + marka + "' AND veh_name = '" + name + "' AND veh_price = '" + price + "'");
            //dodawanie "'" zeby dla SQLa wygladalo to jako String.
            prepStmt.execute();
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, e1);
            e1.printStackTrace();
        }
    }


    *//**
     * metoda sluzaca blokowaniu wypozyczonego Vehicle.
     * @param vehicle
     *//*
    public void lendVehicle(VehicleAbs vehicle) {
        logger.info("Vehicle: " + vehicle.toString());
        if(!vehicle.getAvibility()) {
            JOptionPane.showMessageDialog(null, "Dany pojazd jest juz wypozyczony!");
            return;
        } else {
            vehicle.changeAvibilityToFalse();
        }

        ResultSet result = null;
        try {
            Integer id = vehicle.getId();
            try {
                if(id == null) {    //bezsensu?
                    logger.info("B³ad: id==null");
                    throw new NullPointerException();
                }
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            }

            prepStmt = conn.prepareStatement("UPDATE Vehicle SET veh_avibility = '0' WHERE veh_id = '" + id + "'");   //false
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    *//**
     * metoda sluzaca odblokowywaniu oddanego Vehicle.
     * @param vehicle
     *//*
    public void returnVehicle(VehicleAbs vehicle) {
        logger.info("Vehicle: " + vehicle.toString());
        ResultSet result = null;
        try {
            Integer id = vehicle.getId();
            try {
                if(id == null) {    //bezsensu?
                    logger.info("B³ad: id==null");
                    throw new NullPointerException();
                }
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            }
            vehicle.changeAvibilityToTrue();

            prepStmt = conn.prepareStatement("UPDATE Vehicle SET veh_avibility = 1 WHERE veh_id = '" + id + "'");   //true
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

}
