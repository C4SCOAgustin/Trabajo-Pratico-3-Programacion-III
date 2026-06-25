package test.modelo;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import modelo.AlmacenPersonas;
import modelo.Persona;
import modelo.Rol;

public class AlmacenPersonasTest {

	private Path rutaTest;

	@Before
	public void setUp() {
		rutaTest = Paths.get("test_personas.json");
	}

	@After
	public void tearDown() {
		try {
			if (Files.exists(rutaTest)) {
				Files.delete(rutaTest);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGuardarYCargarVacio() throws IOException {
		List<Persona> personas = new ArrayList<>();
		AlmacenPersonas.guardar(personas, rutaTest);
		List<Persona> cargadas = AlmacenPersonas.cargar(rutaTest);
		assertEquals(0, cargadas.size());
	}

	@Test
	public void testGuardarYCargarUnaPersona() throws IOException {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona("Juan", Rol.PROGRAMADOR, 5));

		AlmacenPersonas.guardar(personas, rutaTest);
		List<Persona> cargadas = AlmacenPersonas.cargar(rutaTest);

		assertEquals(1, cargadas.size());
		assertEquals("Juan", cargadas.get(0).getNombre());
		assertEquals(Rol.PROGRAMADOR, cargadas.get(0).getRol());
		assertEquals(5, cargadas.get(0).getCalificacion());
	}

	@Test
	public void testGuardarYCargarMultiplesPersonas() throws IOException {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona("Juan", Rol.PROGRAMADOR, 5));
		personas.add(new Persona("María", Rol.ARQUITECTO, 4));
		personas.add(new Persona("Carlos", Rol.TESTER, 3));
		personas.add(new Persona("Ana", Rol.LIDER_DE_PROYECTO, 2));

		AlmacenPersonas.guardar(personas, rutaTest);
		List<Persona> cargadas = AlmacenPersonas.cargar(rutaTest);

		assertEquals(4, cargadas.size());
		assertEquals("Juan", cargadas.get(0).getNombre());
		assertEquals("María", cargadas.get(1).getNombre());
		assertEquals("Carlos", cargadas.get(2).getNombre());
		assertEquals("Ana", cargadas.get(3).getNombre());
	}

	@Test
	public void testGuardarPersonasConNombresEspeciales() throws IOException {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona("José María", Rol.PROGRAMADOR, 5));
		personas.add(new Persona("D'Angelo", Rol.ARQUITECTO, 4));

		AlmacenPersonas.guardar(personas, rutaTest);
		List<Persona> cargadas = AlmacenPersonas.cargar(rutaTest);

		assertEquals(2, cargadas.size());
		assertEquals("José María", cargadas.get(0).getNombre());
		assertEquals("D'Angelo", cargadas.get(1).getNombre());
	}

	@Test
	public void testCargarArchivoNoExistente() throws IOException {
		List<Persona> cargadas = AlmacenPersonas.cargar(Paths.get("no_existe.json"));
		assertEquals(0, cargadas.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGuardarNull() throws IOException {
		AlmacenPersonas.guardar(null, rutaTest);
	}

	@Test
	public void testGuardarConDirectoriosNoExistentes() throws IOException {
		Path ruta = Paths.get("test_dir", "subdir", "personas.json");
		try {
			List<Persona> personas = new ArrayList<>();
			personas.add(new Persona("Test", Rol.PROGRAMADOR, 1));
			AlmacenPersonas.guardar(personas, ruta);
			assertTrue(Files.exists(ruta));
		} finally {
			// Limpiar
			try {
				if (Files.exists(ruta)) Files.delete(ruta);
				Path parent = ruta.getParent();
				if (parent != null && Files.exists(parent)) Files.delete(parent);
				parent = parent.getParent();
				if (parent != null && Files.exists(parent)) Files.delete(parent);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testContenidoJSON() throws IOException {
		List<Persona> personas = new ArrayList<>();
		personas.add(new Persona("Test", Rol.PROGRAMADOR, 3));

		AlmacenPersonas.guardar(personas, rutaTest);
		String contenido = Files.readString(rutaTest);

		assertTrue(contenido.contains("Test"));
		assertTrue(contenido.contains("PROGRAMADOR"));
		assertTrue(contenido.contains("3"));
	}
}
