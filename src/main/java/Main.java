import java.util.ArrayList;

public class Main{

    public static void main(String [] args) {

        ReadJson rj = new ReadJson();
        ArrayList<YelpData> businesses = rj.readFromJson("business.json");



    }


}