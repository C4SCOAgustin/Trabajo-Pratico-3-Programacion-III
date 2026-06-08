package modelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilidad para guardar/leer listas de Persona en formato JSON simple.
 * Implementación ligera sin dependencias externas.
 */
public final class AlmacenPersonas {

    private AlmacenPersonas() { }

    public static final Path RUTA_POR_DEFECTO = Paths.get("personas.json");

    public static void guardar(List<Persona> personas) throws IOException {
        guardar(personas, RUTA_POR_DEFECTO);
    }

    public static void guardar(List<Persona> personas, Path path) throws IOException {
        if (personas == null) throw new IllegalArgumentException("personas no puede ser null");
        Files.createDirectories(path.getParent() == null ? Paths.get(".") : path.getParent());
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Persona p : personas) {
            if (!first) sb.append(',');
            first = false;
            sb.append('{');
            sb.append("\"nombre\":\"").append(escaparJson(p.getNombre())).append("\"");
            sb.append(',');
            sb.append("\"rol\":\"").append(escaparJson(p.getRol().name())).append("\"");
            sb.append(',');
            sb.append("\"calificacion\":").append(p.getCalificacion());
            sb.append('}');
        }
        sb.append("]");
        Files.writeString(path, sb.toString());
    }

    public static List<Persona> cargar() throws IOException {
        return cargar(RUTA_POR_DEFECTO);
    }

    public static List<Persona> cargar(Path path) throws IOException {
        if (!Files.exists(path)) return new ArrayList<>();
        String content = Files.readString(path);
        List<Persona> lista = new ArrayList<>();

        // Regex para objetos: {"nombre":"...","rol":"...","calificacion":N}
        Pattern objPattern = Pattern.compile("\\{\\s*\"nombre\"\\s*:\\s*\"(.*?)\"\\s*,\\s*\"rol\"\\s*:\\s*\"(.*?)\"\\s*,\\s*\"calificacion\"\\s*:\\s*(\\d+)\\s*\\}", Pattern.DOTALL);
        Matcher m = objPattern.matcher(content);
        while (m.find()) {
            String nombre = desescaparJson(m.group(1));
            String rolName = desescaparJson(m.group(2));
            int calif = Integer.parseInt(m.group(3));
            try {
                Persona p = new Persona(nombre, Rol.valueOf(rolName), calif);
                lista.add(p);
            } catch (IllegalArgumentException ex) {
                // saltar entradas inválidas
            }
        }
        return lista;
    }

    private static String escaparJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String desescaparJson(String s) {
        if (s == null) return "";
        return s.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
