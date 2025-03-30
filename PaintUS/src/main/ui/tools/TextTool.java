package main.ui.tools;

import java.awt.*;
import java.awt.image.BufferedImage; // Import faltante
import java.awt.font.TextAttribute;
import java.text.AttributedString;
// TextTool
public class TextTool {
    private String currentText = "";
    private Point textPosition;
    private Font textFont = new Font("Arial", Font.PLAIN, 12);
    private Color textColor = Color.BLACK;

    public void setText(String text) {
        this.currentText = text;
    }

    public void setPosition(Point position) {
        this.textPosition = position;
    }

    public void applyText(BufferedImage target) {
        if (currentText.isEmpty() || textPosition == null) return;

        Graphics2D g2d = target.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(textFont);
        g2d.setColor(textColor);

        AttributedString attributedText = new AttributedString(currentText);
        attributedText.addAttribute(TextAttribute.FONT, textFont);

        g2d.drawString(attributedText.getIterator(),
                textPosition.x, textPosition.y);
        g2d.dispose();
    }

    public void drawTextPreview(Graphics2D g2d) {
        if (currentText.isEmpty() || textPosition == null) return;

        AttributedString attributedText = new AttributedString(currentText);
        attributedText.addAttribute(TextAttribute.FONT, textFont);
        attributedText.addAttribute(TextAttribute.FOREGROUND,
                new Color(textColor.getRed(),
                        textColor.getGreen(),
                        textColor.getBlue(), 150));

        g2d.drawString(attributedText.getIterator(),
                textPosition.x, textPosition.y);
    }

    public void setFont(Font font) {
        this.textFont = font;
    }

    public void setColor(Color color) {
        this.textColor = color;
    }
}