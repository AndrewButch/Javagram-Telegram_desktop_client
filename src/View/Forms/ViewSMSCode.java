package View.Forms;

import View.Resources;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ViewSMSCode {
    private JPasswordField codePasswordField;
    private JButton nextButton;
    private JLabel phoneNumberLabel;
    private JPanel rootPanel;
    private JTextPane titleTextPane;
    private JPanel logoPanel;
    private BufferedImage logo;
    private BufferedImage lock_phone;


    public ViewSMSCode() {
        logo = Resources.getImage(Resources.LOGO_MINI);
        lock_phone = Resources.getImage(Resources.ICON_LOCK);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JLabel getPhoneNumberLabel() {
        return phoneNumberLabel;
    }

    public JPasswordField getCodePasswordField() {
        return codePasswordField;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        logoPanel = new JPanel();
        codePasswordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(lock_phone, 10, 7, null);
            }
        };

        Border outerBorder, innerBorder;
        outerBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);
        innerBorder = BorderFactory.createEmptyBorder(2, 50, 2, 0);
        codePasswordField.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(logo, 0, 0, null);
            }
        };

        StyleContext sc = new StyleContext();
        DefaultStyledDocument doc = new DefaultStyledDocument(sc);
        titleTextPane = new JTextPane(doc);
        Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
        Style mainStyle = sc.addStyle("My Style", defaultStyle);
        StyleConstants.setAlignment(mainStyle,StyleConstants.ALIGN_CENTER);
        doc.setLogicalStyle(0, mainStyle);
    }
}
