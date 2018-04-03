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

            for (int i = 0; i<170000; i++) {
                System.out.println("Adding item: " + i);
                bt.insert(businesses.get(i).hashCode());
            }


            System.out.println("\ndone adding stuff to the btree\n");

            System.out.println("Type a business id to see if it is contained in BTree");
            String command = kb.nextLine();
            while (true) {
                YelpData temp = new YelpData(null, command, null, 0, 0);
                if (bt.contains(bt.root, temp.hashCode())) {
                    System.out.println("yayyy found something");
                    System.out.println("Type a business id to see if it is contained in BTree");
                    command = kb.nextLine();
                } else {
                    System.out.println("this id doesnt exist");
                    System.out.println("Type a business id to see if it is contained in BTree");
                    command = kb.nextLine();
                }
            }






        } catch (Exception e) {
            e.printStackTrace();
        }



    }


}