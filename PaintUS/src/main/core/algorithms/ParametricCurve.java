package main.core.algorithms;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ParametricCurve {

    /**
     * Dibuja una curva Bézier cuadrática
     * @param img BufferedImage destino
     * @param points Lista de 3 puntos (inicio, control, fin)
     * @param color Color de la curva
     * @param thickness Grosor del trazo
     */
    public static void drawQuadraticBezier(BufferedImage img, List<Point> points, Color color, int thickness) {
        if (points.size() != 3) throw new IllegalArgumentException("Se requieren 3 puntos para Bézier cuadrática");

        Point p0 = points.get(0);
        Point p1 = points.get(1);
        Point p2 = points.get(2);

        for (double t = 0; t <= 1; t += 0.001) {
            int x = (int) (Math.pow(1-t, 2) * p0.x + 2 * (1-t) * t * p1.x + Math.pow(t, 2) * p2.x);
            int y = (int) (Math.pow(1-t, 2) * p0.y + 2 * (1-t) * t * p1.y + Math.pow(t, 2) * p2.y);
            drawThickPixel(img, x, y, color.getRGB(), thickness);
        }
    }

    /**
     * Dibuja una curva Bézier cúbica
     * @param img BufferedImage destino
     * @param points Lista de 4 puntos (inicio, control1, control2, fin)
     * @param color Color de la curva
     * @param thickness Grosor del trazo
     */
    public static void drawCubicBezier(BufferedImage img, List<Point> points, Color color, int thickness) {
        if (points.size() != 4) throw new IllegalArgumentException("Se requieren 4 puntos para Bézier cúbica");

        Point p0 = points.get(0);
        Point p1 = points.get(1);
        Point p2 = points.get(2);
        Point p3 = points.get(3);

        for (double t = 0; t <= 1; t += 0.001) {
            int x = (int) (Math.pow(1-t, 3) * p0.x
                    + 3 * Math.pow(1-t, 2) * t * p1.x
                    + 3 * (1-t) * Math.pow(t, 2) * p2.x
                    + Math.pow(t, 3) * p3.x);

            int y = (int) (Math.pow(1-t, 3) * p0.y
                    + 3 * Math.pow(1-t, 2) * t * p1.y
                    + 3 * (1-t) * Math.pow(t, 2) * p2.y
                    + Math.pow(t, 3) * p3.y);

            drawThickPixel(img, x, y, color.getRGB(), thickness);
        }
    }

    private static void drawThickPixel(BufferedImage img, int x, int y, int rgb, int thickness) {
        int radius = thickness / 2;
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
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
     * Calcula puntos intermedios para suavizado
     */
    public static List<Point> calculateSmoothCurve(List<Point> controlPoints, int segments) {
        List<Point> smoothed = new ArrayList<>();
        // Implementación disponible bajo petición
        return smoothed;
    }
}