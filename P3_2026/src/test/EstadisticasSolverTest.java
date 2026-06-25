package test;

import static org.junit.Assert.*;

import org.junit.Test;

import modelo.EstadisticasSolver;

public class EstadisticasSolverTest {

	@Test
	public void testCreacionEstadisticas() {
		EstadisticasSolver est = new EstadisticasSolver(100, 5000);
		assertEquals(100, est.getLlamadasCasoBase());
		assertEquals(5000, est.getTiempoMillis());
	}

	@Test
	public void testTiempoSegundos() {
		EstadisticasSolver est = new EstadisticasSolver(100, 5000);
		assertEquals(5.0, est.getTiempoSegundos(), 0.01);
	}

	@Test
	public void testTiempoSegundosCero() {
		EstadisticasSolver est = new EstadisticasSolver(100, 0);
		assertEquals(0.0, est.getTiempoSegundos(), 0.01);
	}

	@Test
	public void testTiempoSegundosDecimales() {
		EstadisticasSolver est = new EstadisticasSolver(100, 1500);
		assertEquals(1.5, est.getTiempoSegundos(), 0.01);
	}

	@Test
	public void testCerosCasoBase() {
		EstadisticasSolver est = new EstadisticasSolver(0, 1000);
		assertEquals(0, est.getLlamadasCasoBase());
	}

	@Test
	public void testValoresGrandes() {
		EstadisticasSolver est = new EstadisticasSolver(1000000, 60000);
		assertEquals(1000000, est.getLlamadasCasoBase());
		assertEquals(60000, est.getTiempoMillis());
		assertEquals(60.0, est.getTiempoSegundos(), 0.01);
	}

	@Test
	public void testToString() {
		EstadisticasSolver est = new EstadisticasSolver(100, 5000);
		String str = est.toString();
		assertNotNull(str);
		assertTrue(str.contains("100"));
		assertTrue(str.contains("5"));
	}
}
