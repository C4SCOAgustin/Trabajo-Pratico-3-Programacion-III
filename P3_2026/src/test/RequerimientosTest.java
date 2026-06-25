package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import modelo.Requerimientos;
import modelo.Rol;

public class RequerimientosTest {

	private Requerimientos req;

	@Before
	public void setUp() {
		req = new Requerimientos();
	}

	@Test
	public void testRequerimientosVacio() {
		assertEquals(0, req.getCantidad(Rol.PROGRAMADOR));
		assertEquals(0, req.getCantidad(Rol.ARQUITECTO));
		assertEquals(0, req.getCantidad(Rol.TESTER));
		assertEquals(0, req.getCantidad(Rol.LIDER_DE_PROYECTO));
	}

	@Test
	public void testSetCantidad() {
		req.setCantidad(Rol.PROGRAMADOR, 2);
		assertEquals(2, req.getCantidad(Rol.PROGRAMADOR));
	}

	@Test
	public void testSetCantidadVariosRoles() {
		req.setCantidad(Rol.PROGRAMADOR, 3);
		req.setCantidad(Rol.ARQUITECTO, 1);
		req.setCantidad(Rol.TESTER, 2);
		req.setCantidad(Rol.LIDER_DE_PROYECTO, 1);

		assertEquals(3, req.getCantidad(Rol.PROGRAMADOR));
		assertEquals(1, req.getCantidad(Rol.ARQUITECTO));
		assertEquals(2, req.getCantidad(Rol.TESTER));
		assertEquals(1, req.getCantidad(Rol.LIDER_DE_PROYECTO));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetCantidadNegativa() {
		req.setCantidad(Rol.PROGRAMADOR, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetCantidadRolNull() {
		req.setCantidad(null, 2);
	}

	@Test
	public void testSetCantidadCero() {
		req.setCantidad(Rol.PROGRAMADOR, 0);
		assertEquals(0, req.getCantidad(Rol.PROGRAMADOR));
	}

	@Test
	public void testTotal() {
		assertEquals(0, req.total());
		req.setCantidad(Rol.PROGRAMADOR, 2);
		assertEquals(2, req.total());
		req.setCantidad(Rol.ARQUITECTO, 1);
		assertEquals(3, req.total());
		req.setCantidad(Rol.TESTER, 3);
		assertEquals(6, req.total());
	}

	@Test
	public void testCopiarDesde() {
		Requerimientos req2 = new Requerimientos();
		req.setCantidad(Rol.PROGRAMADOR, 5);
		req.setCantidad(Rol.ARQUITECTO, 2);

		req2.copiarDesde(req);
		assertEquals(5, req2.getCantidad(Rol.PROGRAMADOR));
		assertEquals(2, req2.getCantidad(Rol.ARQUITECTO));
		assertEquals(0, req2.getCantidad(Rol.TESTER));
	}

	@Test
	public void testCopiarDesdeNoAfectaOrigen() {
		Requerimientos req2 = new Requerimientos();
		req.setCantidad(Rol.PROGRAMADOR, 5);
		req2.copiarDesde(req);

		req2.setCantidad(Rol.PROGRAMADOR, 10);
		assertEquals(5, req.getCantidad(Rol.PROGRAMADOR));
	}

	@Test
	public void testToString() {
		req.setCantidad(Rol.PROGRAMADOR, 2);
		req.setCantidad(Rol.ARQUITECTO, 1);
		String str = req.toString();
		assertNotNull(str);
		assertTrue(str.length() > 0);
	}
}
