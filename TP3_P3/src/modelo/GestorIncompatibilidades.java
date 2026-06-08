package modelo;

import java.util.ArrayList;
import java.util.List;

public class GestorIncompatibilidades {

	private final List<Incompatibilidad> incompatibilidades = new ArrayList<>();

	public void agregar(Incompatibilidad i) {
		incompatibilidades.add(i);
	}

	public List<Incompatibilidad> getIncompatibilidades() {
		return new ArrayList<>(incompatibilidades);
	}

	public void limpiar() {
		incompatibilidades.clear();
	}

	/** Quita todas las incompatibilidades en las que participa la persona. */
	public void eliminarInvolucrando(Persona persona) {
		incompatibilidades.removeIf(i -> i.involucra(persona));
	}

	/** Quita la incompatibilidad entre dos personas si existe. */
	public boolean eliminar(Persona p1, Persona p2) {
		return incompatibilidades.removeIf(i -> i.involucra(p1, p2));
	}

	/** Indica si dos personas son incompatibles entre sí. */
	public boolean sonIncompatibles(Persona p1, Persona p2) {
		for (Incompatibilidad i : incompatibilidades) {
			if (i.involucra(p1, p2)) {
				return true;
			}
		}
		return false;
	}

	/** Indica si una persona es incompatible con alguno de los integrantes ya seleccionados. */
	public boolean esIncompatibleCon(Persona candidata, List<Persona> seleccionados) {
		for (Persona p : seleccionados) {
			if (sonIncompatibles(candidata, p)) {
				return true;
			}
		}
		return false;
	}
}
