import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;



class ReadJson {


    public HashTable readToHash(String filename){
        HashTable ht = new HashTable(512);

        try {

            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String line = "";

            for (int i = 0; i<170000; i++) {
                //get each line and make a json object from the line
                line = br.readLine();
                org.json.JSONObject jobj = new org.json.JSONObject(line);

                //get relevant data and create new java object
                YelpData yd = new YelpData(jobj.get("name").toString(), jobj.get("business_id").toString(),
                        jobj.get("city").toString(),(float)jobj.getDouble("latitude"),
                        (float)jobj.getDouble("longitude"));

                //get json categories and put them in newly created java object
                List <String> ca = new ArrayList<String>();

                JSONArray jarr = new JSONArray(jobj.getJSONArray("categories").toString());

                //add json category array to our categories arraylist
                for (int j =0; j<jarr.length(); j++) {
                    ca.add(jarr.get(j).toString());
                }

                //add categories from json to object
                yd.categories.addAll(ca);
                yd.numCategories = yd.categories.size();
                //print contents of object
                //System.out.println(yd.toString());

                if ((float)(i+1)/(float)ht.table.length <= 0.75) {
                    ht.add(yd,ht);
                } else {
                    ht = ht.resize();
                    ht.add(yd,ht);
                }
                //ht.add(yd,ht);

                System.out.println(yd.toString());

            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ht;
    }


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
