package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import modelo.GestorIncompatibilidades;
import modelo.Incompatibilidad;
import modelo.Persona;
import modelo.Rol;

public class GestorIncompatibilidadesTest {

	private GestorIncompatibilidades gestor;
	private Persona juan;
	private Persona maria;
	private Persona carlos;
	private Persona ana;

	@Before
	public void setUp() {
		gestor = new GestorIncompatibilidades();
		juan = new Persona("Juan", Rol.PROGRAMADOR, 5);
		maria = new Persona("María", Rol.ARQUITECTO, 4);
		carlos = new Persona("Carlos", Rol.TESTER, 3);
		ana = new Persona("Ana", Rol.LIDER_DE_PROYECTO, 5);
	}

	@Test
	public void testGestorVacio() {
		assertEquals(0, gestor.getIncompatibilidades().size());
	}

	@Test
	public void testAgregarIncompatibilidad() {
		Incompatibilidad i = new Incompatibilidad(juan, maria);
		gestor.agregar(i);
		assertEquals(1, gestor.getIncompatibilidades().size());
	}

	@Test
	public void testAgregarMultiplesIncompatibilidades() {
		gestor.agregar(new Incompatibilidad(juan, maria));
		gestor.agregar(new Incompatibilidad(juan, carlos));
		gestor.agregar(new Incompatibilidad(maria, carlos));
		assertEquals(3, gestor.getIncompatibilidades().size());
	}

	@Test
	public void testSonIncompatibles() {
		gestor.agregar(new Incompatibilidad(juan, maria));
		assertTrue(gestor.sonIncompatibles(juan, maria));
		assertTrue(gestor.sonIncompatibles(maria, juan)); // Sin importar orden
		assertFalse(gestor.sonIncompatibles(juan, carlos));
	}

	@Test
	public void testSonIncompatiblesGestorVacio() {
		assertFalse(gestor.sonIncompatibles(juan, maria));
	}

	@Test
	public void testEliminar() {
		gestor.agregar(new Incompatibilidad(juan, maria));
		gestor.agregar(new Incompatibilidad(carlos, ana));
		
		assertTrue(gestor.eliminar(juan, maria));
		assertEquals(1, gestor.getIncompatibilidades().size());
		assertFalse(gestor.sonIncompatibles(juan, maria));
	}

	@Test
	public void testEliminarInverso() {
		gestor.agregar(new Incompatibilidad(juan, maria));
		assertTrue(gestor.eliminar(maria, juan)); // Orden inverso
		assertFalse(gestor.sonIncompatibles(juan, maria));
	}

	@Test
	public void testEliminarNoExistente() {
		assertFalse(gestor.eliminar(juan, maria));
	}

	@Test
	public void testEliminarInvolucrando() {
		gestor.agregar(new Incompatibilidad(juan, maria));
		gestor.agregar(new Incompatibilidad(juan, carlos));
		gestor.agregar(new Incompatibilidad(maria, carlos));

		gestor.eliminarInvolucrando(juan);
		assertEquals(1, gestor.getIncompatibilidades().size());
		assertFalse(gestor.sonIncompatibles(juan, maria));
		assertFalse(gestor.sonIncompatibles(juan, carlos));
		assertTrue(gestor.sonIncompatibles(maria, carlos));
	}

	@Test
	public void testLimpiar() {
		gestor.agregar(new Incompatibilidad(juan, maria));
		gestor.agregar(new Incompatibilidad(carlos, ana));
		assertEquals(2, gestor.getIncompatibilidades().size());
		
		gestor.limpiar();
		assertEquals(0, gestor.getIncompatibilidades().size());
	}

	@Test
	public void testEsIncompatibleCon() {
		List<Persona> seleccionados = new ArrayList<>();
		seleccionados.add(juan);
		seleccionados.add(carlos);

		gestor.agregar(new Incompatibilidad(maria, juan));
		assertTrue(gestor.esIncompatibleCon(maria, seleccionados));

		gestor.agregar(new Incompatibilidad(ana, carlos));
		assertTrue(gestor.esIncompatibleCon(ana, seleccionados));
	}

	@Test
	public void testEsIncompatibleConFalso() {
		List<Persona> seleccionados = new ArrayList<>();
		seleccionados.add(juan);

		gestor.agregar(new Incompatibilidad(maria, carlos));
		assertFalse(gestor.esIncompatibleCon(ana, seleccionados));
	}

	@Test
	public void testEsIncompatibleConVacio() {
		List<Persona> seleccionados = new ArrayList<>();
		assertFalse(gestor.esIncompatibleCon(juan, seleccionados));
	}

	@Test
	public void testGetIncompatibilidadesRetornaCopia() {
		Incompatibilidad i = new Incompatibilidad(juan, maria);
		gestor.agregar(i);
		List<Incompatibilidad> lista = gestor.getIncompatibilidades();
		lista.add(new Incompatibilidad(carlos, ana));
		assertEquals(1, gestor.getIncompatibilidades().size()); // No debe cambiar
	}
}
