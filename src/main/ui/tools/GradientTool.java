package main.ui.tools;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class GradientTool {
    public enum GradientType { LINEAR, RADIAL }

    private GradientType gradientType = GradientType.LINEAR;
    private Color startColor = Color.RED;
    private Color endColor = Color.BLUE;
    private Point2D startPoint;
    private Point2D endPoint;
    private boolean isDragging;

    public void setGradientType(GradientType type) {
        this.gradientType = type;
    }

    public void setColors(Color start, Color end) {
        this.startColor = start;
        this.endColor = end;
    }

    public void startGradient(Point2D start) {
        this.startPoint = start;
        this.endPoint = start;
        this.isDragging = true;
    }

    public void updateGradient(Point2D end) {
        if (isDragging) {
            this.endPoint = end;
        }
    }

    public void applyGradient(BufferedImage target) {
        if (startPoint == null || endPoint == null) return;

        Graphics2D g2d = target.createGraphics();
        Paint gradient;

        if (gradientType == GradientType.LINEAR) {
            gradient = new LinearGradientPaint(
                    (float)startPoint.getX(), (float)startPoint.getY(),
                    (float)endPoint.getX(), (float)endPoint.getY(),
                    new float[]{0.0f, 1.0f},
                    new Color[]{startColor, endColor}
            );
        } else {
            float radius = (float)startPoint.distance(endPoint);
            gradient = new RadialGradientPaint(
                    (float)startPoint.getX(), (float)startPoint.getY(),
                    radius,
                    new float[]{0.0f, 1.0f},
                    new Color[]{startColor, endColor}
            );
        }

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, target.getWidth(), target.getHeight());
        g2d.dispose();
    }

    public void drawGradientPreview(Graphics2D g2d) {
        if (!isDragging || startPoint == null || endPoint == null) return;

        Paint originalPaint = g2d.getPaint();

        if (gradientType == GradientType.LINEAR) {
            g2d.setPaint(new LinearGradientPaint(
                    (float)startPoint.getX(), (float)startPoint.getY(),
                    (float)endPoint.getX(), (float)endPoint.getY(),
                    new float[]{0.0f, 1.0f},
                    new Color[]{startColor, endColor}
            ));
        } else {
            float radius = (float)startPoint.distance(endPoint);
            g2d.setPaint(new RadialGradientPaint(
                    (float)startPoint.getX(), (float)startPoint.getY(),
                    radius,
                    new float[]{0.0f, 1.0f},
                    new Color[]{startColor, endColor}
            ));
        }

        g2d.fillRect(0, 0, g2d.getClipBounds().width, g2d.getClipBounds().height);
        g2d.setPaint(originalPaint);
    }

    public void endGradient() {
        this.isDragging = false;
    }
}