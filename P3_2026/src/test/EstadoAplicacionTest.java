package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import modelo.EstadoAplicacion;
import modelo.Persona;
import modelo.Requerimientos;
import modelo.Rol;

public class EstadoAplicacionTest {

	private List<Persona> personas;
	private List<String[]> paresIncompatibles;
	private Requerimientos requerimientos;

	@Before
	public void setUp() {
		personas = new ArrayList<>();
		personas.add(new Persona("Juan", Rol.PROGRAMADOR, 5));
		personas.add(new Persona("María", Rol.ARQUITECTO, 4));

		paresIncompatibles = new ArrayList<>();
		paresIncompatibles.add(new String[] { "Juan", "María" });

		requerimientos = new Requerimientos();
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);
		requerimientos.setCantidad(Rol.ARQUITECTO, 1);
	}

	@Test
	public void testCreacionEstadoAplicacion() {
		EstadoAplicacion estado = new EstadoAplicacion(personas, paresIncompatibles, requerimientos);
		assertNotNull(estado);
		assertEquals(2, estado.getPersonas().size());
		assertEquals(1, estado.getParesIncompatibles().size());
		assertNotNull(estado.getRequerimientos());
	}

	@Test
	public void testGetPersonas() {
		EstadoAplicacion estado = new EstadoAplicacion(personas, paresIncompatibles, requerimientos);
		List<Persona> personasRecuperadas = estado.getPersonas();
		assertEquals(2, personasRecuperadas.size());
		assertEquals("Juan", personasRecuperadas.get(0).getNombre());
		assertEquals("María", personasRecuperadas.get(1).getNombre());
	}

	@Test
	public void testGetParesIncompatibles() {
		EstadoAplicacion estado = new EstadoAplicacion(personas, paresIncompatibles, requerimientos);
		List<String[]> paresRecuperados = estado.getParesIncompatibles();
		assertEquals(1, paresRecuperados.size());
		assertEquals("Juan", paresRecuperados.get(0)[0]);
		assertEquals("María", paresRecuperados.get(0)[1]);
	}

	@Test
	public void testGetRequerimientos() {
		EstadoAplicacion estado = new EstadoAplicacion(personas, paresIncompatibles, requerimientos);
		Requerimientos reqRecuperados = estado.getRequerimientos();
		assertNotNull(reqRecuperados);
		assertEquals(1, reqRecuperados.getCantidad(Rol.PROGRAMADOR));
		assertEquals(1, reqRecuperados.getCantidad(Rol.ARQUITECTO));
	}

	@Test
	public void testGetPersonasRetornaCopia() {
		EstadoAplicacion estado = new EstadoAplicacion(personas, paresIncompatibles, requerimientos);
		List<Persona> personasRecuperadas = estado.getPersonas();
		personasRecuperadas.add(new Persona("Otro", Rol.TESTER, 3));
		assertEquals(2, estado.getPersonas().size()); // No debe cambiar
	}

	@Test
	public void testGetParesIncompatiblesRetornaCopia() {
		EstadoAplicacion estado = new EstadoAplicacion(personas, paresIncompatibles, requerimientos);
		List<String[]> paresRecuperados = estado.getParesIncompatibles();
		paresRecuperados.add(new String[] { "Otro", "Otro2" });
		assertEquals(1, estado.getParesIncompatibles().size()); // No debe cambiar
	}

	@Test
	public void testEstadoVacio() {
		List<Persona> personasVacio = new ArrayList<>();
		List<String[]> paresVacio = new ArrayList<>();
		EstadoAplicacion estado = new EstadoAplicacion(personasVacio, paresVacio, new Requerimientos());

		assertEquals(0, estado.getPersonas().size());
		assertEquals(0, estado.getParesIncompatibles().size());
	}

	@Test
	public void testMultiplesParesIncompatibles() {
		paresIncompatibles.add(new String[] { "María", "Juan" });
		paresIncompatibles.add(new String[] { "Juan", "Carlos" });
		EstadoAplicacion estado = new EstadoAplicacion(personas, paresIncompatibles, requerimientos);
		assertEquals(3, estado.getParesIncompatibles().size());
	}
}
