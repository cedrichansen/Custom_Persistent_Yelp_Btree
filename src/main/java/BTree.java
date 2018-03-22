import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class BTree <Key extends Comparable<Key>, Value> {

    // max children per B-tree node = m-1
    private static final int M = 4;


    Node root; //root of tree
    private int height; //height of tree
    private int N; //number of k/v pairs in bTree
    private ArrayList<String> keys;
    private static final String keyFile = "keys.txt";
    private static final String valueFile= "values.ser";


    private static final class Node {
        private int childCount; // number of children
        private Entry [] children = new Entry [M]; //children array

        //constructor with k children for a new node
        private Node(int k){
            childCount = k;
        }
    }


    private static class Entry {

        private Comparable key;
        private Object val;
        private Node next;


        Entry(Comparable key, Object val, Node next){
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    //create a new btree with root node (no children initially)
    public BTree(){
        root = new Node(0);
    }

    public int size(){
        return N;
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public int getHeight(){
        return height;
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }


    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    //search for key
    private Value search(Node x, Key key, int ht){

        Entry [] children = x.children;

        //if its an external node
        if (ht == 0){
            for (int i = 0; i<x.childCount; i++){
                if (eq(key,children[i].key )){
                    return (Value) children[i].val;
                }
            }
        }

        //must be an internal node
        else {
            for (int i=0; i<x.childCount; i++){
                if (i + 1 == x.childCount || less(key, children[i+1].key)){
                    return search(children[i].next, key, ht-1);
                }
            }
        }

        return null;
    }


    //returns value of keys that arent null
    public Value get(Key key){
        if (key == null){
            throw new NullPointerException("Key can't be null");
        }
        return search(root, key, height);
    }



    //Insert a new K/V pair
    //looks to find open spot
    private Node insert(Node currentNode, Key key, Value val, int ht){
        int i; //index at which to place new entry
        Entry t = new Entry(key, val, null); // create entry with kv pairs and no next

        //if at a leaf, begin to traverse child array to find correct psotion for new entry
        if (ht==0){
            for (i = 0; i <currentNode.childCount; i++){
                if (less(key, currentNode.children[i].key)){
                    break;
                }
            }
        }

        //if height is not 0, then we keep looking through tree
        else {
            for (i = 0; i<currentNode.childCount; i++){
                if ((i+1 == currentNode.childCount) || less(key, currentNode.children[i+1].key)){
                    Node u = insert(currentNode.children[i++].next, key, val, ht-1);
                    //increment i because of the if statement. look through tree recursively
                    //to that we find the correct spot for the key. must be less than i+1
                    if (u == null){
                        // if insert doesnt result in split, then ...
                        return null;
                    }
                    t.key = u.children[0].key; //if we split node, set t node's key to the key of new right node
                    t.next = u; // the value of t is set as the new right node
                    break;
                }
            }
        }

        //iterate through children of current node, and if j < i, then stop
        //shift some elements to the right

        for (int j=currentNode.childCount; j<i; j--){
            currentNode.children[j] = currentNode.children[j-1];
            }

            //create new entry node in target location.
            currentNode.children[i] = t;
            currentNode.childCount++;

            //check to see if number of children is too big or not
            //return (h.childCount < M) ? null : split(h);
            if (currentNode.childCount < M) {
                return null;
            } else {
                return split(currentNode);
            }

    }

    //split node in half and return right node because it contains bigger values
    private Node split(Node node){
        Node temp = new Node(M/2);
        node.childCount = M/2;
        for (int i = 0; i< M/2; i++) {
            temp.children[i] = node.children[M/2 + i];
        }
        return temp;
    }



    //inserts a new k/v pair into the table
    //overwrites old val with new val if key already exists
    public void put(Key key, Value val){
        if (key == null) {
            throw new NullPointerException("key can't be null");
        }
        Node u = insert(root, key, val, height);
        N++;
        if (u == null) {
            return;
        }
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry (u.children[0].key, null, u);
        root = t;
        height++;
    }


    //serialize btree into RandomAccessFile
    public static void serialize(BTree bt, String kf, String vf){
        try{
            RandomAccessFile keyFile = new RandomAccessFile(kf, "rw");
            RandomAccessFile valueFile = new RandomAccessFile(vf, "rw");
            valueFile.seek(0);
            keyFile.seek(0);
            ArrayList ky = bt.getKeys();
            valueFile.writeDouble(bt.N);
            for (int i = 0; i < bt.N; i++) {
                keyFile.writeUTF((String)ky.get(i));
                valueFile.writeLong((Long) bt.get((String) ky.get(i)));
            }
            keyFile.close();
            valueFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //easily call serialize method
    public static void serialize(BTree bt) {
        BTree.serialize(bt, keyFile, valueFile);
    }



    public static BTree deserialize(String kf, String vf) throws Exception{

        BTree bt = new BTree();
        int size;
        RandomAccessFile keyFile = new RandomAccessFile(kf, "r");
        RandomAccessFile valueFile = new RandomAccessFile(vf, "r");

        keyFile.seek(0);
        valueFile.seek(0);

        size = valueFile.readInt();

        for (int i = 0; i< size; i++){
            bt.put(keyFile.readUTF(), valueFile.readLong());
        }

        keyFile.close();
        valueFile.close();
        return bt;
    }

    //eaily call deserialize with premade files
    public static BTree deserialize() throws Exception{
        return BTree.deserialize(keyFile, valueFile);
    }



}
