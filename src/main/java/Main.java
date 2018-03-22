import java.io.*;
import java.util.ArrayList;


public class Main {

    public static void write(String filename, ArrayList<YelpData> list) {
        try {
            FileOutputStream fo = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fo);

            for (YelpData y : list) {
                out.writeObject(y);


            }
            fo.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("finished writing to file");
    }

    public static ArrayList<YelpData> read(String filename) {

        ArrayList<YelpData> objectsRead = new ArrayList<YelpData>();

        try {
            FileInputStream fi = new FileInputStream(filename);
            ObjectInputStream os = new ObjectInputStream(fi);



            YelpData a = (YelpData)os.readObject();
            System.out.println("just read: " + a.toString());
            objectsRead.add(a);


            fi.close();
            os.close();

        } catch (Exception e) {
                e.printStackTrace();
        }

    return objectsRead;

    }


    public static void main(String[] args) {

        ReadJson rj = new ReadJson();
        ArrayList<YelpData> businesses = rj.readFromJsonSmall("business.json");

        //write("keys.txt", businesses);
        //read("keys.txt");

    }


}