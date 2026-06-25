package modelo;

/** Resultado de una ejecución del solver (éxito o error). */
public class ResultadoResolucion {

	private final Equipo equipo;
	private final EstadisticasSolver estadisticas;
	private final String mensajeError;

	private ResultadoResolucion(Equipo equipo, EstadisticasSolver estadisticas, String mensajeError) {
		this.equipo = equipo;
		this.estadisticas = estadisticas;
		this.mensajeError = mensajeError;
	}

	public static ResultadoResolucion exito(Equipo equipo, EstadisticasSolver estadisticas) {
		return new ResultadoResolucion(equipo, estadisticas, null);
	}

	public static ResultadoResolucion error(String mensaje) {
		return new ResultadoResolucion(null, null, mensaje != null ? mensaje : "Error desconocido");
	}

	public boolean esExito() {
		return mensajeError == null;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public EstadisticasSolver getEstadisticas() {
		return estadisticas;
	}

	public String getMensajeError() {
		return mensajeError;
	}
}

