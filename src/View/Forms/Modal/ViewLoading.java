package View.Forms.Modal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class ViewLoading {
    private JPanel rootPanel;

    private void createUIComponents() {
        // Задание полупрозрачного фона
        rootPanel = new JPanel(new GridBagLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 0.9f));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        rootPanel.setOpaque(false);
        rootPanel.setVisible(false);
        rootPanel.addMouseListener(new MouseAdapter() {});
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void showView() {
        rootPanel.setVisible(true);
    }

    public void hideView() {
        rootPanel.setVisible(false);
    }
}
