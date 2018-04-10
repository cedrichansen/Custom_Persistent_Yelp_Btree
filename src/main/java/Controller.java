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
    TableColumn<YelpData,String> id;

    @FXML
    TableView<YelpData> similarTable;
    @FXML
    TableColumn<YelpData, String> similarName;
    @FXML
    TableColumn<YelpData, String> similarCity;
    @FXML
    TableColumn<YelpData, String> SimilarLongitude;
    @FXML
    TableColumn <YelpData, String> SimilarLattitude;

    @FXML
    TableView<YelpData> mostSimilarTable;
    @FXML
    TableColumn<YelpData, String> mostSimilarName;
    @FXML
    TableColumn<YelpData, String> mostSimilarCity;
    @FXML
    TableColumn<YelpData, String> mostSimilarLattitude;
    @FXML
    TableColumn<YelpData, String> mostSimilarLongitude;





    public void searchCluster(ActionEvent event) throws Exception {

        for ( int i = 0; i<similarTable.getItems().size(); i++) {
            similarTable.getItems().clear();
        }
        YelpData test = new YelpData(null, table.getSelectionModel().getSelectedItem().id, null, 0,0);

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
                SimilarLattitude.setCellValueFactory(new PropertyValueFactory<YelpData, String>("lattitude"));
                SimilarLongitude.setCellValueFactory(new PropertyValueFactory<YelpData, String>("longitude"));
                similarTable.getItems().addAll(data);


                Point p = new Point(yd.lattitude, yd.longitude, yd);
                Point closest = means.getClosestPoint(p);

                ArrayList<YelpData> closestPoint = new ArrayList<YelpData>();
                closestPoint.add(closest.yd);
                ObservableList<YelpData> close = FXCollections.observableArrayList(closestPoint);

                mostSimilarName.setCellValueFactory(new PropertyValueFactory<YelpData, String>("name"));
                mostSimilarCity.setCellValueFactory(new PropertyValueFactory<YelpData, String>("city"));
                mostSimilarLattitude.setCellValueFactory(new PropertyValueFactory<YelpData, String>("lattitude"));
                mostSimilarLongitude.setCellValueFactory(new PropertyValueFactory<YelpData, String>("longitude"));
                mostSimilarTable.getItems().addAll(close);

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

        id.setCellValueFactory(
                new PropertyValueFactory<YelpData, String>("id")
        );

        lattitude.setCellValueFactory(
                new PropertyValueFactory<YelpData, String>("lattitude"));

        longitude.setCellValueFactory(
                new PropertyValueFactory<YelpData, String>("longitude"));

        table.getItems().addAll(data);

        System.out.println();

    }



}