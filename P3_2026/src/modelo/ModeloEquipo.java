package modelo;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Fachada de negocio de la aplicacion.
 * Centraliza las operaciones que combinan personas, incompatibilidades,
 * requerimientos y resolucion.
 */
public class ModeloEquipo {

    private final GestorPersonas gestorPersonas;
    private final GestorIncompatibilidades gestorIncompat;
    private final Requerimientos requerimientos;

    public ModeloEquipo() {
        this(true);
    }

    public ModeloEquipo(boolean cargarPersonasPersistidas) {
        this(new GestorPersonas(cargarPersonasPersistidas),
                new GestorIncompatibilidades(),
                new Requerimientos());
    }

    public ModeloEquipo(GestorPersonas gestorPersonas, GestorIncompatibilidades gestorIncompat,
            Requerimientos requerimientos) {
        if (gestorPersonas == null || gestorIncompat == null || requerimientos == null) {
            throw new IllegalArgumentException("Los componentes del modelo no pueden ser nulos");
        }
        this.gestorPersonas = gestorPersonas;
        this.gestorIncompat = gestorIncompat;
        this.requerimientos = requerimientos;
    }

    public void agregarObserver(ModeloObserver observer) {
        gestorPersonas.agregarObserver(observer);
        gestorIncompat.agregarObserver(observer);
    }

    public void agregarPersona(String nombre, Rol rol, int calificacion) {
        gestorPersonas.agregar(nombre, rol, calificacion);
    }

    public void eliminarPersona(String nombre) {
        Persona persona = gestorPersonas.buscarObligatoria(nombre);
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
        Persona a = gestorPersonas.buscarObligatoria(nombreA);
        Persona b = gestorPersonas.buscarObligatoria(nombreB);
        gestorIncompat.agregar(a, b);
    }

    public void eliminarIncompatibilidad(String nombreA, String nombreB) {
        Persona a = gestorPersonas.buscarObligatoria(nombreA);
        Persona b = gestorPersonas.buscarObligatoria(nombreB);
        gestorIncompat.eliminarObligatoria(a, b);
    }

    public List<Incompatibilidad> getIncompatibilidades() {
        return gestorIncompat.getIncompatibilidades();
    }

    public void setRequerimiento(Rol rol, int cantidad) {
        requerimientos.setCantidad(rol, cantidad);
    }

    public Requerimientos copiarRequerimientos() {
        Requerimientos copia = new Requerimientos();
        copia.copiarDesde(requerimientos);
        return copia;
    }

    public void limpiarDatos() {
        gestorPersonas.eliminarTodas();
        gestorIncompat.limpiar();
        for (Rol r : Rol.values()) {
            requerimientos.setCantidad(r, 0);
        }
    }

    public ResultadoResolucion resolver() {
        return resolver(null);
    }

    public ResultadoResolucion resolver(final Consumer<EstadisticasSolver> onEstadisticas) {
        try {
            final AtomicReference<Solver> solverRef = new AtomicReference<>();
            Solver solver = new Solver(gestorPersonas.getPersonas(), gestorIncompat.copiar(),
                    copiarRequerimientos(),
                    porcentaje -> publicarEstadisticas(onEstadisticas, solverRef.get()));
            solverRef.set(solver);
            Equipo equipo = solver.resolver();
            return ResultadoResolucion.exito(equipo, solver.getUltimasEstadisticas());
        } catch (RuntimeException e) {
            return ResultadoResolucion.error(e.getMessage());
        }
    }

    private void publicarEstadisticas(Consumer<EstadisticasSolver> onEstadisticas, Solver solver) {
        if (onEstadisticas != null && solver != null) {
            onEstadisticas.accept(solver.getUltimasEstadisticas());
        }
    }
}
