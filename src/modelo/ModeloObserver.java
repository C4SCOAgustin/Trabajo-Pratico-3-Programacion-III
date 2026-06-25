package modelo;

import java.util.List;

/**
 * Interfaz Observer. La vista la implementa para recibir notificaciones
 * automáticas cuando el modelo cambia.
 */
public interface ModeloObserver {
    void onPersonasCambiadas(List<Persona> personas);
    void onIncompatibilidadesCambiadas(List<Incompatibilidad> incompatibilidades);
}