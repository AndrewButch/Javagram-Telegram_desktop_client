package View;

import View.Forms.Decoration;

import javax.swing.*;
import java.awt.*;


public class WindowManager{

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 900;
    private static JFrame frame = new JFrame();
    private static Decoration decoration = new Decoration(frame);

    public static int getScreenWidth() {
        return decoration.getContentPanel().getWidth();
    }

    public static int getScreenHeight() {
        return decoration.getContentPanel().getHeight();
    }

    public static void startFrame(){
        frame.setVisible(false);
        frame.setBackground(Color.RED);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initFrame();
            }
        });
    }
    private static void initFrame() {
        frame.setMinimumSize(new Dimension(500, 300));
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null); //выравниваем окно по центру экрана
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void setContentView(IView view) {
        decoration.setContentPanel(view.getRootPanel());
        frame.revalidate();
        frame.repaint();
    }

    public static void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(
                decoration.getRootPane(),
                message,
                "Ошибка!",
                JOptionPane.ERROR_MESSAGE);
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static Component getContentPanel() {
        return decoration.getContentPanel();
    }
}
