package main.ui;

import javax.swing.*;
import java.awt.*;
//Tolbar
public class Toolbar extends JPanel {
    public Toolbar(Canvas canvas) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Botones principales
        add(createToolButton("Lápiz", Canvas.Tool.PENCIL, canvas));
        add(createToolButton("Línea", Canvas.Tool.LINE, canvas));
        add(createToolButton("Círculo", Canvas.Tool.CIRCLE, canvas));
        add(createToolButton("Rectángulo", Canvas.Tool.RECTANGLE, canvas));

        // Separador
        add(Box.createHorizontalStrut(20));

        // Herramientas avanzadas
        add(createToolButton("Selección", Canvas.Tool.SELECTION, canvas));
        add(createToolButton("Degradado", Canvas.Tool.GRADIENT, canvas));
        add(createToolButton("Texto", Canvas.Tool.TEXT, canvas));
    }

    private JButton createToolButton(String text, Canvas.Tool tool, Canvas canvas) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 30));
        button.addActionListener(e -> canvas.setCurrentTool(tool));
        return button;
    }
}