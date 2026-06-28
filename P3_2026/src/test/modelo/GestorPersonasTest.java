package test.modelo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import modelo.GestorPersonas;
import modelo.Incompatibilidad;
import modelo.ModeloObserver;
import modelo.Persona;
import modelo.Rol;

public class GestorPersonasTest {

    private GestorPersonas gestor;

    @Before
    public void setUp() {
        gestor = new GestorPersonas(false);
    }

    @Test
    public void testAgregarPersona() {
        gestor.agregar("Juan", Rol.PROGRAMADOR, 5);

        List<Persona> personas = gestor.getPersonas();
        assertEquals(1, personas.size());
        assertEquals("Juan", personas.get(0).getNombre());
        assertEquals(Rol.PROGRAMADOR, personas.get(0).getRol());
        assertEquals(5, personas.get(0).getCalificacion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoPermiteNombresDuplicados() {
        gestor.agregar("Juan", Rol.PROGRAMADOR, 5);
        gestor.agregar("Juan", Rol.TESTER, 3);
    }

    @Test
    public void testBuscarObligatoria() {
        gestor.agregar("Ana", Rol.ARQUITECTO, 4);

        Persona persona = gestor.buscarObligatoria("Ana");

        assertEquals("Ana", persona.getNombre());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuscarObligatoriaInexistente() {
        gestor.buscarObligatoria("No existe");
    }

    @Test
    public void testEliminarPersona() {
        gestor.agregar("Juan", Rol.PROGRAMADOR, 5);
        gestor.eliminar("Juan");

        assertTrue(gestor.getPersonas().isEmpty());
    }

    @Test
    public void testGetPersonasRetornaCopia() {
        gestor.agregar("Juan", Rol.PROGRAMADOR, 5);
        List<Persona> personas = gestor.getPersonas();

        personas.add(new Persona("Ana", Rol.TESTER, 3));

        assertEquals(1, gestor.getPersonas().size());
    }

    @Test
    public void testNotificaObserverAlCambiarPersonas() {
        ObserverPrueba observer = new ObserverPrueba();
        gestor.agregarObserver(observer);

        gestor.agregar("Juan", Rol.PROGRAMADOR, 5);
        gestor.eliminar("Juan");

        assertEquals(2, observer.cambiosPersonas);
        assertTrue(observer.ultimasPersonas.isEmpty());
    }

    @Test
    public void testAgregarVariasPersonasConservaOrden() {
        gestor.agregar("Juan", Rol.PROGRAMADOR, 5);
        gestor.agregar("Ana", Rol.TESTER, 4);
        gestor.agregar("Luis", Rol.ARQUITECTO, 3);

        List<Persona> personas = gestor.getPersonas();
        assertEquals("Juan", personas.get(0).getNombre());
        assertEquals("Ana", personas.get(1).getNombre());
        assertEquals("Luis", personas.get(2).getNombre());
    }

    @Test
    public void testBuscarRetornaNullSiNoExiste() {
        assertNull(gestor.buscar("No existe"));
    }

    @Test
    public void testBuscarEsSensibleAMayusculas() {
        gestor.agregar("Juan", Rol.PROGRAMADOR, 5);

        assertNull(gestor.buscar("juan"));
        assertNotNull(gestor.buscar("Juan"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEliminarPersonaInexistente() {
        gestor.eliminar("No existe");
    }

    @Test
    public void testEliminarTodasBorraPersonas() {
        gestor.agregar("Juan", Rol.PROGRAMADOR, 5);
        gestor.agregar("Ana", Rol.TESTER, 4);

        gestor.eliminarTodas();

        assertTrue(gestor.getPersonas().isEmpty());
    }

    @Test
    public void testEliminarTodasNotificaObserver() {
        ObserverPrueba observer = new ObserverPrueba();
        gestor.agregarObserver(observer);
        gestor.agregar("Juan", Rol.PROGRAMADOR, 5);

        gestor.eliminarTodas();

        assertEquals(2, observer.cambiosPersonas);
        assertTrue(observer.ultimasPersonas.isEmpty());
    }

    @Test
    public void testObserverRecibeCopiaDefensiva() {
        ObserverQueModificaLista observer = new ObserverQueModificaLista();
        gestor.agregarObserver(observer);

        gestor.agregar("Juan", Rol.PROGRAMADOR, 5);

        assertEquals(1, gestor.getPersonas().size());
        assertTrue(observer.intentoModificarLista);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgregarObserverNull() {
        gestor.agregarObserver(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPersonaConNombreInvalido() {
        gestor.agregar("   ", Rol.PROGRAMADOR, 5);
    }

    private static class ObserverPrueba implements ModeloObserver {
        private int cambiosPersonas;
        private List<Persona> ultimasPersonas = new ArrayList<>();

        public void onPersonasCambiadas(List<Persona> personas) {
            cambiosPersonas++;
            ultimasPersonas = new ArrayList<>(personas);
        }

        public void onIncompatibilidadesCambiadas(List<Incompatibilidad> incompatibilidades) {
        }
    }

    private static class ObserverQueModificaLista implements ModeloObserver {
        private boolean intentoModificarLista;

        public void onPersonasCambiadas(List<Persona> personas) {
            intentoModificarLista = true;
            personas.clear();
        }

        public void onIncompatibilidadesCambiadas(List<Incompatibilidad> incompatibilidades) {
        }
    }
}
