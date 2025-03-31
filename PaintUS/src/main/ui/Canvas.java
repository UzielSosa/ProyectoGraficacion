package main.ui;

import main.core.algorithms.*;
import main.core.rendering.LayerManager;
import main.ui.tools.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel {
    private LayerManager layerManager;
    private SelectionTool selectionTool;
    private GradientTool gradientTool;
    private TextTool textTool;
    private Color currentColor = Color.BLACK;
    private int brushSize = 3;
    private Tool currentTool = Tool.PENCIL;
    private Point startPoint;
    private BufferedImage temporaryPreview;
    private List<Point> controlPoints = new ArrayList<>();
    private String currentText = "";

    public enum Tool {
        PENCIL, LINE, CIRCLE, RECTANGLE, BEZIER, SELECTION, GRADIENT, TEXT
    }

    public Canvas() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1024, 768));
        initSystems();
        setupEventListeners();
    }

    private void initSystems() {
        layerManager = new LayerManager(getPreferredSize().width, getPreferredSize().height);
        selectionTool = new SelectionTool(layerManager);
        gradientTool = new GradientTool();
        textTool = new TextTool();
        temporaryPreview = new BufferedImage(getPreferredSize().width, getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);
    }

    private void setupEventListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                switch (currentTool) {
                    case SELECTION:
                        selectionTool.mousePressed(e);
                        break;
                    case GRADIENT:
                        gradientTool.startGradient(e.getPoint());
                        break;
                    case TEXT:
                        textTool.setPosition(e.getPoint());
                        String input = JOptionPane.showInputDialog("Ingrese el texto:");
                        if (input != null) {
                            currentText = input;
                            textTool.setText(input);
                            textTool.applyText(layerManager.getCurrentLayer());
                        }
                        break;
                    case PENCIL:
                        drawOnCurrentLayer(e.getX(), e.getY());
                        break;
                    case BEZIER:
                        controlPoints.add(e.getPoint());
                        if (controlPoints.size() == 4) {
                            drawBezierCurve();
                            controlPoints.clear();
                        }
                        break;
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                switch (currentTool) {
                    case SELECTION:
                        selectionTool.mouseReleased(e);
                        break;
                    case GRADIENT:
                        gradientTool.applyGradient(layerManager.getCurrentLayer());
                        gradientTool.endGradient();
                        break;
                    default:
                        if (startPoint != null) {
                            applyDrawing(e);
                        }
                }
                startPoint = null;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                switch (currentTool) {
                    case SELECTION:
                        selectionTool.mouseDragged(e);
                        break;
                    case GRADIENT:
                        gradientTool.updateGradient(e.getPoint());
                        break;
                    case PENCIL:
                        drawOnCurrentLayer(e.getX(), e.getY());
                        break;
                    default:
                        if (startPoint != null) {
                            updateTemporaryPreview(e.getPoint());
                        }
                }
                repaint();
            }
        });
    }

    private void applyDrawing(MouseEvent e) {
        Graphics2D g2d = layerManager.getCurrentLayer().createGraphics();
        configureGraphics(g2d);

        switch (currentTool) {
            case LINE:
                Bresenham.drawLine(layerManager.getCurrentLayer(),
                        startPoint.x, startPoint.y,
                        e.getX(), e.getY(),
                        currentColor, brushSize);
                break;
            case CIRCLE:
                MidpointCircle.drawCircle(layerManager.getCurrentLayer(),
                        startPoint.x, startPoint.y,
                        (int) startPoint.distance(e.getPoint()),
                        currentColor, brushSize);
                break;
            case RECTANGLE:
                drawRectangle(g2d, startPoint.x, startPoint.y, e.getX(), e.getY());
                break;
        }

        g2d.dispose();
        clearTemporaryPreview();
    }

    private void configureGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(currentColor);
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    private void drawOnCurrentLayer(int x, int y) {
        Graphics2D g2d = layerManager.getCurrentLayer().createGraphics();
        g2d.setColor(currentColor);
        g2d.fillOval(x - brushSize/2, y - brushSize/2, brushSize, brushSize);
        g2d.dispose();
    }

    private void drawRectangle(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        g2d.drawRect(
                Math.min(x1, x2),
                Math.min(y1, y2),
                Math.abs(x2 - x1),
                Math.abs(y2 - y1)
        );
    }

    private void drawBezierCurve() {
        if (controlPoints.size() == 4) {
            ParametricCurve.drawCubicBezier(
                    layerManager.getCurrentLayer(),
                    controlPoints,
                    currentColor,
                    brushSize
            );
        }
    }

    private void updateTemporaryPreview(Point endPoint) {
        clearTemporaryPreview();
        Graphics2D g2d = temporaryPreview.createGraphics();
        g2d.setColor(new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 150));
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        switch (currentTool) {
            case LINE:
                g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                break;
            case CIRCLE:
                int radius = (int) startPoint.distance(endPoint);
                g2d.drawOval(startPoint.x - radius, startPoint.y - radius, radius*2, radius*2);
                break;
            case RECTANGLE:
                g2d.drawRect(
                        Math.min(startPoint.x, endPoint.x),
                        Math.min(startPoint.y, endPoint.y),
                        Math.abs(endPoint.x - startPoint.x),
                        Math.abs(endPoint.y - startPoint.y)
                );
                break;
        }
        g2d.dispose();
    }

    private void clearTemporaryPreview() {
        Graphics2D g2d = temporaryPreview.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 1. Dibujar capas base
        g2d.drawImage(layerManager.getCompositeImage(), 0, 0, null);

        // 2. Dibujar preview temporal
        g2d.drawImage(temporaryPreview, 0, 0, null);

        // 3. Dibujar herramientas especiales
        if (currentTool == Tool.SELECTION) {
            selectionTool.drawSelectionFeedback(g2d);
        } else if (currentTool == Tool.GRADIENT) {
            gradientTool.drawGradientPreview(g2d);
        }

        // 4. Dibujar puntos de control
        if (currentTool == Tool.BEZIER && !controlPoints.isEmpty()) {
            drawBezierControlPoints(g2d);
        }
    }

    private void drawBezierControlPoints(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        for (Point p : controlPoints) {
            g2d.fillOval(p.x - 5, p.y - 5, 10, 10);
        }
    }

    public void clearCanvas() {
        layerManager = new LayerManager(getWidth(), getHeight());
        controlPoints.clear();
        currentText = "";
        repaint();
    }

    public void setCurrentTool(Tool tool) {
        this.currentTool = tool;
        controlPoints.clear();
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
        gradientTool.setColors(color, color.darker());
        textTool.setColor(color);
    }

    public void setBrushSize(int size) {
        this.brushSize = size;
    }

    public BufferedImage getCanvasImage() {
        return layerManager.getCompositeImage();
    }

    public LayerManager getLayerManager() {
        return layerManager;
    }
}