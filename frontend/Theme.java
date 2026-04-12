package frontend;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class Theme {
    public static final Color BACKGROUND = new Color(243, 244, 246);
    public static final Color CARD_BG = new Color(255, 255, 255);
    public static final Color PRIMARY_COLOR = new Color(37, 99, 235);
    public static final Color SECONDARY_COLOR = new Color(16, 185, 129);
    public static final Color TEXT_DARK = new Color(31, 41, 55);
    public static final Color TEXT_LIGHT = new Color(107, 114, 128);

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public static JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Enable anti-aliasing for smooth rounded corners
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw the custom background
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12); // 12px border radius
                
                // Let Swing draw the text on top
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE); // Make text white
        button.setFocusPainted(false); // Remove focus outline
        button.setBorderPainted(false); // Remove default OS borders
        button.setContentAreaFilled(false); // Disable default OS background styling
        button.setOpaque(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        return button;
    }
}
