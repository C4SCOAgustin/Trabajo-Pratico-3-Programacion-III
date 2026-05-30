package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import controlador.Controlador;
import modelo.Equipo;
import modelo.Persona;
import modelo.Rol;

/**
 * Vista (capa Vista del MVT). Solo interfaz, sin lógica de negocio.
 * Delega todo al Controlador.
 */
public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private final Controlador controlador = new Controlador();

	// Pestaña Personas
	private JTextField txtNombre;
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

	public VentanaPrincipal() {
		setTitle("Equipo Ideal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);

		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Personas", crearPanelPersonas());
		tabs.addTab("Incompatibilidades", crearPanelIncompatibilidades());
		tabs.addTab("Requerimientos", crearPanelRequerimientos());
		tabs.addTab("Resultado", crearPanelResultado());
		getContentPane().add(tabs);
	}

	private JPanel crearPanelPersonas() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
		form.add(new JLabel("Nombre:"));
		txtNombre = new JTextField();
		form.add(txtNombre);

		form.add(new JLabel("Rol:"));
		cmbRol = new JComboBox<>(Rol.values());
		form.add(cmbRol);

		form.add(new JLabel("Calificación (1-5):"));
		spnCalificacion = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
		form.add(spnCalificacion);

		JButton btnAgregar = new JButton("Agregar persona");
		form.add(btnAgregar);
		form.add(new JLabel(""));

		panel.add(form, BorderLayout.NORTH);

		areaPersonas = new JTextArea();
		areaPersonas.setEditable(false);
		panel.add(new JScrollPane(areaPersonas), BorderLayout.CENTER);

		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregarPersona();
			}
		});

		return panel;
	}

	private JPanel crearPanelIncompatibilidades() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
		form.add(new JLabel("Persona A:"));
		cmbPersonaA = new JComboBox<>();
		form.add(cmbPersonaA);

		form.add(new JLabel("Persona B:"));
		cmbPersonaB = new JComboBox<>();
		form.add(cmbPersonaB);

		JButton btnAgregar = new JButton("Agregar incompatibilidad");
		form.add(btnAgregar);
		form.add(new JLabel(""));

		panel.add(form, BorderLayout.NORTH);

		areaIncompat = new JTextArea();
		areaIncompat.setEditable(false);
		panel.add(new JScrollPane(areaIncompat), BorderLayout.CENTER);

		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregarIncompatibilidad();
			}
		});

		return panel;
	}

	private JPanel crearPanelRequerimientos() {
		JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		panel.add(new JLabel("Líderes de proyecto:"));
		spnLider = new JSpinner(new SpinnerNumberModel(1, 0, 50, 1));
		panel.add(spnLider);

		panel.add(new JLabel("Arquitectos:"));
		spnArquitecto = new JSpinner(new SpinnerNumberModel(1, 0, 50, 1));
		panel.add(spnArquitecto);

		panel.add(new JLabel("Programadores:"));
		spnProgramador = new JSpinner(new SpinnerNumberModel(2, 0, 50, 1));
		panel.add(spnProgramador);

		panel.add(new JLabel("Testers:"));
		spnTester = new JSpinner(new SpinnerNumberModel(1, 0, 50, 1));
		panel.add(spnTester);

		return panel;
	}

	private JPanel crearPanelResultado() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		btnResolver = new JButton("Resolver");
		panel.add(btnResolver, BorderLayout.NORTH);

		areaResultado = new JTextArea();
		areaResultado.setEditable(false);
		panel.add(new JScrollPane(areaResultado), BorderLayout.CENTER);

		btnResolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resolver();
			}
		});

		return panel;
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
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}

	private void agregarIncompatibilidad() {
		try {
			String a = (String) cmbPersonaA.getSelectedItem();
			String b = (String) cmbPersonaB.getSelectedItem();
			if (a == null || b == null) {
				JOptionPane.showMessageDialog(this, "Cargue personas primero");
				return;
			}
			controlador.agregarIncompatibilidad(a, b);
			refrescarIncompatibilidades();
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}

	private void resolver() {
		controlador.setRequerimiento(Rol.LIDER_DE_PROYECTO, (Integer) spnLider.getValue());
		controlador.setRequerimiento(Rol.ARQUITECTO, (Integer) spnArquitecto.getValue());
		controlador.setRequerimiento(Rol.PROGRAMADOR, (Integer) spnProgramador.getValue());
		controlador.setRequerimiento(Rol.TESTER, (Integer) spnTester.getValue());

		btnResolver.setEnabled(false);
		areaResultado.setText("Resolviendo...");

		controlador.resolverAsync(new java.util.function.Consumer<Equipo>() {
			public void accept(Equipo equipo) {
				if (equipo.estaVacio()) {
					areaResultado.setText("No se encontró un equipo que cumpla los requerimientos.");
				} else {
					areaResultado.setText(equipo.toString());
				}
				btnResolver.setEnabled(true);
			}
		});
	}

	private void refrescarPersonas() {
		StringBuilder sb = new StringBuilder();
		List<Persona> personas = controlador.getPersonas();
		for (Persona p : personas) {
			sb.append(p).append("\n");
		}
		areaPersonas.setText(sb.toString());

		// actualizar combos de incompatibilidades
		String[] nombres = new String[personas.size()];
		for (int i = 0; i < personas.size(); i++) {
			nombres[i] = personas.get(i).getNombre();
		}
		cmbPersonaA.setModel(new DefaultComboBoxModel<>(nombres));
		cmbPersonaB.setModel(new DefaultComboBoxModel<>(nombres));
	}

	private void refrescarIncompatibilidades() {
		StringBuilder sb = new StringBuilder();
		for (Object i : controlador.getIncompatibilidades()) {
			sb.append(i).append("\n");
		}
		areaIncompat.setText(sb.toString());
	}
}
