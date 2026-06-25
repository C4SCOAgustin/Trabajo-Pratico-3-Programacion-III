package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import modelo.Equipo;
import modelo.EstadisticasSolver;
import modelo.Persona;
import modelo.ResultadoResolucion;
import modelo.Rol;

public class ResultadoResolucionTest {

	private Equipo equipo;
	private EstadisticasSolver estadisticas;

	@Before
	public void setUp() {
		equipo = new Equipo();
		equipo.agregar(new Persona("Juan", Rol.PROGRAMADOR, 5));
		equipo.agregar(new Persona("María", Rol.ARQUITECTO, 4));
		estadisticas = new EstadisticasSolver(100, 5000);
	}

	@Test
	public void testResultadoExito() {
		ResultadoResolucion resultado = ResultadoResolucion.exito(equipo, estadisticas);
		assertTrue(resultado.esExito());
		assertEquals(equipo, resultado.getEquipo());
		assertEquals(estadisticas, resultado.getEstadisticas());
		assertNull(resultado.getMensajeError());
	}

	@Test
	public void testResultadoError() {
		String mensaje = "No se encontró solución";
		ResultadoResolucion resultado = ResultadoResolucion.error(mensaje);
		assertFalse(resultado.esExito());
		assertNull(resultado.getEquipo());
		assertNull(resultado.getEstadisticas());
		assertEquals(mensaje, resultado.getMensajeError());
	}

	@Test
	public void testResultadoErrorNull() {
		ResultadoResolucion resultado = ResultadoResolucion.error(null);
		assertFalse(resultado.esExito());
		assertEquals("Error desconocido", resultado.getMensajeError());
	}

	@Test
	public void testResultadoErrorMensajeVacio() {
		ResultadoResolucion resultado = ResultadoResolucion.error("");
		assertFalse(resultado.esExito());
		assertEquals("", resultado.getMensajeError());
	}

	@Test
	public void testResultadoExitoConEquipoVacio() {
		Equipo equipoVacio = new Equipo();
		ResultadoResolucion resultado = ResultadoResolucion.exito(equipoVacio, estadisticas);
		assertTrue(resultado.esExito());
		assertEquals(equipoVacio, resultado.getEquipo());
		assertTrue(resultado.getEquipo().estaVacio());
	}

	@Test
	public void testResultadoExitoConEstadisticasCero() {
		EstadisticasSolver estadisticasCero = new EstadisticasSolver(0, 0);
		ResultadoResolucion resultado = ResultadoResolucion.exito(equipo, estadisticasCero);
		assertTrue(resultado.esExito());
		assertEquals(0, resultado.getEstadisticas().getLlamadasCasoBase());
		assertEquals(0, resultado.getEstadisticas().getTiempoMillis());
	}
}
