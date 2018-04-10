import java.util.ArrayList;
import java.util.Scanner;


public class Main {

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






    public static void main(String[] args) {

        Scanner kb = new Scanner(System.in);

        try {

            // this line does the writing of all of the btree objects upon initial write

            //makeTree();

            ArrayList<YelpData> b = new ReadJson().readFromJson("../business.json");
            Means means = createClusters(b);

            BTree bt2 = BTree.readRoot();

            System.out.println("Type a business id to see if it is contained in BTree");
            String YelpDataIdInput = kb.nextLine();
            while (true) {
                YelpData searched = bt2.search(bt2.root, new YelpData(null, YelpDataIdInput, null, 0, 0));
                if (searched != null) {
                    System.out.println(searched.toString() + "\n");
                    System.out.println("Other items in the same cluster: ");
                    ArrayList<YelpData> sameCluster = means.getClusterFromYD(searched);
                    System.out.println("\nType a business id to see if it is contained in BTree");
                    YelpDataIdInput = kb.nextLine();
                } else {
                    System.out.println("this id doesnt exist");
                    System.out.println("Type a business id to see if it is contained in BTree");
                    YelpDataIdInput = kb.nextLine();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}