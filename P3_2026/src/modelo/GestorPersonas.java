package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la lista de personas disponibles para formar el equipo.
 * Encapsula la lógica de negocio: agregar, eliminar, buscar y persistir personas.
 * Notifica a los observers registrados ante cualquier cambio.
 */
public class GestorPersonas {

    private final List<Persona> personas = new ArrayList<>();
    private final List<ModeloObserver> observers = new ArrayList<>();
    private final boolean persistenciaActiva;

    public GestorPersonas() {
        this(true, true);
    }

    public GestorPersonas(boolean cargarPersistidas) {
        this(cargarPersistidas, cargarPersistidas);
    }

    public GestorPersonas(boolean cargarPersistidas, boolean persistenciaActiva) {
        this.persistenciaActiva = persistenciaActiva;
        if (cargarPersistidas) {
            try {
                List<Persona> cargadas = AlmacenPersonas.cargar();
                if (cargadas != null && !cargadas.isEmpty()) {
                    personas.addAll(cargadas);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void agregarObserver(ModeloObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("El observer no puede ser nulo");
        }
        observers.add(observer);
    }

    public void agregar(String nombre, Rol rol, int calificacion) {
        Persona p = new Persona(nombre, rol, calificacion);
        if (personas.contains(p)) {
            throw new IllegalArgumentException("Ya existe una persona con ese nombre");
        }
        personas.add(p);
        persistir();
        notificarPersonasCambiadas();
    }

    public void eliminar(String nombre) {
        Persona persona = buscar(nombre);
        if (persona == null) {
            throw new IllegalArgumentException("No existe una persona con ese nombre");
        }
        personas.remove(persona);
        persistir();
        notificarPersonasCambiadas();
    }

    public void eliminarTodas() {
        personas.clear();
        persistir();
        notificarPersonasCambiadas();
    }

    public Persona buscar(String nombre) {
        for (Persona p : personas) {
            if (p.getNombre().equals(nombre)) {
                return p;
            }
        }
        return null;
    }

    public Persona buscarObligatoria(String nombre) {
        Persona persona = buscar(nombre);
        if (persona == null) {
            throw new IllegalArgumentException("No existe una persona con ese nombre");
        }
        return persona;
    }

    public List<Persona> getPersonas() {
        return new ArrayList<>(personas);
    }

    private void persistir() {
        if (!persistenciaActiva) {
            return;
        }
        try {
            AlmacenPersonas.guardar(personas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notificarPersonasCambiadas() {
        List<Persona> copia = getPersonas();
        for (ModeloObserver observer : observers) {
            observer.onPersonasCambiadas(copia);
        }
    }
}
