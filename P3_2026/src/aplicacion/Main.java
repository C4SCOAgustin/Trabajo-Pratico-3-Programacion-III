package aplicacion;

import java.awt.EventQueue;
import vista.VentanaPrincipal;

public class Main {

	public static void main(String[] args) {
		 EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                    VentanaPrincipal ventana = new VentanaPrincipal();
	                    ventana.setVisible(true);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });

	}

}
