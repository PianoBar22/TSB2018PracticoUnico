package negocio;

import datos.TSBArrayList;
import datos.TSB_OAHashTableSerialization;
import datos.TSB_OAHashtable;
import entidades.Contador;
import entidades.Palabra;

import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class ManejadorArchivos {
    private TSBArrayList<String> listArchivos;
    private TSB_OAHashtable<String, Contador> listPalabras;

    public ManejadorArchivos() {
        listPalabras = new TSB_OAHashtable<>();
        listArchivos = new TSBArrayList<>();
    }

    public void cargarArchivo(File file) throws IOException {
        if (file != null)
        {
            listArchivos.add(file.getAbsolutePath());

            InputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                //String regex = "(\\w|'|ó|í)+";
                String regex = "(\\p{L}|')+";

                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find())
                {
                    Contador p = listPalabras.get(matcher.group());

                    if (p != null)
                    {
                        p.incrementar();
                    }
                    else
                    {
                        listPalabras.put(matcher.group(), new Contador());
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return listPalabras.toString();
    }

    public boolean buscarPalabra(String text) {
        return listPalabras.get(text) != null;
    }
}
