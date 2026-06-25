package test.modelo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import modelo.Incompatibilidad;
import modelo.Persona;
import modelo.Rol;

public class IncompatibilidadTest {

	private Persona juan;
	private Persona maria;
	private Persona carlos;
	private Incompatibilidad incomp;

	@Before
	public void setUp() {
		juan = new Persona("Juan", Rol.PROGRAMADOR, 5);
		maria = new Persona("María", Rol.ARQUITECTO, 4);
		carlos = new Persona("Carlos", Rol.TESTER, 3);
		incomp = new Incompatibilidad(juan, maria);
	}

	@Test
	public void testCreacionIncompatibilidadValida() {
		assertNotNull(incomp);
		assertEquals(juan, incomp.getA());
		assertEquals(maria, incomp.getB());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPersonaNullaA() {
		new Incompatibilidad(null, maria);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPersonaNullaB() {
		new Incompatibilidad(juan, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPersonasIguales() {
		new Incompatibilidad(juan, juan);
	}

	@Test
	public void testInvolucraPersona() {
		assertTrue(incomp.involucra(juan));
		assertTrue(incomp.involucra(maria));
		assertFalse(incomp.involucra(carlos));
	}

	@Test
	public void testInvolucraDosPersonasEnOrden() {
		assertTrue(incomp.involucra(juan, maria));
	}

	@Test
	public void testInvolucraDosPersonasEnOrdenInverso() {
		assertTrue(incomp.involucra(maria, juan));
	}

	@Test
	public void testInvolucraDosPersonasNoPresentes() {
		assertFalse(incomp.involucra(juan, carlos));
		assertFalse(incomp.involucra(maria, carlos));
		assertFalse(incomp.involucra(carlos, juan));
	}

	@Test
	public void testToString() {
		String str = incomp.toString();
		assertTrue(str.contains("Juan"));
		assertTrue(str.contains("María"));
		assertTrue(str.contains("<->"));
	}
}
