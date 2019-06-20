package View;

import javax.swing.*;
import java.awt.*;

public class LoadingForm {
    private JPanel rootPanel;

    private void createUIComponents() {
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
        rootPanel.setSize(900, 600);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
