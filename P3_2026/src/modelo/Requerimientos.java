package modelo;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

public class Requerimientos implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Map<Rol, Integer> cantidades = new EnumMap<>(Rol.class);

	public Requerimientos() {
		for (Rol r : Rol.values()) {
			cantidades.put(r, 0);
		}
	}

	public void setCantidad(Rol rol, int cantidad) {
		if (rol == null) {
			throw new IllegalArgumentException("El rol no puede ser nulo");
		}
		if (cantidad < 0) {
			throw new IllegalArgumentException("La cantidad no puede ser negativa");
		}
		cantidades.put(rol, cantidad);
	}

	public int getCantidad(Rol rol) {
		return cantidades.get(rol);
	}

	/** Total de personas requeridas en todos los roles. */
	public int total() {
		int suma = 0;
		for (int c : cantidades.values()) {
			suma += c;
		}
		return suma;
	}

	/** Copia las cantidades desde otro objeto de requerimientos. */
	public void copiarDesde(Requerimientos otros) {
		for (Rol r : Rol.values()) {
			cantidades.put(r, otros.getCantidad(r));
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Rol r : Rol.values()) {
			sb.append(r.getNombre()).append(": ").append(cantidades.get(r)).append("  ");
		}
		return sb.toString().trim();
	}
}

