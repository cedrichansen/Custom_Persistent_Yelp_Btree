import java.io.*;
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
            } catch(Exception e){
                e.printStackTrace();
            }
        }


    public static void main(String[] args) {

        Scanner kb = new Scanner(System.in);

        try {

            // this line does the writing of all of the btree objects upon initial write
            //makeTree();

            BTree bt2 = BTree.readRoot();


            System.out.println("\ndone adding stuff to the btree\n");

            System.out.println("Type a business id to see if it is contained in BTree");
            String YelpDataIdInput = kb.nextLine();
            while (true) {
                YelpData searched = bt2.search(bt2.root, new YelpData(null, YelpDataIdInput, null, 0, 0));
                if (searched != null) {
                    System.out.println("yayyy found something\n");
                    System.out.println(searched.toString() + "\n");
                    System.out.println("Type a business id to see if it is contained in BTree");
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