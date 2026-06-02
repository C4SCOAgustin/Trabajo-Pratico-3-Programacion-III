package controlador;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import modelo.GestorIncompatibilidades;
import modelo.Incompatibilidad;
import modelo.Persona;
import modelo.Requerimientos;
import modelo.Rol;

/** Genera empleados e incompatibilidades aleatorias para pruebas. */
public final class GeneradorDatosPrueba {

	public static final int EMPLEADOS_DEFAULT = 32;
	public static final int INCOMPATIBILIDADES_DEFAULT = 20;

	private GeneradorDatosPrueba() {
	}

	public static void generar(List<Persona> personas, GestorIncompatibilidades gestor,
			Requerimientos requerimientos, int cantidadEmpleados, int cantidadIncompatibilidades) {
		personas.clear();
		gestor.limpiar();

		Random random = new Random();
		Rol[] roles = Rol.values();

		for (int i = 1; i <= cantidadEmpleados; i++) {
			Rol rol = roles[random.nextInt(roles.length)];
			int calificacion = 1 + random.nextInt(5);
			personas.add(new Persona("Empleado_" + i, rol, calificacion));
		}

		agregarIncompatibilidadesAleatorias(personas, gestor, cantidadIncompatibilidades, random);

		aplicarRequerimientosEjemplo(requerimientos);
	}

	private static void agregarIncompatibilidadesAleatorias(List<Persona> personas,
			GestorIncompatibilidades gestor, int cantidad, Random random) {
		if (personas.size() < 2 || cantidad <= 0) {
			return;
		}

		Set<String> paresUsados = new HashSet<>();
		int intentos = 0;
		int maxIntentos = cantidad * 20;

		while (paresUsados.size() < cantidad && intentos < maxIntentos) {
			intentos++;
			int i = random.nextInt(personas.size());
			int j = random.nextInt(personas.size());
			if (i == j) {
				continue;
			}

			Persona a = personas.get(i);
			Persona b = personas.get(j);
			String clave = clavePar(a.getNombre(), b.getNombre());
			if (!paresUsados.add(clave)) {
				continue;
			}

			gestor.agregar(new Incompatibilidad(a, b));
		}
	}

	private static String clavePar(String nombreA, String nombreB) {
		if (nombreA.compareTo(nombreB) <= 0) {
			return nombreA + "|" + nombreB;
		}
		return nombreB + "|" + nombreA;
	}

	private static void aplicarRequerimientosEjemplo(Requerimientos requerimientos) {
		requerimientos.setCantidad(Rol.LIDER_DE_PROYECTO, 1);
		requerimientos.setCantidad(Rol.ARQUITECTO, 2);
		requerimientos.setCantidad(Rol.PROGRAMADOR, 4);
		requerimientos.setCantidad(Rol.TESTER, 5);
	}
}
