import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BTree implements java.io.Serializable{

    Node root;
    static final String file = "BTreeData.txt";
    static final int k = 8;
    static final int NodeSize = 1000;
    int location;


    public BTree () throws Exception {
        location = 0;
        Node x = new Node(location);
        x.leaf = 1;
        x.n = 0;
        root = x;

        //diskwrite(x);

    }


    class Node implements java.io.Serializable{


        int cn;//4
        int n;//4
        int key[];
        Long children[];
        int leaf;//4
        long id;//8
        //632





        Node(long i) {
            id = i;
            cn = 0;
            n = 0;
            key = new int[2 * k - 1];
            children = new Long[2 * k];
        }




    }



    void write(Node node) throws IOException {
        try{
            RandomAccessFile f = new RandomAccessFile(file, "rw");
            f.seek(node.id *NodeSize);
            FileChannel fc = f.getChannel();
            ByteBuffer b = ByteBuffer.allocate(NodeSize);
            b.putInt(node.leaf);
            b.putLong(node.id);
            b.putInt(node.n);
            for (int i = 0; i< node.cn; i++){
                b.putInt(node.key[i]);
            }
            b.putInt(node.cn);
            for (int i = 0; i<node.cn; i++){
                b.putLong(node.children[i]);
            }

            b.flip();
            fc.write(b);
            b.clear();
            fc.close();
            f.close();

        } catch (Exception e) {
            System.out.println("error writing because of: " + e);
            e.printStackTrace();
        }

    }


    Node read(long position) throws Exception {
        try {
            Node temp = new Node(0);
            RandomAccessFile f = new RandomAccessFile(file, "rw");
            f.seek(position * NodeSize);
            FileChannel fc = f.getChannel();
            ByteBuffer b = ByteBuffer.allocate(NodeSize);
            fc.read(b);
            b.flip();
            temp.leaf = b.getInt();
            temp.id = b.getLong();
            temp.n = b.getInt();

            for (int i = 0; i< temp.n; i++){
                temp.key[i] = b.getInt();
            }
            temp.cn = b.getInt();
            for (int i = 0; i< temp.cn; i++) {
                temp.children[i] = b.getLong();
            }
            b.clear();
            fc.close();;
            f.close();
            return temp;

        } catch (Exception e) {
            System.out.println("error reading because of: " + e);
            e.printStackTrace();
            return null;
        }


    }




}
