package controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

import modelo.Equipo;
import modelo.EstadisticasSolver;

import modelo.GestorIncompatibilidades;
import modelo.Incompatibilidad;
import modelo.Persona;
import modelo.AlmacenPersonas;
import java.io.IOException;
import modelo.Requerimientos;
import modelo.ResultadoResolucion;
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

	public Controlador() {
		try {
			List<Persona> cargadas = AlmacenPersonas.cargar();
			if (cargadas != null && !cargadas.isEmpty()) {
				personas.addAll(cargadas);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void agregarPersona(String nombre, Rol rol, int calificacion) {
		Persona p = new Persona(nombre, rol, calificacion);
		if (personas.contains(p)) {
			throw new IllegalArgumentException("Ya existe una persona con ese nombre");
		}
		personas.add(p);
		try {
			AlmacenPersonas.guardar(personas);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void eliminarPersona(String nombre) {
		Persona persona = buscarPersona(nombre);
		if (persona == null) {
			throw new IllegalArgumentException("No existe una persona con ese nombre");
		}
		personas.remove(persona);
		gestor.eliminarInvolucrando(persona);
		try {
			AlmacenPersonas.guardar(personas);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void eliminarTodasLasPersonas() {
		personas.clear();
		gestor.limpiar();
		try {
			AlmacenPersonas.guardar(personas);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		if(gestor.sonIncompatibles(a, b)){
			throw new IllegalArgumentException("La incompatibilidad ya existe");
		}
		gestor.agregar(new Incompatibilidad(a, b));

	}

	public void eliminarIncompatibilidad(String nombreA, String nombreB) {
		Persona a = buscarPersona(nombreA);
		Persona b = buscarPersona(nombreB);
		if (a == null || b == null) {
			throw new IllegalArgumentException("Ambas personas deben existir");
		}
		if (a.equals(b)) {
			throw new IllegalArgumentException("No se puede eliminar una incompatibilidad entre la misma persona");
		}
		if (!gestor.eliminar(a, b)) {
			throw new IllegalArgumentException("No existe esa incompatibilidad");
		}
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

	public void limpiarDatos() {
		personas.clear();
		gestor.limpiar();
		for (Rol r : Rol.values()) {
			requerimientos.setCantidad(r, 0);
		}
	}

	public Requerimientos copiarRequerimientos() {
		Requerimientos copia = new Requerimientos();
		copia.copiarDesde(requerimientos);
		return copia;
	}



	/**
	 * Resuelve el problema en un hilo separado.
	 * @param onEstadisticas recibe estadísticas parciales durante la ejecución.
	 * @param alTerminar   callback que recibe el resultado (se ejecuta en el EDT).
	 */
	public void resolverAsync(final Consumer<EstadisticasSolver> onEstadisticas,
			final Consumer<ResultadoResolucion> alTerminar) {
		SwingWorker<ResultadoResolucion, EstadisticasSolver> worker =
				new SwingWorker<ResultadoResolucion, EstadisticasSolver>() {
			@Override
			protected ResultadoResolucion doInBackground() {
				try {
					final Solver[] solverHolder = new Solver[1];
					solverHolder[0] = new Solver(personas, gestor, requerimientos,
							porcentaje -> publish(solverHolder[0].getUltimasEstadisticas()));
					Equipo equipo = solverHolder[0].resolver();
					return ResultadoResolucion.exito(equipo, solverHolder[0].getUltimasEstadisticas());
				} catch (RuntimeException e) {
					return ResultadoResolucion.error(e.getMessage());
				}
			}

			@Override
			protected void process(List<EstadisticasSolver> estadisticas) {
				if (onEstadisticas != null && !estadisticas.isEmpty()) {
					onEstadisticas.accept(estadisticas.get(estadisticas.size() - 1));
				}
			}

			@Override
			protected void done() {
				try {
					alTerminar.accept(get());
				} catch (Exception e) {
					String msg = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
					alTerminar.accept(ResultadoResolucion.error(msg));
				}
			}
		};
		worker.execute();
	}

	/** Resolución sincrónica, útil para tests. */
	public ResultadoResolucion resolver() {
		try {
			Solver solver = new Solver(personas, gestor, requerimientos);
			Equipo equipo = solver.resolver();
			return ResultadoResolucion.exito(equipo, solver.getUltimasEstadisticas());
		} catch (RuntimeException e) {
			return ResultadoResolucion.error(e.getMessage());
		}
	}
}
