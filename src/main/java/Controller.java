import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class Controller {

    BTree bt;
    Means means;
    private ArrayList<YelpData> list;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    HashTable ht;
    @FXML
    TableView<YelpData> table;
    @FXML
    TableColumn<YelpData, String> businessName;
    @FXML
    TableColumn<YelpData, String> city;
    @FXML
    TableColumn<YelpData, String> lattitude;
    @FXML
    TableColumn<YelpData, String> longitude;
    @FXML
    TableView<YelpData> similarTable;
    @FXML
    TableColumn<YelpData, String> similarName;
    @FXML
    TableColumn<YelpData, String> similarCity;
    @FXML
    TableColumn<YelpData, String> SimilarLongitude;



    public void searchCluster(ActionEvent event) throws Exception {

        for ( int i = 0; i<similarTable.getItems().size(); i++) {
            similarTable.getItems().clear();
        }
        YelpData test = new YelpData(null, searchField.getText(), null, 0,0);

        BTree bt = BTree.readRoot();
        YelpData yd = bt.search(bt.root, test);


        if (yd != null){
            System.out.println("btree has this element");


            ArrayList<YelpData> clusterElements = means.getClusterFromYD(yd);

            for (YelpData y : clusterElements) {
                System.out.println(y.toString());
            }

            if (clusterElements.isEmpty()){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("No Matches Found");
                errorAlert.setContentText("There are no similar Businesses in the area");
                errorAlert.showAndWait();
            } else {
                ObservableList<YelpData> data = FXCollections.observableArrayList(clusterElements);
                //similarName.setCellValueFactory(new PropertyValueFactory<>("name"));
                similarName.setCellValueFactory(new PropertyValueFactory<YelpData, String>("name"));
                similarCity.setCellValueFactory(new PropertyValueFactory<YelpData, String>("city"));
                SimilarLongitude.setCellValueFactory(new PropertyValueFactory<YelpData, String>("categories"));
                similarTable.getItems().addAll(data);
            }



        } else {
            System.out.println("Business does not exist");

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("Business does not exist");
            errorAlert.showAndWait();
        }
    }




    public void initialize() throws Exception{

        ReadJson r = new ReadJson();

        ArrayList<YelpData> b = r.readFromJson("../business.json");
        means = Main.createClusters(b);
        bt = BTree.readRoot();


        list = b;
        final ObservableList<YelpData> data = FXCollections.observableArrayList(list);

        businessName.setCellValueFactory(
                new PropertyValueFactory<YelpData, String>("name"));

        city.setCellValueFactory(
                new PropertyValueFactory<YelpData, String>("city"));

        lattitude.setCellValueFactory(
                new PropertyValueFactory<YelpData, String>("lattitude"));

        longitude.setCellValueFactory(
                new PropertyValueFactory<YelpData, String>("longitude"));

        table.getItems().addAll(data);

        System.out.println();

    }



}