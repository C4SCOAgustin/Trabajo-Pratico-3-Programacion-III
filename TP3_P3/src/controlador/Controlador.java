package controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

import modelo.Equipo;
import modelo.GestorIncompatibilidades;
import modelo.Incompatibilidad;
import modelo.Persona;
import modelo.Requerimientos;
import modelo.Rol;
import modelo.Solver;

/**
 * Controlador (capa Template/Controlador del MVT).
 * Mantiene el estado del modelo y conecta la vista con la lógica de negocio.
 * Ejecuta el algoritmo en un hilo separado mediante SwingWorker.
 */
public class Controlador {

	private final List<Persona> personas = new ArrayList<>();
	private final GestorIncompatibilidades gestor = new GestorIncompatibilidades();
	private final Requerimientos requerimientos = new Requerimientos();

	public void agregarPersona(String nombre, Rol rol, int calificacion) {
		Persona p = new Persona(nombre, rol, calificacion);
		if (personas.contains(p)) {
			throw new IllegalArgumentException("Ya existe una persona con ese nombre");
		}
		personas.add(p);
	}

	public List<Persona> getPersonas() {
		return new ArrayList<>(personas);
	}

	public Persona buscarPersona(String nombre) {
		for (Persona p : personas) {
			if (p.getNombre().equals(nombre)) {
				return p;
			}
		}
		return null;
	}

	public void agregarIncompatibilidad(String nombreA, String nombreB) {
		Persona a = buscarPersona(nombreA);
		Persona b = buscarPersona(nombreB);
		if (a == null || b == null) {
			throw new IllegalArgumentException("Ambas personas deben existir");
		}
		gestor.agregar(new Incompatibilidad(a, b));
	}

	public List<Incompatibilidad> getIncompatibilidades() {
		return gestor.getIncompatibilidades();
	}

	public void setRequerimiento(Rol rol, int cantidad) {
		requerimientos.setCantidad(rol, cantidad);
	}

	public Requerimientos getRequerimientos() {
		return requerimientos;
	}

	/**
	 * Resuelve el problema en un hilo separado.
	 * @param alTerminar callback que recibe el equipo resultante (se ejecuta en el EDT).
	 */
	public void resolverAsync(final Consumer<Equipo> alTerminar) {
		SwingWorker<Equipo, Void> worker = new SwingWorker<Equipo, Void>() {
			@Override
			protected Equipo doInBackground() {
				Solver solver = new Solver(personas, gestor, requerimientos);
				return solver.resolver();
			}

			@Override
			protected void done() {
				try {
					alTerminar.accept(get());
				} catch (Exception e) {
					alTerminar.accept(new Equipo());
				}
			}
		};
		worker.execute();
	}

	/** Resolución sincrónica, útil para tests. */
	public Equipo resolver() {
		Solver solver = new Solver(personas, gestor, requerimientos);
		return solver.resolver();
	}
}
