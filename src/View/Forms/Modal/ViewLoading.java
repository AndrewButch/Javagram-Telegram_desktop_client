package View.Forms.Modal;

import View.Interface.IViewLoading;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class ViewLoading implements IViewLoading {
    private JPanel rootPanel;
    private JLabel title;

    private void createUIComponents() {
        // Задание полупрозрачного фона
        rootPanel = new JPanel(new GridBagLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 0.5f));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        rootPanel.setOpaque(false);
        rootPanel.setVisible(false);
        rootPanel.addMouseListener(new MouseAdapter() {});
    }

    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }

    @Override
    public void showView() {
        rootPanel.setVisible(true);
    }

    @Override
    public void hideView() {
        rootPanel.setVisible(false);
    }
}
