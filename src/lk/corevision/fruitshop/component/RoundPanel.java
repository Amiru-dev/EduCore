package lk.corevision.fruitshop.component;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.GradientPaint;

/**
 * Custom JPanel with rounded corners. Provides a modern UI component with
 * configurable border radius.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class RoundPanel extends JPanel {

    private Color startColor = Color.WHITE; // Default start color
    private Color endColor = Color.WHITE;  // Default end color

    public RoundPanel() {
        init();
    }

    private void init() {
        this.putClientProperty(FlatClientProperties.STYLE, "arc:25");
        this.setOpaque(false); // Allow custom painting
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create gradient paint with dynamic colors
        GradientPaint gradient = new GradientPaint(
                0, 0, startColor,
                getWidth(), getHeight(), endColor
        );

        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
    }

    // Single method to set both start and end colors
    public void setGradientColors(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        repaint(); // Repaint to reflect the color change
    }

    // Optional getters (if needed elsewhere)
    public Color getStartColor() {
        return startColor;
    }

    public Color getEndColor() {
        return endColor;
    }
}
