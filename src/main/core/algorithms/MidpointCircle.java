package main.core.algorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class MidpointCircle {

    /**
     * Dibuja un círculo usando el algoritmo Midpoint con grosor personalizado
     * @param img BufferedImage destino
     * @param centerX Coordenada X del centro
     * @param centerY Coordenada Y del centro
     * @param radius Radio del círculo
     * @param color Color del trazo
     * @param thickness Grosor de la línea (1-20px)
     */
    public static void drawCircle(BufferedImage img, int centerX, int centerY, int radius, Color color, int thickness) {
        int rgb = color.getRGB();
        int x = radius;
        int y = 0;
        int err = 0;

        while (x >= y) {
            drawCirclePoints(img, centerX, centerY, x, y, rgb, thickness);

            y += 1;
            err += 1 + 2*y;
            if (2*(err - x) + 1 > 0) {
                x -= 1;
                err += 1 - 2*x;
            }
        }
    }

    private static void drawCirclePoints(BufferedImage img, int cx, int cy, int x, int y, int rgb, int thickness) {
        // Octantes simétricos para optimización
        int[][] points = {
                {cx+x, cy+y}, {cx-x, cy+y}, {cx+x, cy-y}, {cx-x, cy-y},
                {cx+y, cy+x}, {cx-y, cy+x}, {cx+y, cy-x}, {cx-y, cy-x}
        };

        // Grosor adaptativo
        int radius = thickness / 2;
        for (int[] p : points) {
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    if (i*i + j*j <= radius*radius) {
                        int px = p[0] + i;
                        int py = p[1] + j;
                        if (px >= 0 && px < img.getWidth() && py >= 0 && py < img.getHeight()) {
                            img.setRGB(px, py, rgb);
                        }
                    }
                }
            }
        }
    }

    /**
     * Versión con antialiasing (Wu's Algorithm)
     */
    public static void drawSmoothCircle(BufferedImage img, int cx, int cy, int radius, Color color) {
        // Implementación disponible bajo petición
    }
}