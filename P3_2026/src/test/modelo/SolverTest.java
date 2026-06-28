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
		assertEquals(100, ultimoPorcentaje[0]);
	}

	@Test
	public void testConstructorCopiaListaDePersonas() {
		List<Persona> candidatos = new ArrayList<>();
		candidatos.add(new Persona("Programador bajo", Rol.PROGRAMADOR, 1));
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);
		Solver solver = new Solver(candidatos, gestor, requerimientos);

		candidatos.add(new Persona("Programador alto", Rol.PROGRAMADOR, 5));
		Equipo resultado = solver.resolver();

		assertEquals(1, resultado.getIntegrantes().size());
		assertEquals(1, resultado.puntajeTotal());
	}

	@Test
	public void testSolverEligeAlternativaCompatible() {
		Persona programador = new Persona("Programador", Rol.PROGRAMADOR, 5);
		Persona arquitectoAlto = new Persona("Arquitecto alto", Rol.ARQUITECTO, 5);
		Persona arquitectoBajo = new Persona("Arquitecto bajo", Rol.ARQUITECTO, 2);
		List<Persona> candidatos = new ArrayList<>();
		candidatos.add(programador);
		candidatos.add(arquitectoAlto);
		candidatos.add(arquitectoBajo);
		GestorIncompatibilidades gestorLocal = new GestorIncompatibilidades();
		gestorLocal.agregar(programador, arquitectoAlto);
		Requerimientos req = new Requerimientos();
		req.setCantidad(Rol.PROGRAMADOR, 1);
		req.setCantidad(Rol.ARQUITECTO, 1);

		Equipo resultado = new Solver(candidatos, gestorLocal, req).resolver();

		assertEquals(2, resultado.getIntegrantes().size());
		assertTrue(resultado.getIntegrantes().contains(programador));
		assertTrue(resultado.getIntegrantes().contains(arquitectoBajo));
		assertEquals(7, resultado.puntajeTotal());
	}

	@Test
	public void testSolverMaximizaPuntajeCombinado() {
		List<Persona> candidatos = new ArrayList<>();
		candidatos.add(new Persona("Prog bajo", Rol.PROGRAMADOR, 2));
		candidatos.add(new Persona("Prog alto", Rol.PROGRAMADOR, 5));
		candidatos.add(new Persona("Tester bajo", Rol.TESTER, 1));
		candidatos.add(new Persona("Tester alto", Rol.TESTER, 5));
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);
		requerimientos.setCantidad(Rol.TESTER, 1);

		Equipo resultado = new Solver(candidatos, gestor, requerimientos).resolver();

		assertEquals(2, resultado.getIntegrantes().size());
		assertEquals(10, resultado.puntajeTotal());
	}

	@Test
	public void testSolverRespetaCuposPorRol() {
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);

		Equipo resultado = new Solver(personas, gestor, requerimientos).resolver();

		assertEquals(1, resultado.getIntegrantes().size());
		assertEquals(1, resultado.cantidadEnRol(Rol.PROGRAMADOR));
		assertEquals(0, resultado.cantidadEnRol(Rol.ARQUITECTO));
	}

	@Test
	public void testResolverDosVecesReiniciaEstado() {
		requerimientos.setCantidad(Rol.PROGRAMADOR, 1);
		Solver solver = new Solver(personas, gestor, requerimientos);

		Equipo primerResultado = solver.resolver();
		long primerasLlamadas = solver.getUltimasEstadisticas().getLlamadasCasoBase();
		Equipo segundoResultado = solver.resolver();

		assertEquals(primerResultado.puntajeTotal(), segundoResultado.puntajeTotal());
		assertEquals(primerasLlamadas, solver.getUltimasEstadisticas().getLlamadasCasoBase());
	}

	@Test
	public void testSinSolucionTambienGeneraEstadisticas() {
		requerimientos.setCantidad(Rol.PROGRAMADOR, 10);
		Solver solver = new Solver(personas, gestor, requerimientos);

		Equipo resultado = solver.resolver();

		assertTrue(resultado.estaVacio());
		assertNotNull(solver.getUltimasEstadisticas());
		assertTrue(solver.getUltimasEstadisticas().getLlamadasCasoBase() > 0);
	}
}
