package main.ui;

import main.ui.Canvas;
import main.ui.Toolbar;
import main.utils.FileExporter;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
///Inicio del MainWindow
public class MainWindow extends JFrame {
    private final Canvas canvas;

    public MainWindow() {
        setTitle("ProyectoGraficacion-Uziel Sosa");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Configuración principal
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(1280, 800));

        // 1. Panel Superior (Barra de menú)
        setJMenuBar(createMenuBar());

        // 2. Panel Central (Lienzo con scroll)
        canvas = new Canvas();
        JScrollPane canvasScroll = new JScrollPane(canvas);
        canvasScroll.setPreferredSize(new Dimension(900, 600));
        add(canvasScroll, BorderLayout.CENTER);

        // 3. Panel Inferior (Toolbar rediseñada)
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Toolbar principal inferior
        Toolbar mainToolbar = new Toolbar(canvas);
        bottomPanel.add(mainToolbar, BorderLayout.CENTER);

        // Mini barra de estado
        JLabel statusBar = new JLabel("  Listo  |  Capa: 1  |  Color: #000000  ");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        bottomPanel.add(statusBar, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // 4. Panel Derecho (Herramientas adicionales)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Selector de color
        JButton colorBtn = new JButton("Seleccionar Color");
        colorBtn.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Elige un color", Color.BLACK);
            if (newColor != null) {
                canvas.setCurrentColor(newColor);
                statusBar.setText("  Color: " + String.format("#%06X", (0xFFFFFF & newColor.getRGB())));
            }
        });

        rightPanel.add(createToolSection("Color", colorBtn));
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(createToolSection("Capas", new JButton("Gestor de Capas")));
        rightPanel.add(Box.createVerticalGlue());

        add(rightPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menú Archivo
        JMenu fileMenu = new JMenu("Archivo");
        fileMenu.add(createMenuItem("Nuevo", e -> canvas.clearCanvas()));
        fileMenu.add(createMenuItem("Abrir...", e -> openFile()));
        fileMenu.add(createMenuItem("Guardar", e -> saveImage()));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Salir", e -> System.exit(0)));

        // Menú Edición
        JMenu editMenu = new JMenu("Edición");
        editMenu.add(createMenuItem("Deshacer", e -> {}));
        editMenu.add(createMenuItem("Rehacer", e -> {}));

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        return menuBar;
    }

    private JPanel createToolSection(String title, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));

        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        component.setMaximumSize(new Dimension(150, 30));

        panel.add(Box.createVerticalStrut(5));
        panel.add(component);
        panel.add(Box.createVerticalStrut(5));

        return panel;
    }

    private void saveImage() {
        BufferedImage image = canvas.getCanvasImage();
        if (image != null) {
            FileExporter.exportToPNG(image);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No hay contenido para guardar",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openFile() {
        JOptionPane.showMessageDialog(this,
                "Funcionalidad en desarrollo",
                "Abrir Archivo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JMenuItem createMenuItem(String text, java.awt.event.ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(action);
        return item;
    }
}