package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import modelo.Equipo;
import modelo.Persona;
import modelo.Rol;

public class EquipoTest {

	private Equipo equipo;
	private Persona juan;
	private Persona maria;
	private Persona carlos;

	@Before
	public void setUp() {
		equipo = new Equipo();
		juan = new Persona("Juan", Rol.PROGRAMADOR, 5);
		maria = new Persona("María", Rol.ARQUITECTO, 4);
		carlos = new Persona("Carlos", Rol.TESTER, 3);
	}

	@Test
	public void testEquipoVacio() {
		assertTrue(equipo.estaVacio());
		assertEquals(0, equipo.getIntegrantes().size());
	}

	@Test
	public void testAgregarPersona() {
		equipo.agregar(juan);
		assertFalse(equipo.estaVacio());
		assertEquals(1, equipo.getIntegrantes().size());
		assertTrue(equipo.getIntegrantes().contains(juan));
	}

	@Test
	public void testAgregarVariasPersonas() {
		equipo.agregar(juan);
		equipo.agregar(maria);
		equipo.agregar(carlos);
		assertEquals(3, equipo.getIntegrantes().size());
	}

	@Test
	public void testQuitarPersona() {
		equipo.agregar(juan);
		equipo.agregar(maria);
		equipo.quitar(juan);
		assertEquals(1, equipo.getIntegrantes().size());
		assertFalse(equipo.getIntegrantes().contains(juan));
		assertTrue(equipo.getIntegrantes().contains(maria));
	}

	@Test
	public void testCantidadEnRol() {
		Persona prog1 = new Persona("Prog1", Rol.PROGRAMADOR, 4);
		Persona prog2 = new Persona("Prog2", Rol.PROGRAMADOR, 3);
		equipo.agregar(prog1);
		equipo.agregar(prog2);
		equipo.agregar(juan); // PROGRAMADOR 5
		equipo.agregar(maria); // ARQUITECTO
		assertEquals(3, equipo.cantidadEnRol(Rol.PROGRAMADOR));
		assertEquals(1, equipo.cantidadEnRol(Rol.ARQUITECTO));
		assertEquals(0, equipo.cantidadEnRol(Rol.TESTER));
	}

	@Test
	public void testPuntajeTotal() {
		equipo.agregar(juan); // 5
		equipo.agregar(maria); // 4
		equipo.agregar(carlos); // 3
		assertEquals(12, equipo.puntajeTotal());
	}

	@Test
	public void testPuntajeTotalVacio() {
		assertEquals(0, equipo.puntajeTotal());
	}

	@Test
	public void testGetIntegrantesRetornaCopia() {
		equipo.agregar(juan);
		List<Persona> integrantes = equipo.getIntegrantes();
		integrantes.add(maria);
		assertEquals(1, equipo.getIntegrantes().size()); // El equipo no debe cambiar
	}

	@Test
	public void testConstructorConLista() {
		List<Persona> personas = new ArrayList<>();
		personas.add(juan);
		personas.add(maria);
		Equipo eq = new Equipo(personas);
		assertEquals(2, eq.getIntegrantes().size());
		assertTrue(eq.getIntegrantes().contains(juan));
		assertTrue(eq.getIntegrantes().contains(maria));
	}

	@Test
	public void testToString() {
		equipo.agregar(juan);
		String str = equipo.toString();
		assertTrue(str.contains("Juan"));
		assertTrue(str.contains("Puntaje total:"));
	}
}
