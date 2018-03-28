import java.io.*;
import java.util.ArrayList;


public class Main {




    public static void main(String[] args) {

        try {
            String yelpFile = "business.json";
            ReadJson rj = new ReadJson();
           // ArrayList<YelpData> businesses = rj.readFromJsonSmall(yelpFile);
            HashTable ht = rj.readToHash(yelpFile);

            System.out.println("done reading to the hashtable");
            System.out.println("current ht size is: " + ht.table.length);
            System.out.println();

            BTree bt = new BTree();
            bt.insert(ht.table[3].get(0).hashCode());


            System.out.println("done adding stuff to the btree");






        } catch (Exception e) {
            e.printStackTrace();
        }



    }


}