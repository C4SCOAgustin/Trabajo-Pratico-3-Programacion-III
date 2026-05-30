package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Resuelve el problema del equipo ideal mediante backtracking.
 * Busca el conjunto de personas que cumple los requerimientos por rol,
 * sin pares incompatibles, maximizando la suma de calificaciones.
 */
public class Solver {

	private final List<Persona> personas;
	private final GestorIncompatibilidades gestor;
	private final Requerimientos requerimientos;

	private Equipo mejorEquipo;
	private int mejorPuntaje;
	private long llamadasCasoBase; // estadística: veces que se completó un equipo candidato

	public Solver(List<Persona> personas, GestorIncompatibilidades gestor, Requerimientos requerimientos) {
		this.personas = new ArrayList<>(personas);
		this.gestor = gestor;
		this.requerimientos = requerimientos;
	}

	/** Ejecuta la búsqueda y devuelve el mejor equipo encontrado (vacío si no hay solución). */
	public Equipo resolver() {
		mejorEquipo = new Equipo();
		mejorPuntaje = -1;
		llamadasCasoBase = 0;
		backtrack(0, new ArrayList<Persona>(), 0);
		return mejorEquipo;
	}

	/**
	 * @param indice    posición actual en la lista de personas
	 * @param actuales  personas seleccionadas hasta el momento
	 * @param puntaje   suma de calificaciones de las seleccionadas
	 */
	private void backtrack(int indice, List<Persona> actuales, int puntaje) {
		// Caso base: ya consideramos todas las personas.
		if (indice == personas.size()) {
			llamadasCasoBase++;
			if (cumpleRequerimientos(actuales) && puntaje > mejorPuntaje) {
				mejorPuntaje = puntaje;
				mejorEquipo = new Equipo(actuales);
			}
			return;
		}

		Persona candidata = personas.get(indice);

		// Opción 1: incluir a la persona si es válido hacerlo.
		boolean cupoDisponible = contarRol(actuales, candidata.getRol())
				< requerimientos.getCantidad(candidata.getRol());
		boolean compatible = !gestor.esIncompatibleCon(candidata, actuales);

		if (cupoDisponible && compatible) {
			actuales.add(candidata);
			backtrack(indice + 1, actuales, puntaje + candidata.getCalificacion());
			actuales.remove(actuales.size() - 1);
		}

		// Opción 2: no incluir a la persona.
		backtrack(indice + 1, actuales, puntaje);
	}

	private int contarRol(List<Persona> seleccionados, Rol rol) {
		int c = 0;
		for (Persona p : seleccionados) {
			if (p.getRol() == rol) {
				c++;
			}
		}
		return c;
	}

	private boolean cumpleRequerimientos(List<Persona> seleccionados) {
		for (Rol r : Rol.values()) {
			if (contarRol(seleccionados, r) != requerimientos.getCantidad(r)) {
				return false;
			}
		}
		return true;
	}

	public long getLlamadasCasoBase() {
		return llamadasCasoBase;
	}
}
