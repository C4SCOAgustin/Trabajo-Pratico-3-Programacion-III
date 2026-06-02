package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

import modelo.Equipo;
import modelo.EstadoAplicacion;
import modelo.EstadisticasSolver;
import modelo.GestorIncompatibilidades;
import modelo.Incompatibilidad;
import modelo.Persona;
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

	public void agregarPersona(String nombre, Rol rol, int calificacion) {
		Persona p = new Persona(nombre, rol, calificacion);
		if (personas.contains(p)) {
			throw new IllegalArgumentException("Ya existe una persona con ese nombre");
		}
		personas.add(p);
	}

	public void eliminarPersona(String nombre) {
		Persona persona = buscarPersona(nombre);
		if (persona == null) {
			throw new IllegalArgumentException("No existe una persona con ese nombre");
		}
		personas.remove(persona);
		gestor.eliminarInvolucrando(persona);
	}

	public void eliminarTodasLasPersonas() {
		personas.clear();
		gestor.limpiar();
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

	public void limpiarDatos() {
		personas.clear();
		gestor.limpiar();
		for (Rol r : Rol.values()) {
			requerimientos.setCantidad(r, 0);
		}
	}

	/** Genera empleados con rol y calificación aleatorios, más incompatibilidades aleatorias. */
	public void generarDatosPrueba(int cantidadEmpleados, int cantidadIncompatibilidades) {
		GeneradorDatosPrueba.generar(personas, gestor, requerimientos,
				cantidadEmpleados, cantidadIncompatibilidades);
	}

	public void generarDatosPrueba() {
		generarDatosPrueba(GeneradorDatosPrueba.EMPLEADOS_DEFAULT,
				GeneradorDatosPrueba.INCOMPATIBILIDADES_DEFAULT);
	}

	public Requerimientos copiarRequerimientos() {
		Requerimientos copia = new Requerimientos();
		copia.copiarDesde(requerimientos);
		return copia;
	}

	public EstadoAplicacion exportarEstado() {
		List<String[]> pares = new ArrayList<>();
		for (Incompatibilidad i : gestor.getIncompatibilidades()) {
			pares.add(new String[] { i.getA().getNombre(), i.getB().getNombre() });
		}
		return new EstadoAplicacion(personas, pares, copiarRequerimientos());
	}

	/** Guarda el estado en disco sin propagar errores a la vista. */
	public void guardarEstadoSilencioso() {
		try {
			guardarEstado();
		} catch (IOException ignored) {
			// La vista puede informar al cerrar si falla el guardado definitivo.
		}
	}

	public void importarEstado(EstadoAplicacion estado) {
		personas.clear();
		personas.addAll(estado.getPersonas());
		gestor.limpiar();
		for (String[] par : estado.getParesIncompatibles()) {
			agregarIncompatibilidad(par[0], par[1]);
		}
		requerimientos.copiarDesde(estado.getRequerimientos());
	}

	public void guardarEstado() throws IOException {
		Persistencia.guardar(exportarEstado());
	}

	/** @return true si se cargó un estado guardado */
	public boolean cargarEstado() throws IOException, ClassNotFoundException {
		EstadoAplicacion estado = Persistencia.cargar();
		if (estado == null) {
			return false;
		}
		importarEstado(estado);
		return true;
	}

	/**
	 * Resuelve el problema en un hilo separado.
	 * @param onProgreso   recibe el porcentaje 0-100 (se procesa en el EDT vía {@code process}).
	 * @param alTerminar   callback que recibe el resultado (se ejecuta en el EDT).
	 */
	public void resolverAsync(final Consumer<Integer> onProgreso,
			final Consumer<ResultadoResolucion> alTerminar) {
		SwingWorker<ResultadoResolucion, Integer> worker = new SwingWorker<ResultadoResolucion, Integer>() {
			@Override
			protected ResultadoResolucion doInBackground() {
				try {
					Solver solver = new Solver(personas, gestor, requerimientos,
							porcentaje -> publish(porcentaje));
					Equipo equipo = solver.resolver();
					return ResultadoResolucion.exito(equipo, solver.getUltimasEstadisticas());
				} catch (RuntimeException e) {
					return ResultadoResolucion.error(e.getMessage());
				}
			}

			@Override
			protected void process(List<Integer> avances) {
				if (onProgreso != null && !avances.isEmpty()) {
					onProgreso.accept(avances.get(avances.size() - 1));
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
