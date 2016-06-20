package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//

public class Main extends Application {
    private final static int HEIGHT = 703;
    private final static int WIDTH = 837;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("medicine.fxml"));
        primaryStage.setTitle("JanuarySOFT");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();


        /*FXMLLoader myLoader = new FXMLLoader(getClass().getResource("medicine.fxml"));
        Pane myPane = (Pane) myLoader.load();
        //AmbulanceController controller = (AmbulanceController) myLoader.getController();

        //controller.setPrevStage(primaryStage);

        //Parent rootAmbulance = FXMLLoader.load(getClass().getResource("ambulance.fxml"));
        //Parent rootMedicine = FXMLLoader.load(getClass().getResource("medicine.fxml"));

        Scene scene = new Scene(myPane, WIDTH, HEIGHT);
        //Scene scene2 = new Scene(rootMedicine, WIDTH, HEIGHT);

        primaryStage.setTitle("Wirtualna Apteczka");
        primaryStage.setScene(scene);
        primaryStage.show();*/
    }


    public static void main(String[] args) {
        launch(args);
        /*Date dateNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, 14); // Adds 15 days
        Date date = c.getTime();

        System.out.println(ft.format(date));
        System.out.println(date.toString());
        MedicineAbs medicine = MedicineFactory.getMedicine(ECategory.PAINKILLER, "Ibuprofen", ft.format(date), ft.format(dateNow), "To je zaje bandaz", 1231231231);

        System.out.println(medicine.toString());

        Ambulance ambulance = new Ambulance(1, "KTT 60SM", "A4", "Audi");*/

        //Facade model = Facade.getInstance();
        //model.insertAmbulanceToDB(ambulance);

        //model.insertHospitalToDB(hospital);
        //model.setAmbulanceID(1);
        //model.setType(ECategory.DRESSING);

        /*List<MedicineAbs> medicines = new ArrayList<>();
        medicines = model.selectAllMedicineFromDB();

        //System.out.println(medicines.get(0).toString());
        JOptionPane.showMessageDialog(null, medicines.toString());

        List<Ambulance> ambulances = new ArrayList<>();
        ambulances = model.initAllAmbulance();

        System.out.println(ambulances.toString());*/

        //System.out.println(model.getAllCategories());


    }
}
