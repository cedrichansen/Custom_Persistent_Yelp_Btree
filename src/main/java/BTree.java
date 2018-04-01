import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BTree implements java.io.Serializable {

    static final String btreeFile = "Btree"; //file to keep track of B tree root and constants
    static final int K = 8;  //constant used throughout the entire Btree
    static final int nodeSize = 1000; // to keep track of where different nodes are in the file
    Node root;
    int totalNumberOfNodes;

    class Node implements java.io.Serializable {
        int currentNumberOfChildren;
        int currentNumberOfKeys;
        int[] keys;
        Long[] children;
        int leaf; // 1 is leaf, 0 is not leaf
        long nodeID;


        public Node(long id) {
            nodeID = id;
            currentNumberOfChildren = 0;
            currentNumberOfKeys = 0;
            keys = new int[2 * K - 1];
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
    Integer search(Node n, int x) throws Exception {
        int i = 0;

        while (i < n.currentNumberOfKeys && x > n.keys[i]) {
            i++;
        }

        if (i <= n.currentNumberOfChildren && x == n.keys[i]) {
            return n.keys[i];
        } else if (n.leaf == 1) { //if not found and its a leaf, then key does not exist
            return null;
        } else {
            //search the appropriate child node
            return search(read(n.children[i]), x);
        }
    }


    void insert(int k) throws Exception {
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
            insertNotFull(s, k);
        } else {
            insertNotFull(r, k);
        }
    }


    void insertNotFull(Node x, int k) throws Exception {

        int i = x.currentNumberOfKeys-1;
        if (x.leaf == 1) {
            while (i > -1 && k < x.keys[i]) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }

            i++;
            x.keys[i] = k;
            x.currentNumberOfKeys++;
            write(x);
        } else {
            while (i > -1 && k < x.keys[i]) { //find appropriate spot
                i--;
            }

            i++;
            Node temp = read(x.children[i]); // figure out why children arent being added for a certain node

            if (temp.currentNumberOfKeys == (2 * K - 1)) {
                split(x, temp);
                if (k > x.keys[i]) {
                    temp = read(x.children[i + 1]);
                }
            }

            insertNotFull(temp, k);
        }
    }


    void split(Node x, Node y) throws IOException {
        totalNumberOfNodes++;
        Node z = new Node(totalNumberOfNodes);
        z.leaf = 0;
        for (int i = 0; i < K - 1; i++) { //move second half of y's keys to to first half of z's keys
            z.keys[i] = y.keys[i + K];
            z.currentNumberOfKeys++; //just added a key, increment numKeys
            y.keys[i + K] = 0;
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
        while (index > -1 && y.keys[K - 1] < x.keys[index]) {
            x.keys[index + 1] = x.keys[index];
            index--;
        }

        index++;
        x.keys[index] = y.keys[K - 1];
        x.currentNumberOfKeys++;
        y.keys[K - 1] = 0;
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
                bb.putInt(n.keys[i]);
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
                temp.keys[i] = bb.getInt();
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
