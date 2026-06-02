package controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import modelo.EstadoAplicacion;

/** Guarda y recupera el estado de la aplicación mediante serialización Java. */
public final class Persistencia {

	private static final String NOMBRE_ARCHIVO = "equipo_ideal.dat";

	private Persistencia() {
	}

	public static Path rutaArchivo() {
		Path dir = Paths.get(System.getProperty("user.home"), ".equipo_ideal");
		return dir.resolve(NOMBRE_ARCHIVO);
	}

	public static void guardar(EstadoAplicacion estado) throws IOException {
		Path archivo = rutaArchivo();
		Files.createDirectories(archivo.getParent());
		try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(archivo))) {
			out.writeObject(estado);
		}
	}

	public static EstadoAplicacion cargar() throws IOException, ClassNotFoundException {
		Path archivo = rutaArchivo();
		if (!Files.isRegularFile(archivo)) {
			return null;
		}
		try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(archivo))) {
			return (EstadoAplicacion) in.readObject();
		}
	}
}
