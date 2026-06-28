package test.modelo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import modelo.EstadisticasSolver;
import modelo.GestorIncompatibilidades;
import modelo.GestorPersonas;
import modelo.Incompatibilidad;
import modelo.ModeloEquipo;
import modelo.ModeloObserver;
import modelo.Persona;
import modelo.Requerimientos;
import modelo.ResultadoResolucion;
import modelo.Rol;

public class ModeloEquipoTest {

    private ModeloEquipo modelo;

    @Before
    public void setUp() {
        modelo = new ModeloEquipo(false);
    }

    @Test
    public void testAgregarIncompatibilidadPorNombre() {
        cargarPersonasBasicas();

        modelo.agregarIncompatibilidad("Juan", "Maria");

        assertEquals(1, modelo.getIncompatibilidades().size());
        assertTrue(modelo.getIncompatibilidades().get(0)
                .involucra(modelo.getPersonas().get(0), modelo.getPersonas().get(1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoPermiteIncompatibilidadDuplicada() {
        cargarPersonasBasicas();

        modelo.agregarIncompatibilidad("Juan", "Maria");
        modelo.agregarIncompatibilidad("Maria", "Juan");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoPermiteIncompatibilidadConPersonaInexistente() {
        modelo.agregarPersona("Juan", Rol.PROGRAMADOR, 5);

        modelo.agregarIncompatibilidad("Juan", "No existe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoPermiteIncompatibilidadConLaMismaPersona() {
        modelo.agregarPersona("Juan", Rol.PROGRAMADOR, 5);

        modelo.agregarIncompatibilidad("Juan", "Juan");
    }

    @Test
    public void testEliminarPersonaEliminaSusIncompatibilidades() {
        cargarPersonasBasicas();
        modelo.agregarPersona("Carlos", Rol.TESTER, 3);
        modelo.agregarIncompatibilidad("Juan", "Maria");
        modelo.agregarIncompatibilidad("Maria", "Carlos");

        modelo.eliminarPersona("Juan");

        assertEquals(2, modelo.getPersonas().size());
        assertEquals(1, modelo.getIncompatibilidades().size());
        assertEquals("Maria <-> Carlos", modelo.getIncompatibilidades().get(0).toString());
    }

    @Test
    public void testObserverRecibeCambiosDelModelo() {
        ObserverPrueba observer = new ObserverPrueba();
        modelo.agregarObserver(observer);
        cargarPersonasBasicas();

        modelo.agregarIncompatibilidad("Juan", "Maria");
        modelo.eliminarPersona("Juan");

        assertEquals(3, observer.cambiosPersonas);
        assertEquals(2, observer.cambiosIncompatibilidades);
        assertEquals(1, observer.ultimasPersonas.size());
        assertTrue(observer.ultimasIncompatibilidades.isEmpty());
    }

    @Test
    public void testResolverDelegadoAlModelo() {
        cargarPersonasBasicas();
        modelo.setRequerimiento(Rol.PROGRAMADOR, 1);

        ResultadoResolucion resultado = modelo.resolver();

        assertTrue(resultado.esExito());
        assertEquals(1, resultado.getEquipo().cantidadEnRol(Rol.PROGRAMADOR));
        assertNotNull(resultado.getEstadisticas());
    }

    @Test
    public void testGetPersonasRetornaCopia() {
        cargarPersonasBasicas();
        List<Persona> personas = modelo.getPersonas();

        personas.clear();

        assertEquals(2, modelo.getPersonas().size());
    }

    @Test
    public void testGetIncompatibilidadesRetornaCopia() {
        cargarPersonasBasicas();
        modelo.agregarPersona("Carlos", Rol.TESTER, 3);
        modelo.agregarIncompatibilidad("Juan", "Maria");
        List<Incompatibilidad> incompatibilidades = modelo.getIncompatibilidades();

        incompatibilidades.add(new Incompatibilidad(modelo.getPersonas().get(0), modelo.getPersonas().get(2)));

        assertEquals(1, modelo.getIncompatibilidades().size());
    }

    @Test
    public void testEliminarIncompatibilidadExitosa() {
        cargarPersonasBasicas();
        modelo.agregarIncompatibilidad("Juan", "Maria");

        modelo.eliminarIncompatibilidad("Juan", "Maria");

        assertTrue(modelo.getIncompatibilidades().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEliminarIncompatibilidadInexistente() {
        cargarPersonasBasicas();

        modelo.eliminarIncompatibilidad("Juan", "Maria");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEliminarPersonaInexistente() {
        modelo.eliminarPersona("No existe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPersonaDuplicada() {
        modelo.agregarPersona("Juan", Rol.PROGRAMADOR, 5);
        modelo.agregarPersona("Juan", Rol.TESTER, 2);
    }

    @Test
    public void testEliminarTodasLasPersonasLimpiaIncompatibilidades() {
        cargarPersonasBasicas();
        modelo.agregarIncompatibilidad("Juan", "Maria");

        modelo.eliminarTodasLasPersonas();

        assertTrue(modelo.getPersonas().isEmpty());
        assertTrue(modelo.getIncompatibilidades().isEmpty());
    }

    @Test
    public void testCopiarRequerimientosEsIndependiente() {
        modelo.setRequerimiento(Rol.PROGRAMADOR, 2);
        Requerimientos copia = modelo.copiarRequerimientos();

        copia.setCantidad(Rol.PROGRAMADOR, 9);

        assertEquals(2, modelo.copiarRequerimientos().getCantidad(Rol.PROGRAMADOR));
    }

    @Test
    public void testLimpiarDatosReiniciaRequerimientos() {
        cargarPersonasBasicas();
        modelo.agregarIncompatibilidad("Juan", "Maria");
        modelo.setRequerimiento(Rol.PROGRAMADOR, 1);

        modelo.limpiarDatos();

        assertTrue(modelo.getPersonas().isEmpty());
        assertTrue(modelo.getIncompatibilidades().isEmpty());
        assertEquals(0, modelo.copiarRequerimientos().total());
    }

    @Test
    public void testResolverSinSolucionDevuelveEquipoVacio() {
        modelo.agregarPersona("Juan", Rol.PROGRAMADOR, 5);
        modelo.setRequerimiento(Rol.PROGRAMADOR, 2);

        ResultadoResolucion resultado = modelo.resolver();

        assertTrue(resultado.esExito());
        assertTrue(resultado.getEquipo().estaVacio());
    }

    @Test
    public void testResolverConCallbackDeEstadisticas() {
        cargarPersonasBasicas();
        modelo.setRequerimiento(Rol.PROGRAMADOR, 1);
        List<EstadisticasSolver> estadisticas = new ArrayList<>();

        ResultadoResolucion resultado = modelo.resolver(e -> estadisticas.add(e));

        assertTrue(resultado.esExito());
        assertFalse(estadisticas.isEmpty());
        assertNotNull(estadisticas.get(estadisticas.size() - 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRechazaGestorPersonasNull() {
        new ModeloEquipo(null, new GestorIncompatibilidades(), new Requerimientos());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRechazaGestorIncompatibilidadesNull() {
        new ModeloEquipo(new GestorPersonas(false), null, new Requerimientos());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRechazaRequerimientosNull() {
        new ModeloEquipo(new GestorPersonas(false), new GestorIncompatibilidades(), null);
    }

    private void cargarPersonasBasicas() {
        modelo.agregarPersona("Juan", Rol.PROGRAMADOR, 5);
        modelo.agregarPersona("Maria", Rol.ARQUITECTO, 4);
    }

    private static class ObserverPrueba implements ModeloObserver {
        private int cambiosPersonas;
        private int cambiosIncompatibilidades;
        private List<Persona> ultimasPersonas = new ArrayList<>();
        private List<Incompatibilidad> ultimasIncompatibilidades = new ArrayList<>();

        public void onPersonasCambiadas(List<Persona> personas) {
            cambiosPersonas++;
            ultimasPersonas = new ArrayList<>(personas);
        }

        public void onIncompatibilidadesCambiadas(List<Incompatibilidad> incompatibilidades) {
            cambiosIncompatibilidades++;
            ultimasIncompatibilidades = new ArrayList<>(incompatibilidades);
        }
    }
}
