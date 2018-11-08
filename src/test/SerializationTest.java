package test;

import datos.TSB_OAHashTableSerialization;
import datos.TSB_OAHashtable;
import entidades.Contador;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SerializationTest {
    private TSB_OAHashtable<String, Contador> list;
    private String pathTest = "lista.dat";

    @Before
    public void setUp(){
        try {
            Files.deleteIfExists(Paths.get(pathTest));
        } catch (IOException e) {
            e.printStackTrace();
        }

        list = new TSB_OAHashtable();
        list.put("Palabra1", new Contador());
        list.put("Palabra2", new Contador(3));
        list.put("Palabra3", new Contador(5));
        list.put("Palabra4", new Contador(6));
        list.put("Palabra5", new Contador());
    }

    @Test(expected = FileNotFoundException.class)
    public void fileNotFoundReaderTest() throws IOException, ClassNotFoundException {
        TSB_OAHashtable<String, Contador> listRead = TSB_OAHashTableSerialization.read(pathTest);
    }

    @Test
    public void writerAndReadTest() throws IOException, ClassNotFoundException {
        TSB_OAHashTableSerialization.write(list, pathTest);

        TSB_OAHashtable<String, Contador> listRead = TSB_OAHashTableSerialization.read(pathTest);

        assertEquals(list, listRead);
    }

    @Test
    public void writerOverrideFileExistTest() throws IOException, ClassNotFoundException {
        TSB_OAHashTableSerialization.write(list, pathTest);
        TSB_OAHashtable<String, Contador> listRead = TSB_OAHashTableSerialization.read(pathTest);

        assertEquals(list, listRead);

        TSB_OAHashTableSerialization.write(new TSB_OAHashtable<>(), pathTest);
        listRead = TSB_OAHashTableSerialization.read(pathTest);
        assertNotEquals(list, listRead);
    }
}
