package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona las incompatibilidades entre personas.
 * Notifica a los observers registrados ante cualquier cambio.
 */
public class GestorIncompatibilidades {

    private final List<Incompatibilidad> incompatibilidades = new ArrayList<>();
    private final List<ModeloObserver> observers = new ArrayList<>();

    public void agregarObserver(ModeloObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("El observer no puede ser nulo");
        }
        observers.add(observer);
    }

    public void agregar(Incompatibilidad i) {
        if (i == null) {
            throw new IllegalArgumentException("La incompatibilidad no puede ser nula");
        }
        if (sonIncompatibles(i.getA(), i.getB())) {
            throw new IllegalArgumentException("La incompatibilidad ya existe");
        }
        incompatibilidades.add(i);
        notificarIncompatibilidadesCambiadas();
    }

    public void agregar(Persona p1, Persona p2) {
        validarPersonasDistintas(p1, p2);
        agregar(new Incompatibilidad(p1, p2));
    }

    public List<Incompatibilidad> getIncompatibilidades() {
        return new ArrayList<>(incompatibilidades);
    }

    public GestorIncompatibilidades copiar() {
        GestorIncompatibilidades copia = new GestorIncompatibilidades();
        copia.incompatibilidades.addAll(incompatibilidades);
        return copia;
    }

    public void limpiar() {
        incompatibilidades.clear();
        notificarIncompatibilidadesCambiadas();
    }

    public void eliminarInvolucrando(Persona persona) {
        incompatibilidades.removeIf(i -> i.involucra(persona));
        notificarIncompatibilidadesCambiadas();
    }

    public boolean eliminar(Persona p1, Persona p2) {
        boolean eliminado = incompatibilidades.removeIf(i -> i.involucra(p1, p2));
        if (eliminado) {
            notificarIncompatibilidadesCambiadas();
        }
        return eliminado;
    }

    public void eliminarObligatoria(Persona p1, Persona p2) {
        validarPersonasDistintas(p1, p2);
        if (!eliminar(p1, p2)) {
            throw new IllegalArgumentException("No existe esa incompatibilidad");
        }
    }

    public boolean sonIncompatibles(Persona p1, Persona p2) {
        for (Incompatibilidad i : incompatibilidades) {
            if (i.involucra(p1, p2)) {
                return true;
            }
        }
        return false;
    }

    public boolean esIncompatibleCon(Persona candidata, List<Persona> seleccionados) {
        for (Persona p : seleccionados) {
            if (sonIncompatibles(candidata, p)) {
                return true;
            }
        }
        return false;
    }

    private void notificarIncompatibilidadesCambiadas() {
        List<Incompatibilidad> copia = getIncompatibilidades();
        for (ModeloObserver observer : observers) {
            observer.onIncompatibilidadesCambiadas(copia);
        }
    }

    private void validarPersonasDistintas(Persona p1, Persona p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Ambas personas deben existir");
        }
        if (p1.equals(p2)) {
            throw new IllegalArgumentException("Una persona no puede ser incompatible consigo misma");
        }
    }
}
