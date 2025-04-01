package main;

import main.ui.MainWindow;
import javax.swing.*;

public class app {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Estilo moderno
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}