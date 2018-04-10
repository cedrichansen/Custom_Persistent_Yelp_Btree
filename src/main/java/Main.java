import javafx.application.Application;

import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application{

    static void makeTree() {
        try {

            String yelpFile = "../business.json";
            ReadJson rj = new ReadJson();
            ArrayList<YelpData> businesses = rj.readFromJson(yelpFile);
            BTree bt = new BTree();

            for (int i = 0; i < businesses.size() /*100000*/; i++) {
                System.out.println("Adding item: " + i);
                bt.insert(businesses.get(i));
            }
                bt.writeRoot();

                System.out.println("\ndone adding stuff to the btree\n");

            } catch(Exception e){
                e.printStackTrace();
            }
        }


        static Means createClusters(ArrayList<YelpData> list){
            Means kMeans = new Means();

            for (YelpData yd: list){
                Point p = new Point(yd.lattitude, yd.longitude, yd);
                kMeans.addPoint(p);
            }

            kMeans.initializeMeans();
            kMeans.calculate();
            return kMeans;

        }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("screen.fxml"));
        primaryStage.setTitle("CSC365 Hw02");
        primaryStage.setScene(new Scene(root, 1200, 771));
        primaryStage.show();
    }




    public static void main(String[] args) {

        //Scanner kb = new Scanner(System.in);

        try {

            // this line below does the writing of all of the btree objects upon initial write
            //makeTree();

            launch(args);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}