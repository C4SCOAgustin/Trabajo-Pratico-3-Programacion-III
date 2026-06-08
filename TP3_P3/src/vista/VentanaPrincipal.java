package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import controlador.Controlador;
import modelo.EstadisticasSolver;
import modelo.Incompatibilidad;
import modelo.Persona;
import modelo.Requerimientos;
import modelo.ResultadoResolucion;
import modelo.Rol;

/**
 * Vista (capa Vista del MVT). Solo interfaz, sin lógica de negocio.
 * Delega todo al Controlador.
 */
public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int LIMITE_LISTADO_UI = 50;
	private static final int LIMITE_COMBO_UI = 100;
	private static final int INTERVALO_ANIMACION_MS = 20;
	private static final int PROGRESO_MAXIMO_BUSQUEDA = 70;
	private static final int DURACION_SUBIDA_BUSQUEDA_MS = 2000;
	private static final int DURACION_COMPLETAR_MS = 1500;

	private final Controlador controlador = new Controlador();

	private Timer timerProgreso;
	private long inicioResolucion;
	private ResultadoResolucion resultadoPendiente;
	private boolean resolucionTerminada;
	private boolean faseFinalizacion;
	private long inicioFaseFinal;
	private int progresoInicioFaseFinal;
	private int progresoVisual;

	// Pestaña Personas
	private JTextField txtNombre;
	private JTextField txtNombreBorrar;
	private JComboBox<Rol> cmbRol;
	private JSpinner spnCalificacion;
	private JTextArea areaPersonas;

	// Pestaña Incompatibilidades
	private JComboBox<String> cmbPersonaA;
	private JComboBox<String> cmbPersonaB;
	private JTextArea areaIncompat;

	// Pestaña Requerimientos
	private JSpinner spnLider;
	private JSpinner spnArquitecto;
	private JSpinner spnProgramador;
	private JSpinner spnTester;

	// Pestaña Resultado
	private JTextArea areaResultado;
	private JButton btnResolver;
	private JProgressBar barraProgreso;
	private JLabel lblProgreso;

	public VentanaPrincipal() {
		TemaOscuro.aplicar();

		setTitle("Equipo Ideal");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 650, 520);
		getContentPane().setBackground(TemaOscuro.FONDO);

		JTabbedPane tabs = new JTabbedPane();
		tabs.setBackground(TemaOscuro.FONDO);
		tabs.addTab("Personas", crearPanelPersonas());
		tabs.addTab("Incompatibilidades", crearPanelIncompatibilidades());
		tabs.addTab("Requerimientos", crearPanelRequerimientos());
		tabs.addTab("Resultado", crearPanelResultado());
		getContentPane().add(tabs);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrarAplicacion();
			}
		});

		cargarDatosGuardados();
	}

	private JPanel crearPanelPersonas() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(TemaOscuro.bordePanel());
		panel.setBackground(TemaOscuro.FONDO_PANEL);

		JPanel form = new JPanel(new GridLayout(6, 1, 5, 10));
		form.setBackground(TemaOscuro.FONDO_PANEL);

		JPanel filaNombre = new JPanel(new BorderLayout(5, 5));
		filaNombre.setBackground(TemaOscuro.FONDO_PANEL);
		filaNombre.add(etiqueta("Nombre:"), BorderLayout.WEST);
		txtNombre = new JTextField();
		filaNombre.add(txtNombre, BorderLayout.CENTER);
		form.add(filaNombre);

		JPanel filaRol = new JPanel(new BorderLayout(5, 5));
		filaRol.setBackground(TemaOscuro.FONDO_PANEL);
		filaRol.add(etiqueta("Rol:"), BorderLayout.WEST);
		cmbRol = new JComboBox<>(Rol.values());
		filaRol.add(cmbRol, BorderLayout.CENTER);
		form.add(filaRol);

		JPanel filaCalificacion = new JPanel(new BorderLayout(5, 5));
		filaCalificacion.setBackground(TemaOscuro.FONDO_PANEL);
		filaCalificacion.add(etiqueta("Calificación (1-5):"), BorderLayout.WEST);
		spnCalificacion = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
		filaCalificacion.add(spnCalificacion, BorderLayout.CENTER);
		configurarSpinnerSoloFlechas(spnCalificacion);
		form.add(filaCalificacion);

		JButton btnAgregar = new JButton("Agregar persona");
		JPanel filaAgregar = new JPanel(new FlowLayout(FlowLayout.CENTER));
		filaAgregar.setBackground(TemaOscuro.FONDO_PANEL);
		filaAgregar.add(btnAgregar);
		form.add(filaAgregar);

		JPanel filaBorrarNombre = new JPanel(new BorderLayout(5, 5));
		filaBorrarNombre.setBackground(TemaOscuro.FONDO_PANEL);
		filaBorrarNombre.add(etiqueta("Nombre a borrar:"), BorderLayout.WEST);
		txtNombreBorrar = new JTextField();
		filaBorrarNombre.add(txtNombreBorrar, BorderLayout.CENTER);
		JButton btnBorrarPersona = new JButton("Borrar persona");
		filaBorrarNombre.add(btnBorrarPersona, BorderLayout.EAST);
		form.add(filaBorrarNombre);

		JButton btnBorrarTodas = new JButton("Borrar todas las personas");
		JPanel filaBorrarTodas = new JPanel(new FlowLayout(FlowLayout.CENTER));
		filaBorrarTodas.setBackground(TemaOscuro.FONDO_PANEL);
		filaBorrarTodas.add(btnBorrarTodas);
		form.add(filaBorrarTodas);

		panel.add(form, BorderLayout.NORTH);

		areaPersonas = new JTextArea();
		areaPersonas.setEditable(false);
		TemaOscuro.estilizarAreaTexto(areaPersonas);
		panel.add(new JScrollPane(areaPersonas), BorderLayout.CENTER);

		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregarPersona();
			}
		});

		btnBorrarPersona.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				borrarPersona();
			}
		});

		btnBorrarTodas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				borrarTodasLasPersonas();
			}
		});

		TemaOscuro.estilizarContenedor(form);
		return panel;
	}

	private JPanel crearPanelIncompatibilidades() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(TemaOscuro.bordePanel());
		panel.setBackground(TemaOscuro.FONDO_PANEL);

		JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
		form.setBackground(TemaOscuro.FONDO_PANEL);
		form.add(etiqueta("Persona A:"));
		cmbPersonaA = new JComboBox<>();
		form.add(cmbPersonaA);

		form.add(etiqueta("Persona B:"));
		cmbPersonaB = new JComboBox<>();
		form.add(cmbPersonaB);

		JButton btnAgregar = new JButton("Agregar incompatibilidad");
		JButton btnEliminar = new JButton("Eliminar incompatibilidad");
		form.add(btnAgregar);
		form.add(btnEliminar);

		panel.add(form, BorderLayout.NORTH);

		areaIncompat = new JTextArea();
		areaIncompat.setEditable(false);
		TemaOscuro.estilizarAreaTexto(areaIncompat);
		panel.add(new JScrollPane(areaIncompat), BorderLayout.CENTER);

		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregarIncompatibilidad();
			}
		});

		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminarIncompatibilidad();
			}
		});

		TemaOscuro.estilizarContenedor(form);
		return panel;
	}

	private JPanel crearPanelRequerimientos() {
		JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
		panel.setBorder(TemaOscuro.bordePanel());
		panel.setBackground(TemaOscuro.FONDO_PANEL);

		panel.add(etiqueta("Líderes de proyecto:"));
		spnLider = new JSpinner(new SpinnerNumberModel(1, 0, 50, 1));
		panel.add(spnLider);
		configurarSpinnerSoloFlechas(spnLider);

		panel.add(etiqueta("Arquitectos:"));
		spnArquitecto = new JSpinner(new SpinnerNumberModel(1, 0, 50, 1));
		panel.add(spnArquitecto);
		configurarSpinnerSoloFlechas(spnArquitecto);

		panel.add(etiqueta("Programadores:"));
		spnProgramador = new JSpinner(new SpinnerNumberModel(2, 0, 50, 1));
		panel.add(spnProgramador);
		configurarSpinnerSoloFlechas(spnProgramador);

		panel.add(etiqueta("Testers:"));
		spnTester = new JSpinner(new SpinnerNumberModel(1, 0, 50, 1));
		panel.add(spnTester);
		configurarSpinnerSoloFlechas(spnTester);

		ChangeListener guardarRequerimientos = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				persistirRequerimientos();
			}
		};
		spnLider.addChangeListener(guardarRequerimientos);
		spnArquitecto.addChangeListener(guardarRequerimientos);
		spnProgramador.addChangeListener(guardarRequerimientos);
		spnTester.addChangeListener(guardarRequerimientos);

		TemaOscuro.estilizarContenedor(panel);
		return panel;
	}

	private JLabel etiqueta(String texto) {
		JLabel label = new JLabel(texto);
		TemaOscuro.estilizarEtiqueta(label);
		return label;
	}

	private JPanel crearPanelResultado() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(TemaOscuro.bordePanel());
		panel.setBackground(TemaOscuro.FONDO_PANEL);

		JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
		btnResolver = new JButton("Resolver");
		panelSuperior.add(btnResolver, BorderLayout.NORTH);

		lblProgreso = new JLabel(" ");
		barraProgreso = new JProgressBar(0, 100);
		barraProgreso.setStringPainted(true);
		barraProgreso.setVisible(false);
		lblProgreso.setVisible(false);

		JPanel panelProgreso = new JPanel(new BorderLayout(5, 2));
		panelProgreso.add(lblProgreso, BorderLayout.NORTH);
		panelProgreso.add(barraProgreso, BorderLayout.CENTER);
		panelSuperior.add(panelProgreso, BorderLayout.CENTER);

		panel.add(panelSuperior, BorderLayout.NORTH);

		areaResultado = new JTextArea();
		areaResultado.setEditable(false);
		TemaOscuro.estilizarAreaTexto(areaResultado);
		panel.add(new JScrollPane(areaResultado), BorderLayout.CENTER);

		lblProgreso.setForeground(TemaOscuro.TEXTO);

		btnResolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resolver();
			}
		});

		TemaOscuro.estilizarBoton(btnResolver);
		return panel;
	}

	private void configurarSpinnerSoloFlechas(JSpinner spinner) {
		TemaOscuro.estilizarSpinner(spinner, true);
	}

	// ---- Acciones (delegan en el controlador) ----

	private void agregarPersona() {
		try {
			String nombre = txtNombre.getText().trim();
			Rol rol = (Rol) cmbRol.getSelectedItem();
			int calif = (Integer) spnCalificacion.getValue();
			controlador.agregarPersona(nombre, rol, calif);
			txtNombre.setText("");
			refrescarPersonas();
			controlador.guardarEstadoSilencioso();
		} catch (IllegalArgumentException ex) {
			TemaOscuro.mostrarMensaje(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void borrarPersona() {
		try {
			String nombre = txtNombreBorrar.getText().trim();
			controlador.eliminarPersona(nombre);
			txtNombreBorrar.setText("");
			refrescarPersonas();
			refrescarIncompatibilidades();
			controlador.guardarEstadoSilencioso();
		} catch (IllegalArgumentException ex) {
			TemaOscuro.mostrarMensaje(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void borrarTodasLasPersonas() {
		if (controlador.getPersonas().isEmpty()) {
			TemaOscuro.mostrarMensaje(this, "No hay personas para borrar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		int opcion = TemaOscuro.mostrarConfirmacion(this,
				"Se eliminarán todas las personas y sus incompatibilidades.\n¿Continuar?",
				"Borrar todas las personas");
		if (opcion != JOptionPane.YES_OPTION) {
			return;
		}
		controlador.eliminarTodasLasPersonas();
		txtNombreBorrar.setText("");
		refrescarPersonas();
		refrescarIncompatibilidades();
		controlador.guardarEstadoSilencioso();
	}

	private void agregarIncompatibilidad() {
		try {
			String a = (String) cmbPersonaA.getSelectedItem();
			String b = (String) cmbPersonaB.getSelectedItem();
			if (a == null || b == null) {
				TemaOscuro.mostrarMensaje(this, "Cargue personas primero", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}
			controlador.agregarIncompatibilidad(a, b);
			refrescarIncompatibilidades();
			controlador.guardarEstadoSilencioso();
		} catch (IllegalArgumentException ex) {
			TemaOscuro.mostrarMensaje(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void eliminarIncompatibilidad() {
		try {
			String a = (String) cmbPersonaA.getSelectedItem();
			String b = (String) cmbPersonaB.getSelectedItem();
			if (a == null || b == null) {
				TemaOscuro.mostrarMensaje(this, "Cargue personas primero", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}
			controlador.eliminarIncompatibilidad(a, b);
			refrescarIncompatibilidades();
			controlador.guardarEstadoSilencioso();
		} catch (IllegalArgumentException ex) {
			TemaOscuro.mostrarMensaje(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void resolver() {
		sincronizarRequerimientos();

		btnResolver.setEnabled(false);
		areaResultado.setText("Resolviendo...\n\n");
		iniciarBarraProgreso();

		controlador.resolverAsync(new Consumer<EstadisticasSolver>() {
			public void accept(EstadisticasSolver estadisticas) {
				mostrarEstadisticasEnEjecucion(estadisticas);
			}
		}, new Consumer<ResultadoResolucion>() {
			public void accept(ResultadoResolucion resultado) {
				resolucionTerminada = true;
				resultadoPendiente = resultado;
			}
		});
	}

	private void mostrarEstadisticasEnEjecucion(EstadisticasSolver estadisticas) {
		if (estadisticas == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Resolviendo...\n\n");
		sb.append(estadisticas);
		areaResultado.setText(sb.toString());
	}

	private void iniciarBarraProgreso() {
		detenerTimerProgreso();
		resolucionTerminada = false;
		faseFinalizacion = false;
		resultadoPendiente = null;
		progresoVisual = 0;
		inicioResolucion = System.currentTimeMillis();

		barraProgreso.setVisible(true);
		lblProgreso.setVisible(true);
		actualizarBarraProgresoMonotono(0);
		lblProgreso.setText("Progreso de la búsqueda:");

		timerProgreso = new Timer(INTERVALO_ANIMACION_MS, e -> avanzarBarraProgreso());
		timerProgreso.start();
	}

	private void avanzarBarraProgreso() {
		if (resolucionTerminada && !faseFinalizacion) {
			faseFinalizacion = true;
			inicioFaseFinal = System.currentTimeMillis();
			progresoInicioFaseFinal = progresoVisual;
		}

		int objetivo;
		if (!faseFinalizacion) {
			long elapsed = System.currentTimeMillis() - inicioResolucion;
			objetivo = (int) Math.min(PROGRESO_MAXIMO_BUSQUEDA,
					elapsed * PROGRESO_MAXIMO_BUSQUEDA / (long) DURACION_SUBIDA_BUSQUEDA_MS);
		} else {
			long elapsedFinal = System.currentTimeMillis() - inicioFaseFinal;
			int rango = 100 - progresoInicioFaseFinal;
			objetivo = progresoInicioFaseFinal
					+ (int) (rango * elapsedFinal / (double) DURACION_COMPLETAR_MS);
			objetivo = Math.min(100, objetivo);
		}

		actualizarBarraProgresoMonotono(objetivo);

		if (faseFinalizacion && progresoVisual >= 100) {
			detenerTimerProgreso();
			completarResolucion();
		}
	}

	private void actualizarBarraProgresoMonotono(int porcentaje) {
		int valor = Math.max(0, Math.min(100, porcentaje));
		if (valor < progresoVisual) {
			return;
		}
		progresoVisual = valor;
		barraProgreso.setValue(progresoVisual);
		barraProgreso.setString(progresoVisual + "%");
	}

	private void completarResolucion() {
		actualizarBarraProgresoMonotono(100);
		mostrarResultadoResolucion(resultadoPendiente);
		ocultarBarraProgreso();
		btnResolver.setEnabled(true);
	}

	private void detenerTimerProgreso() {
		if (timerProgreso != null) {
			timerProgreso.stop();
			timerProgreso = null;
		}
	}

	private void mostrarResultadoResolucion(ResultadoResolucion resultado) {
		if (!resultado.esExito()) {
			areaResultado.setText("");
			ocultarBarraProgreso();
			TemaOscuro.mostrarMensaje(this,
					"Error al resolver: " + resultado.getMensajeError(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		StringBuilder sb = new StringBuilder();
		if (resultado.getEquipo().estaVacio()) {
			sb.append("No se encontró un equipo que cumpla los requerimientos.");
		} else {
			sb.append(resultado.getEquipo().toString());
		}
		sb.append("\n\n").append(resultado.getEstadisticas());
		areaResultado.setText(sb.toString());
		ocultarBarraProgreso();
	}

	private void ocultarBarraProgreso() {
		barraProgreso.setVisible(false);
		lblProgreso.setVisible(false);
	}

	private void sincronizarRequerimientos() {
		controlador.setRequerimiento(Rol.LIDER_DE_PROYECTO, (Integer) spnLider.getValue());
		controlador.setRequerimiento(Rol.ARQUITECTO, (Integer) spnArquitecto.getValue());
		controlador.setRequerimiento(Rol.PROGRAMADOR, (Integer) spnProgramador.getValue());
		controlador.setRequerimiento(Rol.TESTER, (Integer) spnTester.getValue());
	}

	private void persistirRequerimientos() {
		sincronizarRequerimientos();
		controlador.guardarEstadoSilencioso();
	}

	private void aplicarRequerimientosEnSpinners() {
		Requerimientos req = controlador.getRequerimientos();
		actualizarSpinnerSinEvento(spnLider, req.getCantidad(Rol.LIDER_DE_PROYECTO));
		actualizarSpinnerSinEvento(spnArquitecto, req.getCantidad(Rol.ARQUITECTO));
		actualizarSpinnerSinEvento(spnProgramador, req.getCantidad(Rol.PROGRAMADOR));
		actualizarSpinnerSinEvento(spnTester, req.getCantidad(Rol.TESTER));
	}

	private void actualizarSpinnerSinEvento(JSpinner spinner, int valor) {
		ChangeListener[] listeners = spinner.getChangeListeners();
		for (ChangeListener listener : listeners) {
			spinner.removeChangeListener(listener);
		}
		spinner.setValue(valor);
		for (ChangeListener listener : listeners) {
			spinner.addChangeListener(listener);
		}
	}
	private void cargarDatosGuardados() {
		try {
			if (controlador.cargarEstado()) {
				aplicarRequerimientosEnSpinners();
				refrescarPersonas();
				refrescarIncompatibilidades();
			}
		} catch (IOException | ClassNotFoundException ex) {
			TemaOscuro.mostrarMensaje(this,
					"No se pudieron cargar los datos guardados: " + ex.getMessage(),
					"Advertencia",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void cerrarAplicacion() {
		detenerTimerProgreso();
		sincronizarRequerimientos();
		try {
			controlador.guardarEstado();
		} catch (IOException ex) {
			int opcion = TemaOscuro.mostrarConfirmacion(this,
					"No se pudieron guardar los datos: " + ex.getMessage()
							+ "\n¿Desea cerrar igualmente?",
					"Error al guardar");
			if (opcion != JOptionPane.YES_OPTION) {
				return;
			}
		}
		dispose();
		System.exit(0);
	}

	private void refrescarPersonas() {
		List<Persona> personas = controlador.getPersonas();
		areaPersonas.setText(resumenPersonas(personas));
		actualizarCombosPersonas(personas);
	}

	private String resumenPersonas(List<Persona> personas) {
		if (personas.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Total: ").append(personas.size()).append(" empleados\n");
		for (Rol rol : Rol.values()) {
			int cantidad = 0;
			for (Persona p : personas) {
				if (p.getRol() == rol) {
					cantidad++;
				}
			}
			sb.append("  ").append(rol).append(": ").append(cantidad).append("\n");
		}

		if (personas.size() > LIMITE_LISTADO_UI) {
			sb.append("\n(Mostrando los primeros ").append(LIMITE_LISTADO_UI).append(")\n");
		} else {
			sb.append("\n");
		}

		int limite = Math.min(personas.size(), LIMITE_LISTADO_UI);
		for (int i = 0; i < limite; i++) {
			sb.append(personas.get(i)).append("\n");
		}
		return sb.toString();
	}

	private void actualizarCombosPersonas(List<Persona> personas) {
		if (personas.size() > LIMITE_COMBO_UI) {
			cmbPersonaA.setModel(new DefaultComboBoxModel<>());
			cmbPersonaB.setModel(new DefaultComboBoxModel<>());
			return;
		}
		String[] nombres = new String[personas.size()];
		for (int i = 0; i < personas.size(); i++) {
			nombres[i] = personas.get(i).getNombre();
		}
		cmbPersonaA.setModel(new DefaultComboBoxModel<>(nombres));
		cmbPersonaB.setModel(new DefaultComboBoxModel<>(nombres));
	}

	private void refrescarIncompatibilidades() {
		List<Incompatibilidad> incompatibilidades = controlador.getIncompatibilidades();
		if (incompatibilidades.isEmpty()) {
			areaIncompat.setText("");
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Total: ").append(incompatibilidades.size()).append(" incompatibilidades\n\n");

		int limite = Math.min(incompatibilidades.size(), LIMITE_LISTADO_UI);
		for (int i = 0; i < limite; i++) {
			sb.append(incompatibilidades.get(i)).append("\n");
		}
		if (incompatibilidades.size() > LIMITE_LISTADO_UI) {
			sb.append("\n(... ").append(incompatibilidades.size() - LIMITE_LISTADO_UI)
					.append(" más no mostradas)");
		}
		areaIncompat.setText(sb.toString());
	}
}
