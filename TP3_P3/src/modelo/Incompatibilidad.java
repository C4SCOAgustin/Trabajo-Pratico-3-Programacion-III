package modelo;

public class Incompatibilidad {

	private final Persona a;
	private final Persona b;

	public Incompatibilidad(Persona a, Persona b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException("Las personas no pueden ser nulas");
		}
		if (a.equals(b)) {
			throw new IllegalArgumentException("Una persona no puede ser incompatible consigo misma");
		}
		this.a = a;
		this.b = b;
	}

	public Persona getA() {
		return a;
	}

	public Persona getB() {
		return b;
	}

	/** Indica si esta incompatibilidad involucra a las dos personas dadas (sin importar el orden). */
	public boolean involucra(Persona p1, Persona p2) {
		return (a.equals(p1) && b.equals(p2)) || (a.equals(p2) && b.equals(p1));
	}

	@Override
	public String toString() {
		return a.getNombre() + " <-> " + b.getNombre();
	}
}
