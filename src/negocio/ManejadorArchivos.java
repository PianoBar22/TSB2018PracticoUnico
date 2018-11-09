package negocio;

import datos.TSBArrayList;
import datos.TSB_OAHashTableSerialization;
import datos.TSB_Hashtable;
import datos.TSB_OAHashtable;
import entidades.Contador;
import entidades.Palabra;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
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
    }

    public ManejadorArchivos(String path) {
        listPalabras = new TSB_OAHashtable<>();
        listArchivos = new TSBArrayList<>();
        this.pathListaPalabras = path;
    }

    public void cargarArchivo(File file) throws IOException {
        if (file != null)
        {
            listArchivos.add(file);

            InputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
                    String line;

            while ((line = reader.readLine()) != null) {
                //String regex = "([a-zA-Z]|'|í)+";
                String regex = "(\\p{L}|'|ñ|Ñ|á|é|í|ó|ú|Á|É|Í|Ó|Ú)+";

                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find())
                {
                    String descripcion = matcher.group().toUpperCase();
                    Contador p = listPalabras.get(descripcion);

                    if (p != null)
                    {
                        p.incrementar();
                    }
                    else
                    {
                        listPalabras.put(descripcion, new Contador());
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return listPalabras.toString();
    }

    public Contador buscarPalabra(String text) {
        return listPalabras.get(text.toUpperCase());
    }

    public int sizePalabras(){
        return this.listPalabras.size();
    }

    public Iterator<Map.Entry<String, Contador>> iterator(){
        return this.listPalabras.entrySet().iterator();
    }

    public void guardarPalabrasEnDisco() throws IOException {
        TSB_OAHashTableSerialization.write(this.listPalabras, pathListaPalabras);
    }

    public void obtenerPalabrasGuardadas() {
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
