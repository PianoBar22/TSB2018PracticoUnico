package negocio;

import datos.TSBArrayList;
import datos.TSB_OAHashTableSerialization;
import datos.TSB_Hashtable;
import datos.TSB_OAHashtable;
import entidades.Contador;
import entidades.Palabra;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class ManejadorArchivos {
    private TSBArrayList<File> listArchivos;
    private Map<String, Contador> listPalabras;
    private String pathListaPalabras = "lista.dat";

    public ManejadorArchivos() {
        listPalabras = new TSB_OAHashtable<>();
        listArchivos = new TSBArrayList<>();

        this.obtenerPalabrasGuardadas();
    }

    public ManejadorArchivos(String path) {
        listPalabras = new TSB_OAHashtable<>();
        listArchivos = new TSBArrayList<>();
        this.pathListaPalabras = path;

        this.obtenerPalabrasGuardadas();
    }

    public void cargarArchivo(File file) throws IOException {
        if (file != null)
        {
            listArchivos.add(file);

            InputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                String regex = "([a-zA-Z]|'|´)+";
                //String regex = "(\\p{Ll}|')+";

                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find())
                {
                    Contador p = listPalabras.get(matcher.group().toUpperCase());

                    if (p != null)
                    {
                        p.incrementar();
                    }
                    else
                    {
                        listPalabras.put(matcher.group().toUpperCase(), new Contador());
                    }
                }
            }

            this.guardarPalabrasEnDisco();
        }
    }

    @Override
    public String toString() {
        return listPalabras.toString();
    }

    public Contador buscarPalabra(String text) {
        return listPalabras.get(text.toUpperCase());
    }

    public Map<String, Contador> getListPalabras() {
        return listPalabras;
    }

    private void guardarPalabrasEnDisco() throws IOException {
        TSB_OAHashTableSerialization.write(this.listPalabras, pathListaPalabras);
    }

    private void obtenerPalabrasGuardadas() {
        if (Files.exists(Paths.get(pathListaPalabras))){
            Map<String, Contador> listDisco = null;
            try {
                listDisco = TSB_OAHashTableSerialization.read(this.pathListaPalabras);
            } catch (IOException e) {

            } catch (ClassNotFoundException e) {

            }
            this.listPalabras = listDisco;
        }
    }

    public void borrarDatos() throws IOException {
        Files.deleteIfExists(Paths.get(this.pathListaPalabras));
        this.listPalabras = new TSB_OAHashtable<>();
    }
}
