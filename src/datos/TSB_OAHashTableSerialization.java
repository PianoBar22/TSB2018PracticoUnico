package datos;

import entidades.Contador;

import java.io.*;
import java.nio.file.Files;

public class TSB_OAHashTableSerialization {

    public static <K, V> TSB_OAHashtable<K, V> read(String path) throws IOException, ClassNotFoundException {
        FileInputStream istream = new FileInputStream(path);
        ObjectInputStream p = new ObjectInputStream(istream);

        TSB_OAHashtable sl = (TSB_OAHashtable<K, V>)p.readObject();

        p.close();
        istream.close();

        return sl;
    }

    public static <K, V> void write (TSB_OAHashtable<K, V> sl, String path) throws IOException {
        FileOutputStream ostream = new FileOutputStream(path);
        ObjectOutputStream p = new ObjectOutputStream(ostream);

        p.writeObject(sl);

        p.flush();
        ostream.close();
    }
}
