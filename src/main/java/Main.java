import java.io.*;
import java.util.ArrayList;


public class Main {




    public static void main(String[] args) {

        try {
            String yelpFile = "../business.json";
            ReadJson rj = new ReadJson();
            ArrayList<YelpData> businesses = new ArrayList<YelpData>();
            HashTable ht = rj.readToHash(yelpFile);

            for (int i = 0; i<ht.table.length; i++) {
                if (ht.table[i] != null) {
                    for (int j = 0; j<ht.table[i].size(); j++) {
                        businesses.add(ht.table[i].get(j));
                    }
                }
            }

            System.out.println("done reading to the hashtable");
            System.out.println("current ht size is: " + ht.table.length);
            System.out.println();

            BTree bt = new BTree();
            //bt.insert(ht.table[3].get(0).hashCode());

            for (int i = 0; i<169999; i++) {
                System.out.println("Adding item: " + i);
                bt.insert(businesses.get(i).hashCode());
            }


            System.out.println("done adding stuff to the btree");






        } catch (Exception e) {
            e.printStackTrace();
        }



    }


}