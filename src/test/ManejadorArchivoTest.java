package test;

import negocio.ManejadorArchivos;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ManejadorArchivoTest {
    private File file;
    private ManejadorArchivos manejadorArchivos;

    @Before
    public void setUp()
    {
        String path = "FileTest.txt";
        file = new File(path);

        manejadorArchivos = new ManejadorArchivos();
    }

    @Test
    public void CargarArchivoTest() throws IOException {

        manejadorArchivos.cargarArchivo(file);
        System.out.println(manejadorArchivos);
    }
}
