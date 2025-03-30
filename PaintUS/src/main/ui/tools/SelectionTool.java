package main.ui.tools;

import main.core.rendering.LayerManager;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
// SelectionTool
public class SelectionTool {
    public enum SelectionType { RECTANGULAR, ELLIPTICAL, FREE_FORM }

    private final LayerManager layerManager;
    private SelectionType selectionType = SelectionType.RECTANGULAR;
    private Rectangle selectionBounds;
    private Point startPoint;
    private BufferedImage selectionContent;
    private boolean isDraggingSelection;

    public SelectionTool(LayerManager layerManager) {
        this.layerManager = layerManager;
    }

    public void mousePressed(MouseEvent e) {
        startPoint = e.getPoint();
        selectionBounds = new Rectangle(startPoint.x, startPoint.y, 0, 0);
        isDraggingSelection = true;
        captureSelectionContent();
    }

    public void mouseDragged(MouseEvent e) {
        if (!isDraggingSelection) return;
        updateSelectionBounds(e.getPoint());
        captureSelectionContent();
    }

    public void mouseReleased(MouseEvent e) {
        if (!isDraggingSelection) return;
        updateSelectionBounds(e.getPoint());
        isDraggingSelection = false;
    }

    private void updateSelectionBounds(Point currentPoint) {
        int x = Math.min(startPoint.x, currentPoint.x);
        int y = Math.min(startPoint.y, currentPoint.y);
        int width = Math.abs(currentPoint.x - startPoint.x);
        int height = Math.abs(currentPoint.y - startPoint.y);

        selectionBounds = new Rectangle(x, y, width, height);
    }

    private void captureSelectionContent() {
        if (selectionBounds.width <= 0 || selectionBounds.height <= 0) {
            selectionContent = null;
            return;
        }

        try {
            selectionContent = layerManager.getCompositeImage().getSubimage(
                    selectionBounds.x,
                    selectionBounds.y,
                    selectionBounds.width,
                    selectionBounds.height
            );
        } catch (Exception e) {
            selectionContent = null;
        }
    }

    public void drawSelectionFeedback(Graphics2D g2d) {
        if (selectionBounds == null || selectionBounds.isEmpty()) return;

        if (selectionContent != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.drawImage(selectionContent, selectionBounds.x, selectionBounds.y, null);
            g2d.setComposite(AlphaComposite.SrcOver);
        }

        g2d.setColor(new Color(0, 120, 215));
        float[] dashPattern = {3, 3};
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, dashPattern, 0));

        switch (selectionType) {
            case RECTANGULAR:
                g2d.drawRect(selectionBounds.x, selectionBounds.y,
                        selectionBounds.width, selectionBounds.height);
                break;
            case ELLIPTICAL:
                g2d.drawOval(selectionBounds.x, selectionBounds.y,
                        selectionBounds.width, selectionBounds.height);
                break;
        }

        drawResizeHandles(g2d);
    }

    private void drawResizeHandles(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(selectionBounds.x + selectionBounds.width - 4,
                selectionBounds.y + selectionBounds.height - 4, 8, 8);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(selectionBounds.x + selectionBounds.width - 4,
                selectionBounds.y + selectionBounds.height - 4, 8, 8);
    }

    public void moveSelection(int dx, int dy) {
        if (selectionBounds != null) {
            selectionBounds.x += dx;
            selectionBounds.y += dy;
            startPoint.x += dx;
            startPoint.y += dy;
        }
    }

    public BufferedImage getSelectedContent() {
        return selectionContent;
    }

    public Rectangle getSelectionBounds() {
        return selectionBounds;
    }

    public void setSelectionType(SelectionType type) {
        this.selectionType = type;
    }

    public void clearSelection() {
        selectionBounds = null;
        selectionContent = null;
        isDraggingSelection = false;
    }
}