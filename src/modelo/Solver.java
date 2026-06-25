package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

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
	private long llamadasCasoBase;
	private long nodosVisitados;
	private long nodosEstimados;
	private int ultimoPorcentajeReportado;
	private EstadisticasSolver ultimasEstadisticas;
	private long inicioEjecucionNano;
	private final IntConsumer reporteProgreso;
	private static final long INTERVALO_REPORTE_PROGRESO = 1000L;
	private static final int MAX_EXPONENTE_ESTIMACION = 20;

	public Solver(List<Persona> personas, GestorIncompatibilidades gestor, Requerimientos requerimientos) {
		this(personas, gestor, requerimientos, null);
	}

	public Solver(List<Persona> personas, GestorIncompatibilidades gestor, Requerimientos requerimientos,
			IntConsumer reporteProgreso) {
		this.personas = new ArrayList<>(personas);
		this.gestor = gestor;
		this.requerimientos = requerimientos;
		this.reporteProgreso = reporteProgreso;
	}

	/** Ejecuta la búsqueda y devuelve el mejor equipo encontrado (vacío si no hay solución). */
	public Equipo resolver() {
		mejorEquipo = new Equipo();
		mejorPuntaje = -1;
		llamadasCasoBase = 0;
		nodosVisitados = 0;
		ultimoPorcentajeReportado = 0;
		nodosEstimados = estimarNodos(personas.size());
		inicioEjecucionNano = System.nanoTime();
		backtrack(0, new ArrayList<Persona>(), 0);
		long tiempoMs = (System.nanoTime() - inicioEjecucionNano) / 1_000_000L;
		ultimasEstadisticas = new EstadisticasSolver(llamadasCasoBase, tiempoMs);
		notificarProgreso(100);
		return mejorEquipo;
	}

	public EstadisticasSolver getUltimasEstadisticas() {
		return ultimasEstadisticas;
	}

	/**
	 * @param indice    posición actual en la lista de personas
	 * @param actuales  personas seleccionadas hasta el momento
	 * @param puntaje   suma de calificaciones de las seleccionadas
	 */
	private void backtrack(int indice, List<Persona> actuales, int puntaje) {
		nodosVisitados++;
		if (reporteProgreso != null && nodosVisitados % INTERVALO_REPORTE_PROGRESO == 0
				&& nodosEstimados > 0) {
			int porcentaje = (int) Math.min(99, (nodosVisitados * 100L) / nodosEstimados);
			notificarProgreso(porcentaje);
		}

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

	private void notificarProgreso(int porcentaje) {
		if (reporteProgreso != null && porcentaje > ultimoPorcentajeReportado) {
			ultimoPorcentajeReportado = porcentaje;
			long tiempoMs = (System.nanoTime() - inicioEjecucionNano) / 1_000_000L;
			ultimasEstadisticas = new EstadisticasSolver(llamadasCasoBase, tiempoMs);
			reporteProgreso.accept(porcentaje);
		}
	}

	private long estimarNodos(int cantidadPersonas) {
		if (cantidadPersonas <= 0) {
			return 1;
		}
		int exponente = Math.min(cantidadPersonas, MAX_EXPONENTE_ESTIMACION);
		return 1L << exponente;
	}

}

