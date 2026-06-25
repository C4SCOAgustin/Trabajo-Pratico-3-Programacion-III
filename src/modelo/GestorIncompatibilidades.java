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
        observers.add(observer);
    }

    public void agregar(Incompatibilidad i) {
        incompatibilidades.add(i);
        notificarIncompatibilidadesCambiadas();
    }

    public List<Incompatibilidad> getIncompatibilidades() {
        return new ArrayList<>(incompatibilidades);
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
}