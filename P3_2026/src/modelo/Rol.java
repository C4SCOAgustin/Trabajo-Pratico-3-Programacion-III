package modelo;

public enum Rol {
	LIDER_DE_PROYECTO("Líder de proyecto"),
	ARQUITECTO("Arquitecto"),
	PROGRAMADOR("Programador"),
	TESTER("Tester");

	private final String nombre;

	Rol(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
