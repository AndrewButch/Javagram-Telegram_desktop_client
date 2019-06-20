package View.Forms;

import View.Resources;
import View.WindowComponents.ComponentMover;
import View.WindowComponents.ComponentResizer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;


public class Decoration extends JPanel {
    private JPanel rootPanel;
    private JButton closeButton;
    private JPanel titlePanel;
    private JPanel contentPanel;
    private JButton minimizeButton;

    private ComponentMover componentMover;
    private ComponentResizer componentResizer;

    private BufferedImage background;

    public Decoration (JFrame frame){

        setContentPanel(frame.getContentPane());
        frame.setContentPane(this);

        frame.setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        minimizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setExtendedState(Frame.ICONIFIED);
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        componentMover = new ComponentMover(frame, titlePanel);
        componentResizer = new ComponentResizer(frame);

        background = Resources.getImage(Resources.BACKGROUND);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
        contentPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, null);
            }
        };
    }

    public void setContentPanel(Component component) {
        contentPanel.removeAll();
        contentPanel.add(component);

        component.requestFocus();

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public Component getContentPanel() {
        return contentPanel.getComponent(0);
    }

}
