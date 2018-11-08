package presentacion;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import negocio.ManejadorArchivos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PrincipalController {
    public Label lblArchivo;
    public TextField txtBusqueda;
    public Label lblResultado;
    public TextArea txtArchivo;
    public ListView lvwTexto;
    private ManejadorArchivos manejadorArchivo;

    private void limpiarBusqueda() {
        txtBusqueda.setText("");
        lblResultado.setText("");
    }

    public void cargar(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Abrir manejadorArchivo de texto");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt"));
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            limpiarBusqueda();
            lblArchivo.setText(file.getAbsolutePath());

            try {
                manejadorArchivo.cargarArchivo(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            txtArchivo.setText(manejadorArchivo.toString());
        }
    }

    public void buscar (ActionEvent actionEvent) {

        if (txtBusqueda.getText().isEmpty()) {
            lblResultado.setText("No se ingreso ninguna palabra");
        } else {
            if (manejadorArchivo.buscarPalabra(txtBusqueda.getText())) {
                lblResultado.setText("Palabra encontrada");
            } else {
                lblResultado.setText("Palabra NO encontrada");
            }

        }
    }

    public void limpiar (ActionEvent actionEvent){
        limpiarBusqueda();


    }

    public PrincipalController() {
        manejadorArchivo = new ManejadorArchivos();
    }
}
