package test;

import datos.TSB_Hashtable;
import datos.TSB_OAHashTableSerialization;
import entidades.Contador;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SerializationTest {
    private TSB_Hashtable<String, Contador> list;
    private String pathTest = "lista.dat";

    @Before
    public void setUp(){
        try {
            Files.deleteIfExists(Paths.get(pathTest));
        } catch (IOException e) {
            e.printStackTrace();
        }

        list = new TSB_Hashtable();
        list.put("Palabra1", new Contador());
        list.put("Palabra2", new Contador(3));
        list.put("Palabra3", new Contador(5));
        list.put("Palabra4", new Contador(6));
        list.put("Palabra5", new Contador());
    }

    @Test(expected = FileNotFoundException.class)
    public void fileNotFoundReaderTest() throws IOException, ClassNotFoundException {
        Map<String, Contador> listRead = TSB_OAHashTableSerialization.read(pathTest);
    }

    @Test
    public void writerAndReadTest() throws IOException, ClassNotFoundException {
        TSB_OAHashTableSerialization.write(list, pathTest);

        Map<String, Contador> listRead = TSB_OAHashTableSerialization.read(pathTest);

        assertEquals(list, listRead);
    }

    @Test
    public void writerOverrideFileExistTest() throws IOException, ClassNotFoundException {
        TSB_OAHashTableSerialization.write(list, pathTest);
        Map<String, Contador> listRead = TSB_OAHashTableSerialization.read(pathTest);

        assertEquals(list, listRead);

        TSB_OAHashTableSerialization.write(new TSB_Hashtable<>(), pathTest);
        listRead = TSB_OAHashTableSerialization.read(pathTest);
        assertNotEquals(list, listRead);
    }
}
