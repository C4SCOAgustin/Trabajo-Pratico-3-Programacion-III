package test;

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
}
