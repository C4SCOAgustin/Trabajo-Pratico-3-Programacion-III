package test.modelo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import modelo.GestorIncompatibilidades;
import modelo.Incompatibilidad;
import modelo.ModeloObserver;
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

	@Test(expected = IllegalArgumentException.class)
	public void testAgregarIncompatibilidadDuplicada() {
		gestor.agregar(new Incompatibilidad(juan, maria));
		gestor.agregar(new Incompatibilidad(maria, juan));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAgregarIncompatibilidadEntreLaMismaPersona() {
		gestor.agregar(juan, juan);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEliminarIncompatibilidadInexistenteObligatoria() {
		gestor.eliminarObligatoria(juan, maria);
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

	@Test
	public void testAgregarPorPersonas() {
		gestor.agregar(juan, maria);

		assertTrue(gestor.sonIncompatibles(juan, maria));
		assertEquals(1, gestor.getIncompatibilidades().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAgregarIncompatibilidadNull() {
		gestor.agregar((Incompatibilidad) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAgregarConPrimeraPersonaNull() {
		gestor.agregar(null, maria);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAgregarConSegundaPersonaNull() {
		gestor.agregar(juan, null);
	}

	@Test
	public void testEliminarObligatoriaEliminaExistente() {
		gestor.agregar(juan, maria);

		gestor.eliminarObligatoria(juan, maria);

		assertFalse(gestor.sonIncompatibles(juan, maria));
		assertTrue(gestor.getIncompatibilidades().isEmpty());
	}

	@Test
	public void testEliminarInvolucrandoPersonaSinRelacionesNoCambia() {
		gestor.agregar(juan, maria);

		gestor.eliminarInvolucrando(carlos);

		assertEquals(1, gestor.getIncompatibilidades().size());
		assertTrue(gestor.sonIncompatibles(juan, maria));
	}

	@Test
	public void testLimpiarGestorVacio() {
		gestor.limpiar();

		assertTrue(gestor.getIncompatibilidades().isEmpty());
	}

	@Test
	public void testCopiarCreaGestorIndependiente() {
		gestor.agregar(juan, maria);
		GestorIncompatibilidades copia = gestor.copiar();

		copia.agregar(carlos, ana);

		assertEquals(1, gestor.getIncompatibilidades().size());
		assertEquals(2, copia.getIncompatibilidades().size());
	}

	@Test
	public void testObserverRecibeCambiosDeIncompatibilidades() {
		ObserverPrueba observer = new ObserverPrueba();
		gestor.agregarObserver(observer);

		gestor.agregar(juan, maria);
		gestor.eliminar(juan, maria);

		assertEquals(2, observer.cambiosIncompatibilidades);
		assertTrue(observer.ultimasIncompatibilidades.isEmpty());
	}

	@Test
	public void testObserverNoSeNotificaAlEliminarInexistente() {
		ObserverPrueba observer = new ObserverPrueba();
		gestor.agregarObserver(observer);

		assertFalse(gestor.eliminar(juan, maria));

		assertEquals(0, observer.cambiosIncompatibilidades);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAgregarObserverNull() {
		gestor.agregarObserver(null);
	}

	private static class ObserverPrueba implements ModeloObserver {
		private int cambiosIncompatibilidades;
		private List<Incompatibilidad> ultimasIncompatibilidades = new ArrayList<>();

		public void onPersonasCambiadas(List<Persona> personas) {
		}

		public void onIncompatibilidadesCambiadas(List<Incompatibilidad> incompatibilidades) {
			cambiosIncompatibilidades++;
			ultimasIncompatibilidades = new ArrayList<>(incompatibilidades);
		}
	}
}
