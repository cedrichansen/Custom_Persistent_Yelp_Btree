import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class BTree implements java.io.Serializable {

    static final String btreeFile = "Btree"; //file to keep track of B tree root and constants
    static final int K = 8;  //constant used throughout the entire Btree
    static final int nodeSize = 4095; // to keep track of where different nodes are in the file
    Node root;
    int totalNumberOfNodes;

    class Node implements java.io.Serializable {
        int currentNumberOfChildren;
        int currentNumberOfKeys;
        YelpData[] keys;
        Long[] children;
        int leaf; // 1 is leaf, 0 is not leaf
        long nodeID;


        public Node(long id) {
            nodeID = id;
            currentNumberOfChildren = 0;
            currentNumberOfKeys = 0;
            keys = new YelpData[2 * K - 1];
            children = new Long[2 * K];
        }

    }

    BTree() throws Exception {

        totalNumberOfNodes = 0;
        Node temp = new Node(totalNumberOfNodes);
        temp.leaf = 1;
        temp.currentNumberOfKeys = 0;
        write(temp);
        root = temp;
    }

    //recursive function to look for keys in the tree
    YelpData search(Node n, YelpData yd) throws Exception {
        int i = 0;

        while (i < n.currentNumberOfKeys && yd.hashCode() > n.keys[i].hashCode()) {
            i++;
        }

        if (i <= n.currentNumberOfKeys && yd.hashCode() == n.keys[i].hashCode()) {
            return n.keys[i];
        } else if (n.leaf == 1) { //if not found and its a leaf, then key does not exist
            return null;
        } else {
            //search the appropriate child node and search it
            return search(read(n.children[i]), yd);
        }
    }

    boolean contains(Node n, /*int x*/ YelpData yd) throws Exception{
        int i = 0;

        //find where the yelpdata would be in the tree
        while (i < n.currentNumberOfKeys && yd.hashCode() > n.keys[i].hashCode()) {
            i++;
        }

        //just look to see if id matches (easier to search that way)
        if (i <= n.currentNumberOfKeys && yd.hashCode() == n.keys[i].hashCode()) {
            return true;
        } else if (n.leaf == 1) { //if not found and its a leaf, then key does not exist
            return false;
        } else {
            //search the appropriate child node and search it
            return contains(read(n.children[i]), yd);
        }
    }


    void insert(/*int k*/ YelpData yd) throws Exception {
        Node r = root;
        if (root.currentNumberOfKeys == 2 * K - 1) {
            totalNumberOfNodes++;
            Node s = new Node(totalNumberOfNodes);
            root = s;
            s.leaf = 0;
            s.currentNumberOfKeys = 0;
            s.children[0] = r.nodeID;
            s.currentNumberOfChildren++;
            split(s, r);
            insertNotFull(s, yd);
        } else {
            insertNotFull(r, yd);
        }
    }


    void insertNotFull(Node x, /*int k*/ YelpData yd) throws Exception {

        int i = x.currentNumberOfKeys-1;
        if (x.leaf == 1) {
            while (i > -1 && yd.hashCode() < x.keys[i].hashCode()) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }

            i++;
            x.keys[i] = yd;
            x.currentNumberOfKeys++;
            write(x);
        } else {
            while (i > -1 && yd.hashCode() < x.keys[i].hashCode()) { //find appropriate spot
                i--;
            }

            i++;
            Node temp = read(x.children[i]);

            if (temp.currentNumberOfKeys == (2 * K - 1)) {
                split(x, temp);
                if (yd.hashCode() > x.keys[i].hashCode()) {
                    temp = read(x.children[i + 1]);
                }
            }

            insertNotFull(temp, yd);
        }
    }


    void split(Node x, Node y) throws IOException {
        totalNumberOfNodes++;
        Node z = new Node(totalNumberOfNodes);
        z.leaf = y.leaf;
        for (int i = 0; i < K - 1; i++) { //move second half of y's keys to to first half of z's keys
            z.keys[i] = y.keys[i + K];
            z.currentNumberOfKeys++; //just added a key, increment numKeys
            y.keys[i + K] = null; ///this line might give some weird errors - for just keys it was y.keys[i + K] = 0
            y.currentNumberOfKeys--;
        }

        if (y.leaf == 0) {
            //move second half of y's pointers to be first half of z's pointers
            for (int i = 0; i < K; i++) {
                z.children[i] = y.children[i + K];
                z.currentNumberOfChildren++;
                y.children[i + K] = null;
                y.currentNumberOfChildren--;
            }
        }

        // Z node can never point at 0
        int index = x.currentNumberOfKeys - 1;
        while (index > -1 && y.keys[K - 1].hashCode() < x.keys[index].hashCode()) {
            x.keys[index + 1] = x.keys[index];
            index--;
        }

        index++;
        x.keys[index] = y.keys[K - 1];
        x.currentNumberOfKeys++;
        y.keys[K - 1] = null; /// might give an error also -- used to be y.keys[K - 1] = 0
        y.currentNumberOfKeys--;


        int index2 = x.currentNumberOfChildren - 1;
        while (index2 > index) {
            x.children[index2 + 1] = x.children[index2];
            index2--;
        }

        index2++;
        x.children[index2] = z.nodeID;
        x.currentNumberOfChildren++;

        write(x);
        write(y);
        write(z);

    }


    void write(Node n) throws IOException{
        try{
            RandomAccessFile file = new RandomAccessFile(btreeFile, "rw");
            file.seek(n.nodeID*nodeSize);
            FileChannel fc = file.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(nodeSize);

            bb.putInt(n.leaf);
            bb.putLong(n.nodeID);

            bb.putInt(n.currentNumberOfKeys);
            for (int i = 0; i<n.currentNumberOfKeys; i++){

                YelpData current = n.keys[i];

                //write the name of the current yelp object
                byte [] name = current.name.getBytes();
                bb.putInt(name.length);
                bb.put(name);

                //write the id of current yelpObject
                byte [] id = current.id.getBytes();
                bb.putInt(id.length);
                bb.put(id);

                //write city
                byte [] city = current.city.getBytes();
                bb.putInt(city.length);
                bb.put(city);

                //add lat and long
                bb.putDouble(current.lattitude);
                bb.putDouble(current.longitude);


                String [] categories = new String [current.categories.size()];
                current.categories.toArray(categories);
                bb.putInt(current.numCategories);

                // add categories for each yelpdata item
                for (int j = 0; j<current.numCategories; j++){
                    byte[] catBytes = current.categories.get(j).getBytes();
                    bb.putInt(catBytes.length);
                    bb.put(catBytes);
                }

            }

            bb.putInt(n.currentNumberOfChildren);
            for (int i = 0; i<n.currentNumberOfChildren; i++){
                bb.putLong(n.children[i]);
            }


            bb.flip();
            fc.write(bb);
            bb.clear();
            fc.close();
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //returns a node full of yelpData objects
    Node read(long x) throws Exception{
        try {

            Node temp = new Node(0);
            RandomAccessFile file = new RandomAccessFile(btreeFile, "rw");
            file.seek(x*nodeSize);
            FileChannel fc = file.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(nodeSize);
            fc.read(bb);
            bb.flip();
            temp.leaf = bb.getInt();
            temp.nodeID = bb.getLong();

            temp.currentNumberOfKeys = bb.getInt(); //recover keys
            for (int i = 0; i< temp.currentNumberOfKeys; i++) {
                //recover all info that was written for every single
                // yelpdate iobject that was puyt ibn the nodes
                //make sure to refcord the number of byte in the byte buffer so i know
                //when to stop reading and not to get an overflow
               //YelpData yd = new YelpData(null, null, null, 0, 0); // crashes because itll try to do math.abs(null) for int hash


                //read name
                int nameLen = bb.getInt();
                byte [] nameBuf = new byte[nameLen];
                bb.get(nameBuf);
                String name = new String(nameBuf);

                //read id
                int idLen = bb.getInt();
                byte [] idBuf = new byte[idLen];
                bb.get(idBuf);
                String id = new String(idBuf);

                //get the city
                int cityLen = bb.getInt();
                byte [] cityBuf = new byte[cityLen];
                bb.get(cityBuf);
                String city = new String(cityBuf);

                Double lattitude = bb.getDouble();
                Double longitude = bb.getDouble();

                int numCategories = bb.getInt();
                ArrayList<String> categories = new ArrayList<String>();

                //get all of the categories
                for (int k = 0; k<numCategories; k++){
                    int currCatLen = bb.getInt();
                    byte [] catBuf = new byte[currCatLen];
                    bb.get(catBuf);
                    categories.add(new String(catBuf));
                }

                //once everything has been read from the file, add the object to the node
                YelpData yd = new YelpData(name, id, city, lattitude, longitude);
                yd.categories = categories;
                temp.keys[i] = yd;
            }

            temp.currentNumberOfChildren = bb.getInt(); //recover children
            for (int i = 0; i<temp.currentNumberOfChildren; i++){
                temp.children[i] = bb.getLong();
            }

            bb.clear();
            fc.close();
            file.close();
            return temp;

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
