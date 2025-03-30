package main.core.rendering;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
// clase LayerManager
public class LayerManager {
    private final ArrayList<BufferedImage> layers;
    private final ArrayList<Boolean> visibleLayers;
    private int currentLayerIndex;
    private final int width;
    private final int height;

    public LayerManager(int width, int height) {
        this.width = width;
        this.height = height;
        this.layers = new ArrayList<>();
        this.visibleLayers = new ArrayList<>();
        addNewLayer(); // Capa inicial
    }

    /**
     * Crea una nueva capa transparente
     */
    public void addNewLayer() {
        BufferedImage newLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newLayer.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        layers.add(newLayer);
        visibleLayers.add(true);
        currentLayerIndex = layers.size() - 1;
    }

    /**
     * Obtiene la imagen compuesta de todas las capas visibles
     */
    public BufferedImage getCompositeImage() {
        BufferedImage composite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = composite.createGraphics();

        for (int i = 0; i < layers.size(); i++) {
            if (visibleLayers.get(i)) {
                g2d.drawImage(layers.get(i), 0, 0, null);
            }
        }
        g2d.dispose();
        return composite;
    }

    /**
     * Obtiene la capa actualmente seleccionada
     */
    public BufferedImage getCurrentLayer() {
        return layers.get(currentLayerIndex);
    }

    public void setLayerVisible(int index, boolean visible) {
        if (index >= 0 && index < visibleLayers.size()) {
            visibleLayers.set(index, visible);
        }
    }

    public void removeCurrentLayer() {
        if (layers.size() > 1) {
            layers.remove(currentLayerIndex);
            visibleLayers.remove(currentLayerIndex);
            if (currentLayerIndex >= layers.size()) {
                currentLayerIndex = layers.size() - 1;
            }
        }
    }

    public void mergeLayers(int fromIndex, int toIndex) {
        // Implementación disponible bajo petición
    }

    public int getLayerCount() {
        return layers.size();
    }

    public void setCurrentLayer(int index) {
        if (index >= 0 && index < layers.size()) {
            currentLayerIndex = index;
        }
    }
}