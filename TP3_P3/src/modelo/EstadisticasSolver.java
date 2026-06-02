package modelo;

import java.io.Serializable;
import java.util.Locale;

/** Métricas recopiladas durante la ejecución del algoritmo de backtracking. */
public class EstadisticasSolver implements Serializable {

	private static final long serialVersionUID = 1L;

	private final long llamadasCasoBase;
	private final long tiempoMillis;

	public EstadisticasSolver(long llamadasCasoBase, long tiempoMillis) {
		this.llamadasCasoBase = llamadasCasoBase;
		this.tiempoMillis = tiempoMillis;
	}

	public long getLlamadasCasoBase() {
		return llamadasCasoBase;
	}

	public long getTiempoMillis() {
		return tiempoMillis;
	}

	public double getTiempoSegundos() {
		return tiempoMillis / 1000.0;
	}

	@Override
	public String toString() {
		return String.format(Locale.US,
				"Estadísticas del algoritmo:%n  Casos base evaluados: %d%n  Tiempo total: %.3f seg",
				llamadasCasoBase, getTiempoSegundos());
	}
}
