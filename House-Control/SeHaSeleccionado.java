import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

class SeHaSeleccionado extends JPanel implements SujetoObservable {
    private List<Observador> observadores = new ArrayList<>();
    private JPanel etiquetaPanel; // Nuevo panel para la etiqueta
    private JLabel etiquetaSeleccion;
    private JPanel panelLabels;
    private JLabel imagenLabel; // Label para la imagen
    private ImageIcon imagen; // ImageIcon para la imagen

    // Constructor de SeHaSeleccionado
    public SeHaSeleccionado() {
        setLayout(new BorderLayout());

        panelLabels = new JPanel();
        panelLabels.setLayout(new BoxLayout(panelLabels, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelLabels);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        // Crear un nuevo panel para la etiqueta
        etiquetaPanel = new JPanel();
        etiquetaPanel.setPreferredSize(new Dimension(500, getHeight())); // Establece una altura fija

        // Cambia el fondo del panel a negro
        etiquetaPanel.setBackground(new Color(0, 10, 33));
        etiquetaPanel.setLayout(new BoxLayout(etiquetaPanel, BoxLayout.Y_AXIS)); // Utiliza BoxLayout para centrar verticalmente
        etiquetaSeleccion = new JLabel(" ");
        etiquetaSeleccion.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra horizontalmente
        etiquetaSeleccion.setAlignmentY(Component.CENTER_ALIGNMENT); // Centra verticalmente

        // Cambiar el tamaño y el tipo de letra de la etiqueta
        Font nuevaFuente = new Font("Arial", Font.BOLD, 26);
        etiquetaSeleccion.setForeground(Color.WHITE);

        etiquetaSeleccion.setFont(nuevaFuente);

        etiquetaPanel.add(Box.createVerticalGlue()); 
        etiquetaPanel.add(etiquetaSeleccion);
        etiquetaPanel.add(Box.createVerticalGlue()); 

        // Inicializar la imagen 
        imagen = new ImageIcon("house.png"); // Cambiar la ruta a la imagen por defecto
        // Escalar la imagen a un tamaño deseado
        int imagenWidth = 500; // Cambia el ancho de la imagen
        int imagenHeight = 500; // Cambia la altura de la imagen
        imagen = scaleImageIcon(imagen, imagenWidth, imagenHeight);
        imagenLabel = new JLabel(imagen);
        etiquetaPanel.add(imagenLabel);
        etiquetaPanel.add(Box.createVerticalGlue()); // Espacio vertical flexible después de la imagen

        // Centrar la imagen horizontalmente en el JLabel
        imagenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(etiquetaPanel, BorderLayout.EAST);
    }

    @Override
    public void registrarObservador(Observador observador) {
        observadores.add(observador);
    }

    @Override
    public void notificarObservadores(String accion) {
        for (Observador observador : observadores) {
            observador.actualizar(accion);
        }
    }

    // Método para mostrar una acción en el panel de acciones
    public void mostrarAccion(String accion) {
        JLabel labelAccion = new JLabel(accion);
        panelLabels.add(labelAccion);
        revalidate();
    }

    // Método para mostrar un texto en la etiqueta
    public void mostrarEtiqueta(String texto) {
        etiquetaSeleccion.setText(texto);
    }

    // Método para cambiar la imagen según el estado
    public void mostrarImagen(ImageIcon nuevaImagen) {
        imagenLabel.setIcon(nuevaImagen);
        // Centrar la imagen horizontalmente en el JLabel
        imagenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

     // Método para escalar una ImageIcon al tamaño deseado
    private ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
