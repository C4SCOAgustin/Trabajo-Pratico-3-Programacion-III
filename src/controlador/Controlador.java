package controlador;

import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

import modelo.Equipo;
import modelo.EstadisticasSolver;
import modelo.GestorIncompatibilidades;
import modelo.GestorPersonas;
import modelo.Incompatibilidad;
import modelo.ModeloObserver;
import modelo.Persona;
import modelo.Requerimientos;
import modelo.ResultadoResolucion;
import modelo.Rol;
import modelo.Solver;

/**
 * Controlador (capa Controlador del MVC).
 * Conecta la vista con la lógica de negocio. No contiene lógica de negocio propia.
 */
public class Controlador {

    private final GestorPersonas gestorPersonas = new GestorPersonas();
    private final GestorIncompatibilidades gestorIncompat = new GestorIncompatibilidades();
    private final Requerimientos requerimientos = new Requerimientos();

    public void agregarObserver(ModeloObserver observer) {
        gestorPersonas.agregarObserver(observer);
        gestorIncompat.agregarObserver(observer);
    }

    public void agregarPersona(String nombre, Rol rol, int calificacion) {
        gestorPersonas.agregar(nombre, rol, calificacion);
    }

    public void eliminarPersona(String nombre) {
        Persona persona = gestorPersonas.buscar(nombre);
        gestorPersonas.eliminar(nombre);
        gestorIncompat.eliminarInvolucrando(persona);
    }

    public void eliminarTodasLasPersonas() {
        gestorPersonas.eliminarTodas();
        gestorIncompat.limpiar();
    }

    public List<Persona> getPersonas() {
        return gestorPersonas.getPersonas();
    }

    public void agregarIncompatibilidad(String nombreA, String nombreB) {
        Persona a = gestorPersonas.buscar(nombreA);
        Persona b = gestorPersonas.buscar(nombreB);
        if (a == null || b == null) {
            throw new IllegalArgumentException("Ambas personas deben existir");
        }
        if (gestorIncompat.sonIncompatibles(a, b)) {
            throw new IllegalArgumentException("La incompatibilidad ya existe");
        }
        gestorIncompat.agregar(new Incompatibilidad(a, b));
    }

    public void eliminarIncompatibilidad(String nombreA, String nombreB) {
        Persona a = gestorPersonas.buscar(nombreA);
        Persona b = gestorPersonas.buscar(nombreB);
        if (a == null || b == null) {
            throw new IllegalArgumentException("Ambas personas deben existir");
        }
        if (a.equals(b)) {
            throw new IllegalArgumentException("No se puede eliminar una incompatibilidad entre la misma persona");
        }
        if (!gestorIncompat.eliminar(a, b)) {
            throw new IllegalArgumentException("No existe esa incompatibilidad");
        }
    }

    public List<Incompatibilidad> getIncompatibilidades() {
        return gestorIncompat.getIncompatibilidades();
    }

    public void setRequerimiento(Rol rol, int cantidad) {
        requerimientos.setCantidad(rol, cantidad);
    }

    public Requerimientos getRequerimientos() {
        return requerimientos;
    }

    public void limpiarDatos() {
        gestorPersonas.eliminarTodas();
        gestorIncompat.limpiar();
        for (Rol r : Rol.values()) {
            requerimientos.setCantidad(r, 0);
        }
    }

    public Requerimientos copiarRequerimientos() {
        Requerimientos copia = new Requerimientos();
        copia.copiarDesde(requerimientos);
        return copia;
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
                try {
                    final Solver[] solverHolder = new Solver[1];
                    solverHolder[0] = new Solver(gestorPersonas.getPersonas(), gestorIncompat, requerimientos,
                            porcentaje -> publish(solverHolder[0].getUltimasEstadisticas()));
                    Equipo equipo = solverHolder[0].resolver();
                    return ResultadoResolucion.exito(equipo, solverHolder[0].getUltimasEstadisticas());
                } catch (RuntimeException e) {
                    return ResultadoResolucion.error(e.getMessage());
                }
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
        try {
            Solver solver = new Solver(gestorPersonas.getPersonas(), gestorIncompat, requerimientos);
            Equipo equipo = solver.resolver();
            return ResultadoResolucion.exito(equipo, solver.getUltimasEstadisticas());
        } catch (RuntimeException e) {
            return ResultadoResolucion.error(e.getMessage());
        }
    }
}