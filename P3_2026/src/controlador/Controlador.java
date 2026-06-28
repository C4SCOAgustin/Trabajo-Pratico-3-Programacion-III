package controlador;

import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

import modelo.EstadisticasSolver;
import modelo.Incompatibilidad;
import modelo.ModeloEquipo;
import modelo.ModeloObserver;
import modelo.Persona;
import modelo.Requerimientos;
import modelo.ResultadoResolucion;
import modelo.Rol;

/**
 * Controlador (capa Controlador del MVC).
 * Conecta la vista con la lógica de negocio. No contiene lógica de negocio propia.
 */
public class Controlador {

    private final ModeloEquipo modelo = new ModeloEquipo();

    public void agregarObserver(ModeloObserver observer) {
        modelo.agregarObserver(observer);
    }

    public void agregarPersona(String nombre, Rol rol, int calificacion) {
        modelo.agregarPersona(nombre, rol, calificacion);
    }

    public void eliminarPersona(String nombre) {
        modelo.eliminarPersona(nombre);
    }

    public void eliminarTodasLasPersonas() {
        modelo.eliminarTodasLasPersonas();
    }

    public List<Persona> getPersonas() {
        return modelo.getPersonas();
    }

    public void agregarIncompatibilidad(String nombreA, String nombreB) {
        modelo.agregarIncompatibilidad(nombreA, nombreB);
    }

    public void eliminarIncompatibilidad(String nombreA, String nombreB) {
        modelo.eliminarIncompatibilidad(nombreA, nombreB);
    }

    public List<Incompatibilidad> getIncompatibilidades() {
        return modelo.getIncompatibilidades();
    }

    public void setRequerimiento(Rol rol, int cantidad) {
        modelo.setRequerimiento(rol, cantidad);
    }

    public Requerimientos getRequerimientos() {
        return modelo.copiarRequerimientos();
    }

    public void limpiarDatos() {
        modelo.limpiarDatos();
    }

    public Requerimientos copiarRequerimientos() {
        return modelo.copiarRequerimientos();
    }

    /**
     * Resuelve el problema en un hilo separado.
     * @param onEstadisticas recibe estadísticas parciales durante la ejecución.
     * @param alTerminar     callback que recibe el resultado (se ejecuta en el EDT).
     */
    public void resolverAsync(final Consumer<EstadisticasSolver> onEstadisticas,
            final Consumer<ResultadoResolucion> alTerminar) {
        SwingWorker<ResultadoResolucion, EstadisticasSolver> worker =
                new SwingWorker<ResultadoResolucion, EstadisticasSolver>() {
            @Override
            protected ResultadoResolucion doInBackground() {
                return modelo.resolver(estadisticas -> publish(estadisticas));
            }

            @Override
            protected void process(List<EstadisticasSolver> estadisticas) {
                if (onEstadisticas != null && !estadisticas.isEmpty()) {
                    onEstadisticas.accept(estadisticas.get(estadisticas.size() - 1));
                }
            }

            @Override
            protected void done() {
                try {
                    alTerminar.accept(get());
                } catch (Exception e) {
                    String msg = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                    alTerminar.accept(ResultadoResolucion.error(msg));
                }
            }
        };
        worker.execute();
    }

    /** Resolución sincrónica, útil para tests. */
    public ResultadoResolucion resolver() {
        return modelo.resolver();
    }
}
