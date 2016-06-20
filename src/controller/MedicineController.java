package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import model.Facade;
import model.core.condition.Condition;
import model.core.medicine.CommodityAbs;
import model.core.medicine.CommodityFactory;
import model.core.medicine.ECategory;

import javax.swing.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class MedicineController implements Initializable {
    private static Logger logger = Logger.getLogger(MedicineController.class.getName());
    private Facade model = Facade.getInstance();

    private boolean editFlag = false;

    List<CommodityAbs> medicines = new ArrayList<>();

    //ObservableList<Ambulance> obList2;

    ///FXML variable region
    @FXML TableView<CommodityAbs> tv_medicine;
    @FXML TableColumn<CommodityAbs,String> col_name;
    @FXML TableColumn<CommodityAbs,Integer> col_EAN;
    @FXML TableColumn<CommodityAbs,String> col_date_introduction;
    @FXML TableColumn<CommodityAbs,String> col_date_expiration;
    @FXML TableColumn<CommodityAbs,Integer> col_quantity;
    @FXML TableColumn<CommodityAbs,Integer> col_order;

    @FXML ChoiceBox<ECategory> cb_type;
    @FXML TextField tf_name, tf_ean, tf_exp_date, tf_quantity;
    @FXML Button btn_accept, btn_cancel;    //TODO btn_delete zrobi� pewne "podswietlanie" gdy jest jaka� opcja wybrana na tableView...
    //endregion


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("jestem w initialize!");

        model.initAllMedicine();    //TODO do wyodrebnienia!

        model.setType(ECategory.BUILDING);

        initTableView();
        setMedicineArray();

        /**
         * Ladowanie do ChoiceBox
         */
        List<ECategory> listCategory;
        listCategory = model.getAllCategories();
        ObservableList<ECategory> obList = FXCollections.observableList(listCategory);
        cb_type.getItems().clear();
        cb_type.setItems(obList);
        cb_type.getSelectionModel().selectFirst();

        //setAmbulanceCB();
    }

    //private method region
    private void initTableView() {
        tv_medicine.setEditable(true);
        //TODO dodac ID(najlepiej uzyc ORMa) i stworzyc modyfikacje dla nazwy.
        col_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        col_EAN.setCellValueFactory(new PropertyValueFactory<>("CodeEan"));
        col_date_introduction.setCellValueFactory(new PropertyValueFactory<>("DateIntroduction"));
        col_date_expiration.setCellValueFactory(new PropertyValueFactory<>("DateExpiration"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("Packages"));
        col_quantity.setCellFactory(TextFieldTableCell.<CommodityAbs, Integer>forTableColumn(new IntegerStringConverter()));

        ///order
        col_order.setCellValueFactory(new PropertyValueFactory<>("Sachets"));
        col_order.setCellFactory(TextFieldTableCell.<CommodityAbs, Integer>forTableColumn(new IntegerStringConverter()));
        col_order.setOnEditCommit(event -> {
            //col_quantity.setStyle("-fx-background-color: blue");
            if (!Objects.equals(event.getOldValue(), event.getNewValue())) {
                //TODO sprawdzenie czy dodalismy integer > 0
                int rowId = event.getTablePosition().getRow();  //pobranie ID wiersza ktory modifikujemy
                for (Node n : tv_medicine.lookupAll("TableRow")) {
                    if (n instanceof TableRow) {
                        TableRow row = (TableRow) n;
                        if (row.getIndex() == rowId) {
                            row.setStyle("-fx-background-color: blue; ");    //TODO jak zmienic kolor tylko tekstu do znalezienia
                            //row.setStyle("-fx-text-inner-color: blue;");
                            //TODO do uporzadkowania i przenisienia do metody private
                            String nameOfColumn = event.getTableColumn().getText();
                            int newValue = event.getNewValue();
                            Condition con = tv_medicine.getSelectionModel().getSelectedItem().getCondition();
                            Integer medID = tv_medicine.getSelectionModel().getSelectedItem().getMedID();
                            //zmien nie na trwalo liste w Facade ale nie update do BD:
                            model.updateTempCommodityArray(medID, newValue, con);
                            //reload widoku:
                            setMedicineArray();
                            logger.info("Zamieniono tymczasowo");
                            break;
                        }
                    }
                }
            }
        });
    }

    private void setMedicineArray() {
        medicines = model.getAllMedicineByType();
        ObservableList<CommodityAbs> dataMedicine = FXCollections.observableArrayList(medicines);
        tv_medicine.setItems(dataMedicine);
    }

    private boolean validateIntegers(List<String> texts) {
        for (String text : texts) {
            if (!(text.matches("\\d*")))
                return true;
        }
        return false;
    }

    private void resetAddSettings() {
        cb_type.getSelectionModel().selectFirst();
        tf_name.clear(); tf_ean.clear(); tf_exp_date.clear(); tf_quantity.clear();
    }
    //endregion


    //FXML method region
    @FXML public void btnShowBuilding() {
        model.setType(ECategory.BUILDING);
        setMedicineArray();
    }

    @FXML public void btnShowElectronic() {
        model.setType(ECategory.ELECTRONIC);
        setMedicineArray();
    }

    @FXML public void btnShowOthers() {
        model.setType(ECategory.OTHERS);
        setMedicineArray();
    }

    @FXML public void btnAddResources() {
        /*ArrayList<String> atributes = new ArrayList<>();
        if(validateIntegers(Arrays.asList(tf_quantity.getText()))) {
            JOptionPane.showMessageDialog(null, "Please set an Integer into column \"Quantity\"");
            resetAddSettings();
            tf_quantity.setPromptText("Only Integer!");
            return;
        }
        atributes.add(tf_quantity.getText());
        atributes.add(tf_name.getText());
        atributes.add(tf_exp_date.getText());
        atributes.add(tf_ean.getText());
        ECategory category = cb_type.getValue();

        logger.info(atributes.toString());

        if(atributes.size() != 0) { //nie wypelnione ZADNE pole.
            for (String string : atributes) {   //sprawdzam czy wypelnione WSZYSTKIE pola
                if (string.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all sections");
                    return;
                }
            }
            Condition con = new Condition(Integer.valueOf(atributes.get(0)));
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
            CommodityAbs medicine = CommodityFactory.getMedicine(category, atributes.get(3), atributes.get(4), ft.format(new Date()), Integer.valueOf(atributes.get(5)), con);
            logger.info("Medicine: " + medicine.toString());
            //DB
            model.setType(category);
            model.insertCommodityToDB(medicine);
            //VIEW
            setMedicineArray();
            //flag = cb_type.getValue(); //TODO
            //changeToAddByType(flag);

        } else {
            JOptionPane.showMessageDialog(null, "Please fill all sections");
            return;
        }*/

    }

    @FXML public void btnAccept() {
        /*logger.info("proba buttona btnDeleteMedicine");
        final int selectedIdx = tv_medicine.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            CommodityAbs medicineToRemove = tv_medicine.getSelectionModel().getSelectedItem();
            final int newSelectedIdx = (selectedIdx == tv_medicine.getItems().size()-1) ? selectedIdx : selectedIdx;

            //usun z BD:
            model.deleteMedicineFromDB(medicineToRemove);
            //reload widoku:
            setMedicineArray();
            logger.info("Pousuwane");
            tv_medicine.getSelectionModel().select(newSelectedIdx);
        }*/
    }

    private void cleanBackgroundTableView() {
        for (Node n : tv_medicine.lookupAll("TableRow")) {
            if (n instanceof TableRow) {
                TableRow row = (TableRow) n;
                row.setStyle("-fx-background-color: white; ");
            }
        }
    }

    @FXML public void btnCancel() {
        model.initAllMedicine();
        //initTableView();
        setMedicineArray();
        //cleanBackgroundTableView();
    }
    //endregion

}


//przydatny kod do editable TableColumn:
// SETTING THE CELL FACTORY FOR THE RATINGS COLUMN
/*rating.setCellFactory(new Callback<TableColumn<Music,Integer>,TableCell<Music,Integer>>(){
@Override
public TableCell<Music, Integer> call(TableColumn<Music, Integer> param) {
        TableCell<Music, Integer> cell = new TableCell<Music, Integer>(){
@Override
public void updateItem(Integer item, boolean empty) {
        if(item!=null){

        ChoiceBox choice = new ChoiceBox(ratingSample);
        choice.getSelectionModel().select(ratingSample.indexOf(item));
        //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
        setGraphic(choice);
        }
        }
        };
        return cell;
        }
        });*/


