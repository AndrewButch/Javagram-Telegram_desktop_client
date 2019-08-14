package View.Forms;

import Presenter.Interface.IPresenterSignUp;
import Presenter.PrSignUp;
import View.Interface.IViewSignUp;
import View.Resources;
import View.WindowManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class ViewSignUp implements IViewSignUp {
    private IPresenterSignUp presenter;
    private JTextField lastNameText;
    private JTextField firstNameText;
    private JButton nextButton;
    private JPanel rootPanel;
    private JTextPane titleTextPane;
    private JPanel logoPanel;
    private BufferedImage logo;

    public ViewSignUp() {
        this.logo = Resources.getImage(Resources.LOGO_MINI);
        setPresenter(new PrSignUp(this));
        setListeners();
        WindowManager.setContentView(this);
        firstNameText.requestFocus();
    }

    private void setListeners() {
        // слушатель на кнопку "Продолжить"
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.signup();
            }
        });
        // слушатель на поле ввода фамилии
        lastNameText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.signup();
            }
        });
    }

    private void createUIComponents() {
        firstNameText = new JTextField();
        lastNameText = new JTextField();

        Border outerBorder, innerBorder;
        outerBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);
        innerBorder = BorderFactory.createEmptyBorder(2, 10, 2, 0);
        firstNameText.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
        lastNameText.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

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

    @Override
    public JComponent getRootPanel() {
        return rootPanel;
    }

    @Override
    public void setPresenter(IPresenterSignUp presenter) {
        this.presenter = presenter;
    }

    @Override
    public String getLastName() {
        return lastNameText.getText();
    }

    @Override
    public String getFirstName() {
        return firstNameText.getText();
    }

    @Override
    public void showPhoneInputView() {
        new ViewEnterPhone();
    }

    @Override
    public void showChatView() {
        new ViewChat();
    }


}
