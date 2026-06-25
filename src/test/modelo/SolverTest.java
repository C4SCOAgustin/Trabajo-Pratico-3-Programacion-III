package test.modelo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import modelo.Equipo;
import modelo.GestorIncompatibilidades;
import modelo.Persona;
import modelo.Requerimientos;
import modelo.Rol;
import modelo.Solver;

public class SolverTest {

	private List<Persona> personas;
	private GestorIncompatibilidades gestor;
	private Requerimientos requerimientos;

	@Before
	public void setUp() {
		personas = new ArrayList<>();
		personas.add(new Persona("Juan", Rol.PROGRAMADOR, 5));
		personas.add(new Persona("María", Rol.ARQUITECTO, 4));
		personas.add(new Persona("Carlos", Rol.TESTER, 3));
		personas.add(new Persona("Ana", Rol.LIDER_DE_PROYECTO, 5));

		gestor = new GestorIncompatibilidades();
		requerimientos = new Requerimientos();
	}

	@Test
	public void testSolverSinRequerimientos() {
		Solver solver = new Solver(personas, gestor, requerimientos);
		Equipo resultado = solver.resolver();
		assertNotNull(resultado);
		assertTrue(resultado.estaVacio());
	}

	@Test
	public void testSolverConUnRequerimiento() {
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);
		Solver solver = new Solver(personas, gestor, requerimientos);
		Equipo resultado = solver.resolver();
		
		assertNotNull(resultado);
		assertEquals(1, resultado.getIntegrantes().size());
		assertEquals(1, resultado.cantidadEnRol(Rol.PROGRAMADOR));
		assertEquals(5, resultado.puntajeTotal()); // Juan tiene calificación 5
	}

	@Test
	public void testSolverMultiplesRequerimientos() {
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);
		requerimientos.setCantidad(Rol.ARQUITECTO, 1);
		requerimientos.setCantidad(Rol.TESTER, 1);
		requerimientos.setCantidad(Rol.LIDER_DE_PROYECTO, 1);

		Solver solver = new Solver(personas, gestor, requerimientos);
		Equipo resultado = solver.resolver();

		assertNotNull(resultado);
		assertEquals(4, resultado.getIntegrantes().size());
		assertEquals(1, resultado.cantidadEnRol(Rol.PROGRAMADOR));
		assertEquals(1, resultado.cantidadEnRol(Rol.ARQUITECTO));
		assertEquals(1, resultado.cantidadEnRol(Rol.TESTER));
		assertEquals(1, resultado.cantidadEnRol(Rol.LIDER_DE_PROYECTO));
	}

	@Test
	public void testSolverConIncompatibilidades() {
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);
		requerimientos.setCantidad(Rol.ARQUITECTO, 1);

		// Juan y María son incompatibles
		gestor.agregar(new modelo.Incompatibilidad(personas.get(0), personas.get(1)));

		Solver solver = new Solver(personas, gestor, requerimientos);
		Equipo resultado = solver.resolver();

		// No debe haber solución porque Juan y María no pueden estar juntos
		assertTrue(resultado.estaVacio());
	}

	@Test
	public void testSolverMaximizaPuntaje() {
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);

		Solver solver = new Solver(personas, gestor, requerimientos);
		Equipo resultado = solver.resolver();

		// Debe seleccionar a Juan (calificación 5) en lugar de otro programador
		assertEquals(5, resultado.puntajeTotal());
	}

	@Test
	public void testSolverSinSolucion() {
		requerimientos.setCantidad(Rol.PROGRAMADOR, 3); // Necesita 3 programadores, solo hay 1

		Solver solver = new Solver(personas, gestor, requerimientos);
		Equipo resultado = solver.resolver();

		assertTrue(resultado.estaVacio());
	}

	@Test
	public void testSolverListaVacia() {
		List<Persona> personasVacio = new ArrayList<>();
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);

		Solver solver = new Solver(personasVacio, gestor, requerimientos);
		Equipo resultado = solver.resolver();

		assertTrue(resultado.estaVacio());
	}

	@Test
	public void testGetUltimasEstadisticas() {
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);
		Solver solver = new Solver(personas, gestor, requerimientos);
		Equipo resultado = solver.resolver();

		assertNotNull(solver.getUltimasEstadisticas());
		assertTrue(solver.getUltimasEstadisticas().getLlamadasCasoBase() > 0);
	}

	@Test
	public void testSolverConReporteProgreso() {
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);
		
		final int[] ultimoPorcentaje = {0};
		Solver solver = new Solver(personas, gestor, requerimientos, porcentaje -> {
			ultimoPorcentaje[0] = porcentaje;
		});
		
		Equipo resultado = solver.resolver();
		assertNotNull(resultado);
	}
}
