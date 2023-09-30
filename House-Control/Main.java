import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Main {
    private static ArrayList<Main> botonesActivos = new ArrayList<>();
    private static boolean deshaciendo = false;
    private static Stack<HistorialAccion> accionHistorial = new Stack<>();
    private static SeHaSeleccionado panelAcciones;

    private boolean estado = false;
    private JFrame ventana;
    private String nombre;
    private JPanel botonFrame;
    private JLabel label;
    private JButton botonToggle;
    private ImageIcon imagenOn;
    private ImageIcon imagenOff;
    private ImageIcon imagenOn2;
    private ImageIcon imagenOff2;
    private JLabel labelImagen;

    public Main(JFrame ventana, String nombre, SeHaSeleccionado panelAcciones) {
        this.ventana = ventana;
        this.nombre = nombre;
        this.botonFrame = new JPanel();
        this.ventana.add(botonFrame);
        this.botonFrame.setBackground(new Color(0, 15, 47));

        this.ventana.add(botonFrame);

        botonFrame.setLayout(new FlowLayout(FlowLayout.LEFT));
        imagenOn = scaleImageIcon("on-button.png", 50, 50);
        imagenOff = scaleImageIcon("off-button.png", 50, 50);
        imagenOn2 = scaleImageIcon("on.png", 300, 300);
        imagenOff2 = scaleImageIcon("off.png", 300, 300);
        labelImagen = new JLabel(imagenOff);
        botonFrame.add(labelImagen);

        Font nuevaFuente = new Font("Arial", Font.BOLD, 17);
        this.label = new JLabel(nombre);
        this.label.setForeground(Color.WHITE);
        this.label.setFont(nuevaFuente);
        this.botonFrame.add(label);

        this.botonToggle = new JButton("ON  | OFF");
        this.botonToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggle();
            }
        });
        this.botonFrame.add(botonToggle);

        botonesActivos.add(this);
    }

    public boolean getEstado() {
        return estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void toggle() {
        if (estado) {
            apagar();
        } else {
            encender();
        }
    }

    public void encender() {
        estado = true;
        labelImagen.setIcon(imagenOn);
        if (!deshaciendo) {
            accionHistorial.push(new HistorialAccion(this.nombre, "ON"));
            panelAcciones.notificarObservadores(this.nombre);
            panelAcciones.mostrarEtiqueta("Has been selected: " + this.nombre + " ON");
            panelAcciones.mostrarImagen(imagenOn2);
        }
    }

    public void apagar() {
        estado = false;
        labelImagen.setIcon(imagenOff);
        if (!deshaciendo) {
            accionHistorial.push(new HistorialAccion(this.nombre, "OFF"));
            panelAcciones.notificarObservadores(this.nombre);
            panelAcciones.mostrarEtiqueta("Has been selected: " + this.nombre + " OFF");
            panelAcciones.mostrarImagen(imagenOff2);
        }
    }

    private ImageIcon scaleImageIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    public static void vacationMode() {
        boolean alMenosUnEncendido = false;

        for (Main boton : botonesActivos) {
            if (boton.getEstado()) {
                boton.apagar();
                alMenosUnEncendido = true;
            }
        }

        if (alMenosUnEncendido) {
            JOptionPane.showMessageDialog(null, "Vacation mode has been selected, all devices have been turned off", "Vacation Mode", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void undoAction(Stack<HistorialAccion> accionHistorial) {
        if (!accionHistorial.isEmpty()) {
            deshaciendo = true;
            HistorialAccion historialAccion = accionHistorial.pop();
            String accion = historialAccion.getEstado();
            String nombreBoton = historialAccion.getNombre();
            Main boton = buscarBoton(nombreBoton);

            if (accion.equals("ON")) {
                boton.apagar();
            } else if (accion.equals("OFF")) {
                boton.encender();
            }
            deshaciendo = false;
        }
    }

    public static Main buscarBoton(String nombreBoton) {
        for (Main boton : botonesActivos) {
            if (boton.getNombre().equals(nombreBoton)) {
                return boton;
            }
        }
        return null;
    }

    public static Stack<HistorialAccion> getActionHistory() {
        return accionHistorial;
    }
}
