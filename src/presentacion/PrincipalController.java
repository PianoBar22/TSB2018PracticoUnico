package presentacion;

import entidades.Contador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import negocio.ManejadorArchivos;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {
    public Label lblArchivo;
    public TextField txtBusqueda;
    public Label lblResultado;
    public TextArea txtArchivo;
    public ListView lvPalabras;
    private ManejadorArchivos manejadorArchivo;

    private void limpiarBusqueda() {
        txtBusqueda.setText("");
        lblResultado.setText("");
    }

    public void cargar(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Abrir Archivo de texto");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt"));
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            limpiarBusqueda();

            try {
                manejadorArchivo.cargarArchivo(file);
                manejadorArchivo.guardarPalabrasEnDisco();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mostrarPalabras();
        }
    }

    public void buscar (ActionEvent actionEvent) {

        if (txtBusqueda.getText().isEmpty()) {
            lblResultado.setText("No se ingreso ninguna palabra");
        } else {
            Contador c = manejadorArchivo.buscarPalabra(txtBusqueda.getText());

            if (c != null) {
                lblResultado.setText("Palabra encontrada; " + c.toString());
            } else {
                lblResultado.setText("Palabra NO encontrada");
            }

        }
    }

    public void limpiar (ActionEvent actionEvent){
        try {
            this.manejadorArchivo.borrarDatos();
            mostrarPalabras();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Hubo un error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public PrincipalController() {
        manejadorArchivo = new ManejadorArchivos();
        manejadorArchivo.obtenerPalabrasGuardadas();
    }

    private void mostrarPalabras(){
        lvPalabras.getItems().clear();

        lblArchivo.setText("Cantidad de palabras: " + manejadorArchivo.sizePalabras());
        if (manejadorArchivo.sizePalabras() > 0){
            Iterator<Map.Entry<String, Contador>> i = manejadorArchivo.iterator();
            ObservableList data = FXCollections.observableArrayList();

            while(i.hasNext())
            {
                Map.Entry<String, Contador> e = i.next();
                String key = e.getKey();
                int value = ((Contador)e.getValue()).getFrecuencia();

                String result = "palabra: " + key + "; frecuencia: " + value;
                data.add(result);
            }
            lvPalabras.setItems(data);
            //txtArchivo.setText(manejadorArchivo.getListPalabras().toString());
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrarPalabras();
    }
}
