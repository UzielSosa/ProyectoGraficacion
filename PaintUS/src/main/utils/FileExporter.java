package main.utils;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileExporter {
    private static final String PNG_EXT = ".png";
    private static final String JPG_EXT = ".jpg";

    public static void exportToPNG(BufferedImage image) {
        if (image == null) {
            showError("La imagen no puede ser nula");
            return;
        }

        JFileChooser fileChooser = createFileChooser("Guardar PNG", PNG_EXT);
        fileChooser.setFileFilter(createFileFilter(PNG_EXT, "Archivos PNG (*.png)"));

        processExport(fileChooser, image, "PNG");
    }

    public static void exportToJPG(BufferedImage image, float quality) {
        if (image == null) {
            showError("La imagen no puede ser nula");
            return;
        }

        JFileChooser fileChooser = createFileChooser("Guardar JPG", JPG_EXT);
        fileChooser.setFileFilter(createFileFilter(JPG_EXT, "Archivos JPG (*.jpg)"));

        processExport(fileChooser, image, "JPEG");
    }

    private static JFileChooser createFileChooser(String title, String extension) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        String defaultName = "dibujo_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + extension;
        chooser.setSelectedFile(new File(defaultName));
        return chooser;
    }

    private static javax.swing.filechooser.FileFilter createFileFilter(String extension, String description) {
        return new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(extension) || f.isDirectory();
            }
            public String getDescription() {
                return description;
            }
        };
    }

    private static void processExport(JFileChooser chooser, BufferedImage image, String format) {
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = ensureFileExtension(chooser.getSelectedFile(), "." + format.toLowerCase());
            try {
                if (ImageIO.write(image, format, file)) {
                    showSuccess("Imagen guardada en: " + file.getAbsolutePath());
                } else {
                    showError("Formato no soportado: " + format);
                }
            } catch (IOException e) {
                showError("Error al guardar: " + e.getMessage());
            }
        }
    }

    private static File ensureFileExtension(File file, String extension) {
        String path = file.getAbsolutePath();
        if (!path.toLowerCase().endsWith(extension)) {
            return new File(path + extension);
        }
        return file;
    }

    private static void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}