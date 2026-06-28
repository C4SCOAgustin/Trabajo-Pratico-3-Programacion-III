package test.modelo;

import static org.junit.Assert.*;

import org.junit.Test;

import modelo.Rol;

public class RolTest {

	@Test
	public void testRolesExisten() {
		assertNotNull(Rol.LIDER_DE_PROYECTO);
		assertNotNull(Rol.ARQUITECTO);
		assertNotNull(Rol.PROGRAMADOR);
		assertNotNull(Rol.TESTER);
	}

	@Test
	public void testNombresRoles() {
		assertEquals("Líder de proyecto", Rol.LIDER_DE_PROYECTO.getNombre());
		assertEquals("Arquitecto", Rol.ARQUITECTO.getNombre());
		assertEquals("Programador", Rol.PROGRAMADOR.getNombre());
		assertEquals("Tester", Rol.TESTER.getNombre());
	}

	@Test
	public void testToString() {
		assertEquals("Líder de proyecto", Rol.LIDER_DE_PROYECTO.toString());
		assertEquals("Arquitecto", Rol.ARQUITECTO.toString());
		assertEquals("Programador", Rol.PROGRAMADOR.toString());
		assertEquals("Tester", Rol.TESTER.toString());
	}

	@Test
	public void testCantidadRoles() {
		Rol[] roles = Rol.values();
		assertEquals(4, roles.length);
	}

	@Test
	public void testRolValueOf() {
		assertEquals(Rol.PROGRAMADOR, Rol.valueOf("PROGRAMADOR"));
		assertEquals(Rol.ARQUITECTO, Rol.valueOf("ARQUITECTO"));
		assertEquals(Rol.TESTER, Rol.valueOf("TESTER"));
		assertEquals(Rol.LIDER_DE_PROYECTO, Rol.valueOf("LIDER_DE_PROYECTO"));
	}

	@Test
	public void testToStringCoincideConGetNombre() {
		for (Rol rol : Rol.values()) {
			assertEquals(rol.getNombre(), rol.toString());
		}
	}

	@Test
	public void testTodosLosRolesTienenNombreNoVacio() {
		for (Rol rol : Rol.values()) {
			assertNotNull(rol.getNombre());
			assertFalse(rol.getNombre().trim().isEmpty());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRolValueOfInvalido() {
		Rol.valueOf("NO_EXISTE");
	}

	@Test
	public void testOrdenDeRolesEsperado() {
		Rol[] roles = Rol.values();

		assertEquals(Rol.LIDER_DE_PROYECTO, roles[0]);
		assertEquals(Rol.ARQUITECTO, roles[1]);
		assertEquals(Rol.PROGRAMADOR, roles[2]);
		assertEquals(Rol.TESTER, roles[3]);
	}
}
