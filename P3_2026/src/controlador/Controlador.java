package controlador;

import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

import modelo.EstadisticasSolver;
import modelo.ModeloEquipo;
import modelo.ResultadoResolucion;
import modelo.Rol;

/**
 * Controlador (capa Controlador del MVC).
 * Conecta la vista con la lógica de negocio. No contiene lógica de negocio propia.
 */
public class Controlador {

    private final ModeloEquipo modelo;

    public Controlador() {
        this(new ModeloEquipo());
    }

    public Controlador(ModeloEquipo modelo) {
        if (modelo == null) {
            throw new IllegalArgumentException("El modelo no puede ser nulo");
        }
        this.modelo = modelo;
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

    public void agregarIncompatibilidad(String nombreA, String nombreB) {
        modelo.agregarIncompatibilidad(nombreA, nombreB);
    }

    public void eliminarIncompatibilidad(String nombreA, String nombreB) {
        modelo.eliminarIncompatibilidad(nombreA, nombreB);
    }

    public void setRequerimiento(Rol rol, int cantidad) {
        modelo.setRequerimiento(rol, cantidad);
    }

    public void limpiarDatos() {
        modelo.limpiarDatos();
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
