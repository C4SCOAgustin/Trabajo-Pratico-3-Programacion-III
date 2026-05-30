package modelo;

import java.util.ArrayList;
import java.util.List;

public class Equipo {

	private final List<Persona> integrantes = new ArrayList<>();

	public Equipo() {
	}

	public Equipo(List<Persona> integrantes) {
		this.integrantes.addAll(integrantes);
	}

	public void agregar(Persona p) {
		integrantes.add(p);
	}

	public void quitar(Persona p) {
		integrantes.remove(p);
	}

	public List<Persona> getIntegrantes() {
		return new ArrayList<>(integrantes);
	}

	public int cantidadEnRol(Rol rol) {
		int c = 0;
		for (Persona p : integrantes) {
			if (p.getRol() == rol) {
				c++;
			}
		}
		return c;
	}

	public int puntajeTotal() {
		int suma = 0;
		for (Persona p : integrantes) {
			suma += p.getCalificacion();
		}
		return suma;
	}

	public boolean estaVacio() {
		return integrantes.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Persona p : integrantes) {
			sb.append(p).append("\n");
		}
		sb.append("Puntaje total: ").append(puntajeTotal());
		return sb.toString();
	}
}
