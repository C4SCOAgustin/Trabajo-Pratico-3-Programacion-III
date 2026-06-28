package modelo;

import java.io.Serializable;

public class Persona implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int CALIFICACION_MINIMA = 1;
	public static final int CALIFICACION_MAXIMA = 5;

	private final String nombre;
	private final Rol rol;
	private final int calificacion;

	public Persona(String nombre, Rol rol, int calificacion) {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede estar vacío");
		}
		if (rol == null) {
			throw new IllegalArgumentException("El rol no puede ser nulo");
		}
		if (calificacion < CALIFICACION_MINIMA || calificacion > CALIFICACION_MAXIMA) {
			throw new IllegalArgumentException("La calificación debe estar entre "
					+ CALIFICACION_MINIMA + " y " + CALIFICACION_MAXIMA);
		}
		this.nombre = nombre;
		this.rol = rol;
		this.calificacion = calificacion;
	}

	public String getNombre() {
		return nombre;
	}

	public Rol getRol() {
		return rol;
	}

	public int getCalificacion() {
		return calificacion;
	}

	@Override
	public String toString() {
		return nombre + " (" + rol + ", " + calificacion + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Persona persona = (Persona) o;
		return nombre.equals(persona.nombre);
	}

	@Override
	public int hashCode() {
		return nombre.hashCode();
	}
}
