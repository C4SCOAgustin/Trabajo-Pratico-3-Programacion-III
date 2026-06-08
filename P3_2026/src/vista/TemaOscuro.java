package vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;

/** Aplica un tema oscuro consistente a los componentes Swing. */
public final class TemaOscuro {

	public static final Color FONDO = new Color(0x1e1e1e);
	public static final Color FONDO_PANEL = new Color(0x2b2b2b);
	public static final Color FONDO_CAMPO = new Color(0x3c3f41);
	public static final Color TEXTO = new Color(0xe0e0e0);
	public static final Color TEXTO_SECUNDARIO = new Color(0xb0b0b0);
	public static final Color ACENTO = new Color(0x4a9eff);
	public static final Color BORDE = new Color(0x4a4a4a);
	public static final Color SELECCION = new Color(0x4a6fa5);

	private TemaOscuro() {
	}

	public static void aplicar() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception ignored) {
			// Se usan los colores por defecto del LAF actual.
		}

		UIManager.put("control", new ColorUIResource(FONDO_PANEL));
		UIManager.put("info", new ColorUIResource(FONDO_PANEL));
		UIManager.put("nimbusBase", new ColorUIResource(FONDO));
		UIManager.put("nimbusAlertYellow", new ColorUIResource(ACENTO));
		UIManager.put("nimbusDisabledText", new ColorUIResource(TEXTO_SECUNDARIO));
		UIManager.put("nimbusFocus", new ColorUIResource(ACENTO));
		UIManager.put("nimbusGreen", new ColorUIResource(ACENTO));
		UIManager.put("nimbusInfoBlue", new ColorUIResource(ACENTO));
		UIManager.put("nimbusLightBackground", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("nimbusOrange", new ColorUIResource(ACENTO));
		UIManager.put("nimbusRed", new ColorUIResource(ACENTO));
		UIManager.put("nimbusSelectedText", new ColorUIResource(TEXTO));
		UIManager.put("nimbusSelectionBackground", new ColorUIResource(SELECCION));

		// Fuente por defecto más grande y legible
		int tamanoFuente = 14;
		java.awt.Font fuentePorDefecto = new Font("Segoe UI", Font.PLAIN, tamanoFuente);
		UIManager.put("defaultFont", fuentePorDefecto);
		UIManager.put("Label.font", fuentePorDefecto);
		UIManager.put("Button.font", fuentePorDefecto);
		UIManager.put("TextField.font", fuentePorDefecto);
		UIManager.put("TextArea.font", fuentePorDefecto);
		UIManager.put("ComboBox.font", fuentePorDefecto);
		UIManager.put("Spinner.font", fuentePorDefecto);
		UIManager.put("TabbedPane.font", fuentePorDefecto);
		UIManager.put("ProgressBar.font", fuentePorDefecto);

		UIManager.put("Panel.background", new ColorUIResource(FONDO_PANEL));
		UIManager.put("Panel.foreground", new ColorUIResource(TEXTO));
		UIManager.put("Label.background", new ColorUIResource(FONDO_PANEL));
		UIManager.put("Label.foreground", new ColorUIResource(TEXTO));

		UIManager.put("Button.background", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("Button.foreground", new ColorUIResource(TEXTO));
		UIManager.put("Button.select", new ColorUIResource(SELECCION));
		UIManager.put("Button.focus", new ColorUIResource(ACENTO));
		UIManager.put("Button.shadow", new ColorUIResource(BORDE));
		UIManager.put("Button.darkShadow", new ColorUIResource(FONDO));
		UIManager.put("Button.light", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("Button.highlight", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("Button.disabledShadow", new ColorUIResource(BORDE));
		UIManager.put("Button.disabledDarkShadow", new ColorUIResource(FONDO));
		UIManager.put("Button.disabledHighlight", new ColorUIResource(FONDO_PANEL));
		UIManager.put("Button.disabledLight", new ColorUIResource(FONDO_PANEL));
		UIManager.put("Button.disabledText", new ColorUIResource(TEXTO_SECUNDARIO));

		UIManager.put("ToggleButton.background", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("ToggleButton.foreground", new ColorUIResource(TEXTO));

		UIManager.put("TextField.background", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("TextField.foreground", new ColorUIResource(TEXTO));
		UIManager.put("TextField.caretForeground", new ColorUIResource(TEXTO));
		UIManager.put("TextField.inactiveForeground", new ColorUIResource(TEXTO_SECUNDARIO));
		UIManager.put("TextArea.background", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("TextArea.foreground", new ColorUIResource(TEXTO));
		UIManager.put("TextArea.caretForeground", new ColorUIResource(TEXTO));
		UIManager.put("FormattedTextField.background", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("FormattedTextField.foreground", new ColorUIResource(TEXTO));
		UIManager.put("FormattedTextField.caretForeground", new ColorUIResource(TEXTO));
		UIManager.put("FormattedTextField.inactiveBackground", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("FormattedTextField.inactiveForeground", new ColorUIResource(TEXTO));

		UIManager.put("ComboBox.background", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("ComboBox.foreground", new ColorUIResource(TEXTO));
		UIManager.put("ComboBox.selectionBackground", new ColorUIResource(SELECCION));
		UIManager.put("ComboBox.selectionForeground", new ColorUIResource(TEXTO));
		UIManager.put("ComboBox.buttonBackground", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("ComboBox.buttonHighlight", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("ComboBox.buttonShadow", new ColorUIResource(BORDE));
		UIManager.put("ComboBox.buttonDarkShadow", new ColorUIResource(FONDO));

		UIManager.put("Spinner.background", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("Spinner.foreground", new ColorUIResource(TEXTO));
		UIManager.put("Spinner.arrowButtonBackground", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("Spinner.arrowButtonBorder", BorderFactory.createLineBorder(BORDE));
		UIManager.put("Spinner.arrowButtonShadow", new ColorUIResource(BORDE));
		UIManager.put("Spinner.arrowButtonHighlight", new ColorUIResource(FONDO_CAMPO));

		UIManager.put("TabbedPane.background", new ColorUIResource(FONDO));
		UIManager.put("TabbedPane.foreground", new ColorUIResource(TEXTO_SECUNDARIO));
		UIManager.put("TabbedPane.selected", new ColorUIResource(FONDO_PANEL));
		UIManager.put("TabbedPane.selectedForeground", new ColorUIResource(TEXTO));
		UIManager.put("TabbedPane.contentAreaColor", new ColorUIResource(FONDO_PANEL));
		UIManager.put("TabbedPane.highlight", new ColorUIResource(BORDE));
		UIManager.put("TabbedPane.focus", new ColorUIResource(ACENTO));

		UIManager.put("ProgressBar.background", new ColorUIResource(FONDO_CAMPO));
		UIManager.put("ProgressBar.foreground", new ColorUIResource(ACENTO));
		UIManager.put("ProgressBar.selectionBackground", new ColorUIResource(ACENTO));
		UIManager.put("ProgressBar.selectionForeground", new ColorUIResource(TEXTO));

		UIManager.put("ScrollPane.background", new ColorUIResource(FONDO_PANEL));
		UIManager.put("ScrollBar.background", new ColorUIResource(FONDO_PANEL));
		UIManager.put("ScrollBar.thumb", new ColorUIResource(BORDE));
		UIManager.put("ScrollBar.track", new ColorUIResource(FONDO));

		UIManager.put("OptionPane.background", new ColorUIResource(FONDO_PANEL));
		UIManager.put("OptionPane.foreground", new ColorUIResource(TEXTO));
		UIManager.put("OptionPane.messageForeground", new ColorUIResource(TEXTO));
	}

	public static Border bordePanel() {
		return BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDE),
				BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	public static Border bordeCampo() {
		return BorderFactory.createLineBorder(BORDE);
	}

	public static void estilizarAreaTexto(JTextArea area) {
		area.setBackground(FONDO_CAMPO);
		area.setForeground(TEXTO);
		area.setCaretColor(TEXTO);
		area.setFont(new Font("Monospaced", Font.PLAIN, 14));
		area.setBorder(bordeCampo());
	}

	public static void estilizarCampoTexto(JTextField campo) {
		campo.setBackground(FONDO_CAMPO);
		campo.setForeground(TEXTO);
		campo.setCaretColor(TEXTO);
		campo.setOpaque(true);
		campo.setBorder(bordeCampo());
	}

	public static void estilizarComboBox(JComboBox<?> combo) {
		combo.setBackground(FONDO_CAMPO);
		combo.setForeground(TEXTO);
		combo.setOpaque(true);
	}

	public static void estilizarBoton(JButton boton) {
		boton.setBackground(FONDO_CAMPO);
		boton.setForeground(TEXTO);
		boton.setOpaque(true);
		boton.setFocusPainted(false);
		boton.setBorderPainted(true);
		boton.setContentAreaFilled(true);
		boton.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDE),
				BorderFactory.createEmptyBorder(6, 12, 6, 12)));
	}

	public static void estilizarSpinner(JSpinner spinner, boolean soloFlechas) {
		spinner.setBackground(FONDO_CAMPO);
		spinner.setForeground(TEXTO);
		spinner.setOpaque(true);

		JComponent editor = spinner.getEditor();
		if (editor instanceof JSpinner.DefaultEditor) {
			JSpinner.DefaultEditor defaultEditor = (JSpinner.DefaultEditor) editor;
			defaultEditor.setBackground(FONDO_CAMPO);
			defaultEditor.setOpaque(true);

			JFormattedTextField campo = defaultEditor.getTextField();
			campo.setBackground(FONDO_CAMPO);
			campo.setForeground(TEXTO);
			campo.setCaretColor(TEXTO);
			campo.setOpaque(true);
			campo.setBorder(bordeCampo());
			if (soloFlechas) {
				campo.setEditable(false);
				// reducir columnas para que el cuadro sea más pequeño (1 caracter)
				campo.setColumns(1);
			} else {
				campo.setColumns(3);
			}

			// Ajustar tamaño preferido del spinner para respetar el ancho del campo
			int charWidth = campo.getFontMetrics(campo.getFont()).charWidth('0');
			int targetWidth = Math.max(24, charWidth * (campo.getColumns() + 1));
			Dimension d = spinner.getPreferredSize();
			d.width = targetWidth + 32; // espacio para botones
			spinner.setPreferredSize(d);
		}

		for (Component hijo : spinner.getComponents()) {
			if (hijo instanceof JButton) {
				estilizarBoton((JButton) hijo);
			} else {
				hijo.setBackground(FONDO_CAMPO);
				hijo.setForeground(TEXTO);
			}
		}
	}

	public static void estilizarEtiqueta(JLabel etiqueta) {
		etiqueta.setForeground(TEXTO);
		etiqueta.setBackground(FONDO_PANEL);
		etiqueta.setOpaque(false);
	}

	public static void estilizarContenedor(Container contenedor) {
		for (Component componente : contenedor.getComponents()) {
			if (componente instanceof JButton) {
				estilizarBoton((JButton) componente);
			} else if (componente instanceof JTextField) {
				estilizarCampoTexto((JTextField) componente);
			} else if (componente instanceof JComboBox) {
				estilizarComboBox((JComboBox<?>) componente);
			} else if (componente instanceof JSpinner) {
				estilizarSpinner((JSpinner) componente, false);
			} else if (componente instanceof JLabel) {
				estilizarEtiqueta((JLabel) componente);
			} else if (componente instanceof JTextArea) {
				estilizarAreaTexto((JTextArea) componente);
			} else if (componente instanceof Container) {
				estilizarContenedor((Container) componente);
			}
		}
	}

	private static void configurarOptionPane(JOptionPane pane) {
		pane.setBackground(FONDO_PANEL);
		pane.setForeground(TEXTO);
		pane.setOpaque(true);
		for (Component hijo : pane.getComponents()) {
			if (hijo instanceof JLabel) {
				JLabel lbl = (JLabel) hijo;
				lbl.setForeground(TEXTO);
				lbl.setBackground(FONDO_PANEL);
			}
		}
	}

	private static void configurarDialogo(JDialog dialogo) {
		dialogo.getContentPane().setBackground(FONDO_PANEL);
		estilizarContenedor(dialogo.getContentPane());
		if (dialogo.getRootPane() != null) {
			dialogo.getRootPane().setBackground(FONDO_PANEL);
			estilizarContenedor(dialogo.getRootPane());
		}
	}

	public static void mostrarMensaje(java.awt.Component padre, String mensaje, String titulo, int tipo) {
		JOptionPane pane = new JOptionPane(mensaje, tipo);
		configurarOptionPane(pane);
		JDialog dialogo = pane.createDialog(padre, titulo);
		configurarDialogo(dialogo);
		estilizarContenedor(pane);
		dialogo.setVisible(true);
		dialogo.dispose();
	}

	public static int mostrarConfirmacion(java.awt.Component padre, String mensaje, String titulo) {
		JOptionPane pane = new JOptionPane(mensaje, JOptionPane.WARNING_MESSAGE,
				JOptionPane.YES_NO_OPTION);
		configurarOptionPane(pane);
		JDialog dialogo = pane.createDialog(padre, titulo);
		configurarDialogo(dialogo);
		estilizarContenedor(pane);
		dialogo.setVisible(true);
		Object valor = pane.getValue();
		dialogo.dispose();
		if (valor instanceof Integer && (Integer) valor == JOptionPane.YES_OPTION) {
			return JOptionPane.YES_OPTION;
		}
		return JOptionPane.NO_OPTION;
	}
}

