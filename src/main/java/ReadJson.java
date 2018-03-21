import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;



class ReadJson {

    public ArrayList<YelpData> readFromJson(String filename){
        ArrayList<YelpData> businesses = new ArrayList<YelpData>();

        try{

            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String line = "";

            for (int i = 0; i< 174566; i++){
                line = br.readLine();
                org.json.JSONObject jobj = new org.json.JSONObject(line);

                YelpData yd = new YelpData(jobj.get("name").toString(), jobj.get("business_id").toString(),
                      jobj.get("city").toString(), jobj.getDouble("latitude"), jobj.getDouble("longitude"));

                //add categories for each business
                List <String> categories = new ArrayList<String>();
                JSONArray jarr = new JSONArray(jobj.getJSONArray("categories").toString());
                for (int j =0; j<jarr.length(); j++) {
                    categories.add(jarr.get(j).toString());
                }
                yd.categories.addAll(categories);
                System.out.println(i + " HashCode: " + yd.hashCode() + " " + yd.toString());
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return businesses;
    }




    public ArrayList<YelpData> readFromJsonSmall(String filename){
        ArrayList<YelpData> businesses = new ArrayList<YelpData>();

        try{

            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String line = "";

            for (int i = 0; i< 1000; i++){
                line = br.readLine();
                org.json.JSONObject jobj = new org.json.JSONObject(line);

                YelpData yd = new YelpData(jobj.get("name").toString(), jobj.get("business_id").toString(),
                        jobj.get("city").toString(), jobj.getDouble("latitude"), jobj.getDouble("longitude"));

                //add categories for each business
                List <String> categories = new ArrayList<String>();
                JSONArray jarr = new JSONArray(jobj.getJSONArray("categories").toString());
                for (int j =0; j<jarr.length(); j++) {
                    categories.add(jarr.get(j).toString());
                }
                yd.categories.addAll(categories);
                System.out.println(i + " HashCode: " + yd.hashCode() + " " + yd.toString());
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return businesses;
    }

}
