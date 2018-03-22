import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;


public class HashTable {

    LinkedList<YelpData>[] table;

    public HashTable(int size) {

        table = new LinkedList[size];
    }

    public YelpData add(YelpData y, HashTable ht) {

        int hash = y.hashify(ht);

        if (this.table[hash] == null) {
            LinkedList<YelpData> ll = new LinkedList<YelpData>();
            this.table[hash] = ll;
            ll.add(y);
        } else {
            this.table[hash].add(y);
        }

        return y;
    }

    public boolean contains(YelpData y, HashTable ht) {
        int hash = y.hashify(ht);
        String name = y.name;

        if (ht.table[hash] == null) {
            return false;
        }

        for (YelpData yd : ht.table[hash]){
            if (yd.name.equals(name)){
                return true;
            }
        }

        return false;
    }


    public HashTable resize() {
        int slots = this.table.length;
        HashTable biggerHT = new HashTable(slots * 2);

        for (int i = 0; i < this.table.length; i++) {
            if (this.table[i] != null) {
                for (YelpData y : this.table[i]) {
                    biggerHT.add(y, biggerHT);
                }
            }
        }
        return biggerHT;
    }

}
