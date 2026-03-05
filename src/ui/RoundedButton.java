package ui;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    public RoundedButton(String text) {
        super(text);

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);

        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setRolloverEnabled(true);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        ButtonModel model = getModel();

        // Sky Blue Colors
        Color normal = new Color(135, 206, 235);   // sky blue
        Color hover = new Color(176, 226, 255);    // lighter sky blue
        Color pressed = new Color(100, 180, 220);  // darker sky blue

        if (model.isPressed()) {
            g2.setColor(pressed);
        }
        else if (model.isRollover()) {
            g2.setColor(hover);
        }
        else {
            g2.setColor(normal);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        super.paintComponent(g);
        g2.dispose();
    }
}