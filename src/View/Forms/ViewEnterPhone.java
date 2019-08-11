package View.Forms;

import Presenter.Interface.IPresenterPhoneInput;
import Presenter.PrPhoneInput;
import View.Interface.IViewPhoneInput;
import View.Resources;
import View.WindowManager;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.ParseException;



public class ViewEnterPhone implements IViewPhoneInput {
    private JPanel rootPanel;
    private JButton nextButton;
    private JFormattedTextField phoneJFormattedText;
    private JLabel errorText;
    private JTextPane titleTextPane;
    private JPanel logoPanel;
    private BufferedImage background;
    private BufferedImage logo;
    private BufferedImage icon_phone;
    private IPresenterPhoneInput presenter;


    public ViewEnterPhone() {
        setPresenter(new PrPhoneInput(this));
        logo = Resources.getImage(Resources.LOGO);
        icon_phone = Resources.getImage(Resources.ICON_PHONE);
        WindowManager.setContentView(this);
        phoneJFormattedText.requestFocus();

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.authorizationPhone((String)phoneJFormattedText.getValue());
            }
        });
        // слушатель на поле ввода телефона
        phoneJFormattedText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.authorizationPhone((String)phoneJFormattedText.getValue());
            }
        });
    }

    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }

    @Override
    public void setPresenter(IPresenterPhoneInput presenter) {
        this.presenter = presenter;
    }

    public void clearPhoneField() {
        phoneJFormattedText.setText("");
    }

    public JLabel getErrorText() {
        return errorText;
    }

    public void goToSMSView() {
        new ViewSMSCode();
    }

    private void createUIComponents() {
        try {
            phoneJFormattedText = new JFormattedTextField(new MaskFormatter("+7 ### ### ## ##")) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(icon_phone, 20, 7, null);
                }
            };
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Border outerBorder, innerBorder;
        outerBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);
        innerBorder = BorderFactory.createEmptyBorder(2, 50, 2, 0);
        phoneJFormattedText.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

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
