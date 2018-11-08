package datos;

import java.io.*;
import java.util.Map;

public class TSB_OAHashTableSerialization {

    public static <K, V> Map<K, V> read(String path) throws IOException, ClassNotFoundException {
        FileInputStream istream = new FileInputStream(path);
        ObjectInputStream p = new ObjectInputStream(istream);

        Map sl = (Map<K, V>)p.readObject();

        p.close();
        istream.close();

        return sl;
    }

    public static <K, V> void write (Map<K, V> sl, String path) throws IOException {
        FileOutputStream ostream = new FileOutputStream(path);
        ObjectOutputStream p = new ObjectOutputStream(ostream);

        p.writeObject(sl);

        p.flush();
        ostream.close();
    }
}
