package lk.educore.schoolms.component;

import java.awt.*;
import javax.swing.*;

/**
 * Round Panel
 * Custom JPanel with rounded corners and gradient background
 * Used for dashboard cards and modern UI components
 */
public class RoundPanel extends JPanel {

    private int cornerRadius;
    private Color startColor;
    private Color endColor;
    private boolean gradientEnabled;
    private boolean borderEnabled;
    private Color borderColor;
    private int borderThickness;

    /**
     * Default constructor with white background and rounded corners
     */
    public RoundPanel() {
        this(20, Color.WHITE, Color.WHITE, false, false, Color.GRAY, 1);
    }

    /**
     * Constructor with corner radius
     * @param cornerRadius Corner radius in pixels
     */
    public RoundPanel(int cornerRadius) {
        this(cornerRadius, Color.WHITE, Color.WHITE, false, false, Color.GRAY, 1);
    }

    /**
     * Constructor with corner radius and background color
     * @param cornerRadius Corner radius in pixels
     * @param backgroundColor Background color
     */
    public RoundPanel(int cornerRadius, Color backgroundColor) {
        this(cornerRadius, backgroundColor, backgroundColor, false, false, Color.GRAY, 1);
    }

    /**
     * Constructor with gradient background
     * @param cornerRadius Corner radius in pixels
     * @param startColor Gradient start color
     * @param endColor Gradient end color
     */
    public RoundPanel(int cornerRadius, Color startColor, Color endColor) {
        this(cornerRadius, startColor, endColor, true, false, Color.GRAY, 1);
    }

    /**
     * Full constructor with all options
     * @param cornerRadius Corner radius in pixels
     * @param startColor Gradient start color (or solid background if gradient disabled)
     * @param endColor Gradient end color
     * @param gradientEnabled Enable gradient background
     * @param borderEnabled Enable border
     * @param borderColor Border color
     * @param borderThickness Border thickness in pixels
     */
    public RoundPanel(int cornerRadius, Color startColor, Color endColor, 
                     boolean gradientEnabled, boolean borderEnabled, 
                     Color borderColor, int borderThickness) {
        this.cornerRadius = cornerRadius;
        this.startColor = startColor;
        this.endColor = endColor;
        this.gradientEnabled = gradientEnabled;
        this.borderEnabled = borderEnabled;
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
        
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Create rounded rectangle
        int width = getWidth();
        int height = getHeight();
        
        // Paint background
        if (gradientEnabled) {
            // Gradient background
            GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, height, endColor);
            g2d.setPaint(gradient);
        } else {
            // Solid background
            g2d.setColor(startColor);
        }
        
        g2d.fillRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
        
        // Paint border if enabled
        if (borderEnabled) {
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(borderThickness));
            g2d.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
        }
        
        g2d.dispose();
        
        super.paintComponent(g);
    }

    /**
     * Set corner radius
     * @param cornerRadius Corner radius in pixels
     */
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

    /**
     * Get corner radius
     * @return Corner radius in pixels
     */
    public int getCornerRadius() {
        return cornerRadius;
    }

    /**
     * Set gradient colors
     * @param startColor Gradient start color
     * @param endColor Gradient end color
     */
    public void setGradientColors(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.gradientEnabled = true;
        repaint();
    }

    /**
     * Set solid background color
     * @param color Background color
     */
    public void setBackgroundColor(Color color) {
        this.startColor = color;
        this.endColor = color;
        this.gradientEnabled = false;
        repaint();
    }

    /**
     * Enable gradient background
     * @param enabled Enable gradient
     */
    public void setGradientEnabled(boolean enabled) {
        this.gradientEnabled = enabled;
        repaint();
    }

    /**
     * Enable border
     * @param enabled Enable border
     */
    public void setBorderEnabled(boolean enabled) {
        this.borderEnabled = enabled;
        repaint();
    }

    /**
     * Set border color
     * @param color Border color
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        this.borderEnabled = true;
        repaint();
    }

    /**
     * Set border thickness
     * @param thickness Border thickness in pixels
     */
    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
        repaint();
    }

    /**
     * Create a blue gradient panel for dashboard cards
     * @return RoundPanel with blue gradient
     */
    public static RoundPanel createBlueGradientPanel() {
        return new RoundPanel(20, new Color(33, 150, 243), new Color(33, 150, 243), true, false, Color.GRAY, 1);
    }

    /**
     * Create a green gradient panel for dashboard cards
     * @return RoundPanel with green gradient
     */
    public static RoundPanel createGreenGradientPanel() {
        return new RoundPanel(20, new Color(76, 175, 80), new Color(76, 175, 80), true, false, Color.GRAY, 1);
    }

    /**
     * Create a red gradient panel for dashboard cards
     * @return RoundPanel with red gradient
     */
    public static RoundPanel createRedGradientPanel() {
        return new RoundPanel(20, new Color(244, 67, 54), new Color(244, 67, 54), true, false, Color.GRAY, 1);
    }

    /**
     * Create a yellow gradient panel for dashboard cards
     * @return RoundPanel with yellow gradient
     */
    public static RoundPanel createYellowGradientPanel() {
        return new RoundPanel(20, new Color(255, 193, 7), new Color(255, 193, 7), true, false, Color.GRAY, 1);
    }

    /**
     * Create a purple gradient panel for dashboard cards
     * @return RoundPanel with purple gradient
     */
    public static RoundPanel createPurpleGradientPanel() {
        return new RoundPanel(20, new Color(156, 39, 176), new Color(156, 39, 176), true, false, Color.GRAY, 1);
    }

    /**
     * Create a white panel with shadow effect
     * @return RoundPanel with white background and border
     */
    public static RoundPanel createWhitePanel() {
        return new RoundPanel(15, Color.WHITE, Color.WHITE, false, true, new Color(200, 200, 200), 1);
    }

    /**
     * Create a light gray panel
     * @return RoundPanel with light gray background
     */
    public static RoundPanel createGrayPanel() {
        return new RoundPanel(15, new Color(245, 245, 245), new Color(245, 245, 245), false, true, new Color(200, 200, 200), 1);
    }

    /**
     * Create a custom gradient panel
     * @param startColor Gradient start color
     * @param endColor Gradient end color
     * @return RoundPanel with custom gradient
     */
    public static RoundPanel createCustomGradientPanel(Color startColor, Color endColor) {
        return new RoundPanel(20, startColor, endColor, true, false, Color.GRAY, 1);
    }
}