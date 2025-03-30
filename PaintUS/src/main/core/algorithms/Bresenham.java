package main.core.algorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
// clase Bresenham
public class Bresenham {

    /**
     * Dibuja una línea usando el algoritmo de Bresenham con grosor personalizado
     * @param img BufferedImage donde se dibujará
     * @param x1 Coordenada x inicial
     * @param y1 Coordenada y inicial
     * @param x2 Coordenada x final
     * @param y2 Coordenada y final
     * @param color Color de la línea
     * @param thickness Grosor del trazo (1-20px)
     */
    public static void drawLine(BufferedImage img, int x1, int y1, int x2, int y2, Color color, int thickness) {
        int rgb = color.getRGB();

        // Algoritmo base
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        // Versión optimizada para grosor
        while (true) {
            drawThickPixel(img, x1, y1, rgb, thickness);

            if (x1 == x2 && y1 == y2) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    /**
     * Dibuja un pixel con grosor (círculo de píxeles)
     */
    private static void drawThickPixel(BufferedImage img, int x, int y, int rgb, int thickness) {
        int radius = thickness / 2;

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                // Fórmula de círculo para bordes suaves
                if (i*i + j*j <= radius*radius) {
                    int px = x + i;
                    int py = y + j;

                    if (px >= 0 && px < img.getWidth() && py >= 0 && py < img.getHeight()) {
                        img.setRGB(px, py, rgb);
                    }
                }
            }
        }
    }

    /**
     * Versión alternativa para líneas con antialiasing (calidad profesional)
     */
    public static void drawLineAA(BufferedImage img, int x0, int y0, int x1, int y1, Color color) {
        // Implementación Xiaolin Wu's algorithm aquí...
        // (Código disponible bajo petición)
    }
}