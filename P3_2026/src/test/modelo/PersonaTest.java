package test.modelo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import modelo.Persona;
import modelo.Rol;

public class PersonaTest {

	private Persona persona1;
	private Persona persona2;

	@Before
	public void setUp() {
		persona1 = new Persona("Juan", Rol.PROGRAMADOR, 5);
		persona2 = new Persona("María", Rol.ARQUITECTO, 4);
	}

	@Test
	public void testCreacionPersonaValida() {
		assertNotNull(persona1);
		assertEquals("Juan", persona1.getNombre());
		assertEquals(Rol.PROGRAMADOR, persona1.getRol());
		assertEquals(5, persona1.getCalificacion());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNombreVacio() {
		new Persona("", Rol.PROGRAMADOR, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNombreNull() {
		new Persona(null, Rol.PROGRAMADOR, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNombreSoloEspacios() {
		new Persona("   ", Rol.PROGRAMADOR, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRolNull() {
		new Persona("Pedro", null, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalificacionMenorA1() {
		new Persona("Pedro", Rol.TESTER, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalificacionMayorA5() {
		new Persona("Pedro", Rol.TESTER, 6);
	}

	@Test
	public void testCalificacionMinima() {
		Persona p = new Persona("Ana", Rol.LIDER_DE_PROYECTO, 1);
		assertEquals(1, p.getCalificacion());
	}

	@Test
	public void testCalificacionMaxima() {
		Persona p = new Persona("Ana", Rol.LIDER_DE_PROYECTO, 5);
		assertEquals(5, p.getCalificacion());
	}

	@Test
	public void testEquals() {
		Persona p1 = new Persona("Juan", Rol.PROGRAMADOR, 5);
		Persona p2 = new Persona("Juan", Rol.ARQUITECTO, 3);
		Persona p3 = new Persona("Carlos", Rol.PROGRAMADOR, 5);

		assertEquals(p1, p2); // Igualdad solo por nombre
		assertNotEquals(p1, p3);
		assertEquals(p1, p1); // Reflexividad
	}

	@Test
	public void testHashCode() {
		Persona p1 = new Persona("Juan", Rol.PROGRAMADOR, 5);
		Persona p2 = new Persona("Juan", Rol.ARQUITECTO, 3);
		assertEquals(p1.hashCode(), p2.hashCode()); // Si son iguales, deben tener el mismo hashCode
	}

	@Test
	public void testToString() {
		String str = persona1.toString();
		assertTrue(str.contains("Juan"));
		assertTrue(str.contains("Programador"));
		assertTrue(str.contains("5"));
	}

	@Test
	public void testConstantesDeCalificacion() {
		assertEquals(1, Persona.CALIFICACION_MINIMA);
		assertEquals(5, Persona.CALIFICACION_MAXIMA);
	}

	@Test
	public void testCalificacionesIntermedias() {
		for (int calificacion = Persona.CALIFICACION_MINIMA;
				calificacion <= Persona.CALIFICACION_MAXIMA; calificacion++) {
			Persona persona = new Persona("Persona" + calificacion, Rol.TESTER, calificacion);
			assertEquals(calificacion, persona.getCalificacion());
		}
	}

	@Test
	public void testEqualsConNullYOtroTipo() {
		assertNotEquals(persona1, null);
		assertNotEquals(persona1, "Juan");
	}

	@Test
	public void testEqualsNoDependeDeRolNiCalificacion() {
		Persona mismaPersonaOtroRol = new Persona("Juan", Rol.ARQUITECTO, 1);

		assertEquals(persona1, mismaPersonaOtroRol);
	}

	@Test
	public void testEqualsDistingueMayusculasYMinusculas() {
		Persona juanMinuscula = new Persona("juan", Rol.PROGRAMADOR, 5);

		assertNotEquals(persona1, juanMinuscula);
	}

	@Test
	public void testToStringIncluyeRolLegibleYCalificacion() {
		String str = persona2.toString();

		assertTrue(str.contains(persona2.getNombre()));
		assertTrue(str.contains(persona2.getRol().toString()));
		assertTrue(str.contains(String.valueOf(persona2.getCalificacion())));
	}
}
