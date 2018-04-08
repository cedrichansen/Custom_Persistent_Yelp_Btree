import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {




    public static void main(String[] args) {

        Scanner kb = new Scanner(System.in);

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

            for (int i = 0; i</*169999*/ 100000; i++) {
                System.out.println("Adding item: " + i);
                bt.insert(businesses.get(i));
            }


            System.out.println("\ndone adding stuff to the btree\n");

            System.out.println("Type a business id to see if it is contained in BTree");
            String YelpDataIdInput = kb.nextLine();
            while (true) {
                YelpData searched = bt.search(bt.root,new YelpData(null, YelpDataIdInput, null, 0, 0) );
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

        // try with jUdkAoyuXqCjCjkP2zzD0w id




        } catch (Exception e) {
            e.printStackTrace();
        }



    }


}