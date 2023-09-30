import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BotonControl {
    private static ArrayList<BotonControl> botonesActivos = new ArrayList<>();
    private static boolean deshaciendo = false;
    private static Stack<HistorialAccion> accionHistorial = new Stack<>();
    private static SeHaSeleccionado panelAcciones;

    private boolean estado = false;
    private JFrame ventana;
    private String nombre;
    private JPanel botonFrame;
    private JLabel label;
    private JButton botonToggle; // Un solo botón para encender/apagar
    // Imágenes para ON y OFF
    private ImageIcon imagenOn;
    private ImageIcon imagenOff;
    private ImageIcon imagenOn2; 
    private ImageIcon imagenOff2;
    private JLabel labelImagen;

    // Constructor de BotonControl
    public BotonControl(JFrame ventana, String nombre) {
        this.ventana = ventana;
        this.nombre = nombre;
        this.botonFrame = new JPanel();
        this.ventana.add(botonFrame);

        // Cambiar el color de fondo del panel que contiene los botones
        this.botonFrame.setBackground(new Color(0, 15, 47)); // Cambiar el color

        this.ventana.add(botonFrame);

        botonFrame.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Imágenes para ON y OFF 
        imagenOn = scaleImageIcon("on-button.png", 50, 50); 
        imagenOff = scaleImageIcon("off-button.png", 50, 50); 

        // Imagen diferente para el nuevo estado
        imagenOn2 = scaleImageIcon("on.png", 300, 300);
        imagenOff2 = scaleImageIcon("off.png", 300, 300);

        labelImagen = new JLabel(imagenOff); // Inicialmente, mostrar la imagen OFF
        botonFrame.add(labelImagen);

        Font nuevaFuente = new Font("Arial", Font.BOLD, 17); // Cambiar el tipo de letra, tamaño y estilo
        this.label = new JLabel(nombre);
        // Cambiar el color del texto (nombre del botón)
        this.label.setForeground(Color.WHITE); // Cambiar el color

        this.label.setFont(nuevaFuente); // Aplicar el tipo de letra y el tamaño personalizado
        this.botonFrame.add(label);

        this.botonToggle = new JButton("ON  | OFF");
        this.botonToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggle(); // Alternar entre encender y apagar
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

    // Método para alternar entre encender y apagar
    public void toggle() {
        if (estado) {
            apagar();
        } else {
            encender();
        }
    }

    // Método para encender el botón
    public void encender() {
        estado = true;
        labelImagen.setIcon(imagenOn); // Cambiar la imagen a ON
        if (!deshaciendo) {
            accionHistorial.push(new HistorialAccion(this.nombre, "ON"));
            panelAcciones.notificarObservadores(this.nombre);
            panelAcciones.mostrarEtiqueta("Has been selected: " + this.nombre + " ON");
            panelAcciones.mostrarImagen(imagenOn2); 
        }
    }

    // Método para apagar el botón
    public void apagar() {
        estado = false;
        labelImagen.setIcon(imagenOff); // Cambiar la imagen a OFF
        if (!deshaciendo) {
            accionHistorial.push(new HistorialAccion(this.nombre, "OFF"));
            panelAcciones.notificarObservadores(this.nombre);
            panelAcciones.mostrarEtiqueta("Has been selected: " + this.nombre + " OFF");
            panelAcciones.mostrarImagen(imagenOff2); 
        }
    }

    // Método para escalar una ImageIcon al tamaño deseado
    private ImageIcon scaleImageIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    // Método estático para crear la interfaz de usuario
    public static void crearInterfaz() {
        JFrame ventana = new JFrame("House Control");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new BorderLayout());

        ventana.getContentPane().setBackground(Color.BLACK);

        String[] nombresBotones = {"CeilingFan", "GarageDoor", "LivingRoom", "HotTub", "Stereo", "TV", "Lights"};

        JPanel panelHorizontal = new JPanel();
        panelHorizontal.setLayout(new BoxLayout(panelHorizontal, BoxLayout.Y_AXIS));

        panelHorizontal.setBorder(BorderFactory.createEmptyBorder());

        panelHorizontal.setBackground(new Color(1, 28, 86)); // Cambiar el color de fondo

        for (String nombre : nombresBotones) {
            BotonControl boton = new BotonControl(ventana, nombre);
            panelHorizontal.add(boton.botonFrame);
        }

        JScrollPane scrollPane = new JScrollPane(panelHorizontal);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        ventana.add(scrollPane, BorderLayout.CENTER);

        panelAcciones = new SeHaSeleccionado();
        ventana.add(panelAcciones, BorderLayout.EAST);

        // Configurar el JLabel para mostrar la imagen
        JLabel vacationModeLabel = new JLabel();
        vacationModeLabel.setPreferredSize(new Dimension(80, 40)); // Cambiar el ancho y alto 

        // Cambiar la imagen 
        ImageIcon vacationIcon = new ImageIcon("vacation-button.png"); // Ruta de la imagen de Vacation Mode
        vacationModeLabel.setIcon(vacationIcon);

        // Agregar un evento de clic a la etiqueta "Vacation Mode"
        vacationModeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vacationMode();
            }
        });

        // Crear botón "Undo" con un tamaño personalizado
        JButton undoButton = new JButton("Undo");
        undoButton.setPreferredSize(new Dimension(65, 40)); // Cambiar el ancho y alto 
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoAccion(accionHistorial);
            }
        });

        // Agregar la imagen al botón "Undo"
        ImageIcon undoIcon = new ImageIcon("undo-button.png"); // Ruta de la imagen para el botón "Undo"
        undoButton.setIcon(undoIcon);

        // Agregar los componentes al panel personalizado
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Ajusta el espacio horizontal entre los botones
        buttonPanel.add(vacationModeLabel);
        buttonPanel.add(undoButton);
        buttonPanel.setBackground(new Color(1, 20, 64)); // Cambiar el color de fondo

        ventana.add(buttonPanel, BorderLayout.SOUTH);

        ventana.setPreferredSize(new Dimension(800, 528)); // Establece un tamaño preferido para la ventana

        ventana.pack();
        ventana.setVisible(true);
    }

    // Método estático para activar el modo vacaciones
    public static void vacationMode() {
        boolean alMenosUnEncendido = false;

        for (BotonControl boton : botonesActivos) {
            if (boton.getEstado()) {
                boton.apagar();
                alMenosUnEncendido = true;
            }
        }

        // Mostrar el mensaje con JOptionPane
        if (alMenosUnEncendido) {
            // Mostrar el JOptionPane con el mensaje deseado
            JOptionPane.showMessageDialog(null, "Vacation mode has been selected, all devices have been turned off", "Vacation Mode", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Método estático para deshacer una acción
    public static void undoAccion(Stack<HistorialAccion> accionHistorial) {
        if (!accionHistorial.isEmpty()) {
            deshaciendo = true;
            HistorialAccion historialAccion = accionHistorial.pop();
            String accion = historialAccion.getEstado();
            String nombreBoton = historialAccion.getNombre();
            BotonControl boton = buscarBoton(nombreBoton);

            if (accion.equals("ON")) {
                boton.apagar();
            } else if (accion.equals("OFF")) {
                boton.encender();
            }
            deshaciendo = false;
        }
    }

    // Método estático para buscar un botón por nombre
    public static BotonControl buscarBoton(String nombreBoton) {
        for (BotonControl boton : botonesActivos) {
            if (boton.getNombre().equals(nombreBoton)) {
                return boton;
            }
        }
        return null;
    }

    // Método principal para iniciar la interfaz de usuario
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                crearInterfaz();
            }
        });
    }
}

// Definición de la clase HistorialAccion
class HistorialAccion {
    private String nombre;
    private String estado;

    // Constructor de HistorialAccion
    public HistorialAccion(String nombre, String estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    // Método para obtener el nombre
    public String getNombre() {
        return nombre;
    }

    // Método para obtener el estado
    public String getEstado() {
        return estado;
    }
}

