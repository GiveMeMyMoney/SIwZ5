package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import model.Facade;
import model.core.condition.Condition;
import model.core.medicine.CommodityAbs;
import model.core.medicine.ECategory;

import javax.swing.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class ResourcesController implements Initializable {
    private static Logger logger = Logger.getLogger(ResourcesController.class.getName());
    private Facade model = Facade.getInstance();

    List<CommodityAbs> commodities = new ArrayList<>();

    ///FXML variable region
    @FXML TableView<CommodityAbs> tv_commodity;
    @FXML TableColumn<CommodityAbs,String> col_name;
    @FXML TableColumn<CommodityAbs,Integer> col_EAN;
    @FXML TableColumn<CommodityAbs,String> col_date_introduction;
    @FXML TableColumn<CommodityAbs,String> col_date_expiration;
    @FXML TableColumn<CommodityAbs,Integer> col_quantity;
    @FXML TableColumn<CommodityAbs,Integer> col_order;

    @FXML ChoiceBox<ECategory> cb_type;
    @FXML TextField tf_name, tf_ean, tf_exp_date, tf_quantity;
    @FXML Text tf_category;
    @FXML Button btn_accept, btn_cancel;    //TODO btn_delete zrobi� pewne "podswietlanie" gdy jest jaka� opcja wybrana na tableView...
    //endregion


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("jestem w initialize!");

        model.initAllCommodities();    //TODO do wyodrebnienia!

        model.setType(ECategory.BUILDING);

        initTableView();
        setCommodityArray();

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
        tf_category.setText(model.getType());
        tv_commodity.setEditable(true);
        //TODO dodac ID(najlepiej uzyc ORMa) i stworzyc modyfikacje dla nazwy.
        col_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        col_EAN.setCellValueFactory(new PropertyValueFactory<>("CodeEan"));
        col_date_introduction.setCellValueFactory(new PropertyValueFactory<>("DateIntroduction"));
        col_date_expiration.setCellValueFactory(new PropertyValueFactory<>("DateExpiration"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

        ///order
        col_order.setCellValueFactory(new PropertyValueFactory<>("Order"));
        col_order.setCellFactory(TextFieldTableCell.<CommodityAbs, Integer>forTableColumn(new IntegerStringConverter()));
        col_order.setOnEditCommit(event -> {
            //col_quantity.setStyle("-fx-background-color: blue");
            if (!Objects.equals(event.getOldValue(), event.getNewValue())) {
                //TODO sprawdzenie czy dodalismy integer > 0
                int rowId = event.getTablePosition().getRow();  //pobranie ID wiersza ktory modifikujemy
                for (Node n : tv_commodity.lookupAll("TableRow")) {
                    if (n instanceof TableRow) {
                        TableRow row = (TableRow) n;
                        if (row.getIndex() == rowId) {
                                //TODO jak zmienic kolor tylko tekstu do znalezienia
                            //row.setStyle("-fx-text-inner-color: blue;");
                            //TODO do uporzadkowania i przenisienia do metody private
                            //String nameOfColumn = event.getTableColumn().getText();
                            int newValue = event.getNewValue();
                            Condition con = tv_commodity.getSelectionModel().getSelectedItem().getCondition();
                            if (newValue > con.getQuantity()) {
                                JOptionPane.showMessageDialog(null, "YOU DONT HAVE SO MUCH QUANTITY OF THIS PRODUCT");
                                initTableView();
                            } else {
                                row.setStyle("-fx-background-color: #9fddff; ");
                                Integer conID = tv_commodity.getSelectionModel().getSelectedItem().getComID();
                                //zmien nie na trwalo liste w Facade ale nie update do BD:
                                model.updateTempCommodityArray(conID, newValue, con);
                                //reload widoku:
                                setCommodityArray();
                            }

                            logger.info("Zamieniono tymczasowo");
                            break;
                        }
                    }
                }
            }
        });
    }

    private void setCommodityArray() {
        commodities = model.getAllMedicineByType();
        ObservableList<CommodityAbs> dataCommodities = FXCollections.observableArrayList(commodities);
        tv_commodity.setItems(dataCommodities);
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
        setCommodityArray();
        initTableView();
    }

    @FXML public void btnShowElectronic() {
        model.setType(ECategory.ELECTRONIC);
        setCommodityArray();
        initTableView();
    }

    @FXML public void btnShowOthers() {
        model.setType(ECategory.OTHERS);
        setCommodityArray();
        initTableView();
    }

    @FXML public void btnAddResources() {
        JOptionPane.showMessageDialog(null, "HERE YOU CAN ADD SOME RESOURCES IF YOU DONT SEE THEM IN THE LIST :)");
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
            CommodityAbs medicine = CommodityFactory.getCommodity(category, atributes.get(3), atributes.get(4), ft.format(new Date()), Integer.valueOf(atributes.get(5)), con);
            logger.info("Medicine: " + medicine.toString());
            //DB
            model.setType(category);
            model.insertCommodityToDB(medicine);
            //VIEW
            setCommodityArray();
            //flag = cb_type.getValue(); //TODO
            //changeToAddByType(flag);

        } else {
            JOptionPane.showMessageDialog(null, "Please fill all sections");
            return;
        }*/

    }

    @FXML public void btnAccept() {
        JOptionPane.showMessageDialog(null, "WELL DONE! YOU HAVE CHOOSE YOUR COMMODITIES :)");
        /*logger.info("proba buttona btnDeleteMedicine");
        final int selectedIdx = tv_commodity.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            CommodityAbs medicineToRemove = tv_commodity.getSelectionModel().getSelectedItem();
            final int newSelectedIdx = (selectedIdx == tv_commodity.getItems().size()-1) ? selectedIdx : selectedIdx;

            //usun z BD:
            model.deleteMedicineFromDB(medicineToRemove);
            //reload widoku:
            setCommodityArray();
            logger.info("Pousuwane");
            tv_commodity.getSelectionModel().select(newSelectedIdx);
        }*/
    }

    private void cleanBackgroundTableView() {
        for (Node n : tv_commodity.lookupAll("TableRow")) {
            if (n instanceof TableRow) {
                TableRow row = (TableRow) n;
                row.setStyle("-fx-background-color: white; ");
            }
        }
    }

    @FXML public void btnCancel() {
        model.initAllCommodities();
        //initTableView();
        setCommodityArray();
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


