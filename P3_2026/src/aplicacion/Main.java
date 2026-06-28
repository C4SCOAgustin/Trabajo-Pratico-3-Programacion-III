package aplicacion;

import java.awt.EventQueue;

import controlador.Controlador;
import modelo.ModeloEquipo;
import vista.VentanaPrincipal;

public class Main {

	public static void main(String[] args) {
	 EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                    ModeloEquipo modelo = new ModeloEquipo();
	                    Controlador controlador = new Controlador(modelo);
	                    VentanaPrincipal ventana = new VentanaPrincipal(modelo, controlador);
	                    ventana.setVisible(true);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });

	}

}
