package controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Marcin on 2016-01-22.
 */
public class AmbulanceController implements Initializable {

    /*Stage prevStage;
    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }*/


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JOptionPane.showMessageDialog(null, "whats UP?");

    }

    public void btnChangeScene(Event event) {
        JOptionPane.showMessageDialog(null, "co ja robie tu oooo?");
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("../view/medicine.fxml"));
            Pane myPane = (Pane) myLoader.load();
            Scene scene = new Scene(myPane);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            /*Stage stage = new Stage();
            stage.setTitle("Medicaments");
            Pane myPane = null;
            myPane = FXMLLoader.load(getClass().getResource("../view/medicine.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);

            prevStage.close();

            stage.show();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
