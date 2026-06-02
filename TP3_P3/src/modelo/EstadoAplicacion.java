package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Instantánea serializable del estado de la aplicación. */
public class EstadoAplicacion implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<Persona> personas;
	private final List<String[]> paresIncompatibles;
	private final Requerimientos requerimientos;

	public EstadoAplicacion(List<Persona> personas, List<String[]> paresIncompatibles,
			Requerimientos requerimientos) {
		this.personas = new ArrayList<>(personas);
		this.paresIncompatibles = new ArrayList<>();
		for (String[] par : paresIncompatibles) {
			this.paresIncompatibles.add(new String[] { par[0], par[1] });
		}
		this.requerimientos = requerimientos;
	}

	public List<Persona> getPersonas() {
		return new ArrayList<>(personas);
	}

	public List<String[]> getParesIncompatibles() {
		List<String[]> copia = new ArrayList<>();
		for (String[] par : paresIncompatibles) {
			copia.add(new String[] { par[0], par[1] });
		}
		return copia;
	}

	public Requerimientos getRequerimientos() {
		return requerimientos;
	}
}
