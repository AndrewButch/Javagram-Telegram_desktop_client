package View.Forms;

import Presenter.Interface.IPresenterSMSCodeCheck;
import Presenter.PrSMSCodeCheck;
import View.Interface.IViewSMSCodeCheck;
import View.Resources;
import View.WindowManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ViewSMSCode implements IViewSMSCodeCheck {
    private JPasswordField codePasswordField;
    private JButton nextButton;
    private JLabel phoneNumberLabel;
    private JPanel rootPanel;
    private JTextPane titleTextPane;
    private JPanel logoPanel;
    private BufferedImage logo;
    private BufferedImage lock_phone;
    private IPresenterSMSCodeCheck presenter;


    public ViewSMSCode() {
        logo = Resources.getImage(Resources.LOGO_MINI);
        lock_phone = Resources.getImage(Resources.ICON_LOCK);
        setPresenter(new PrSMSCodeCheck(this));
        setListeners();
        WindowManager.setContentView(this);
        codePasswordField.requestFocus();
    }

    @Override
    public void setPresenter(IPresenterSMSCodeCheck presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showSignUpView() {
        new ViewSignUp();
    }

    @Override
    public void showPhoneInputView() {
        new ViewEnterPhone();
    }

    @Override
    public void showChatView() {
        new ViewChat();
    }

    @Override
    public String getCode() {
        return new String(codePasswordField.getPassword());
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        phoneNumberLabel.setText(phoneNumber);
    }

    private void createUIComponents() {
        logoPanel = new JPanel();
        codePasswordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(lock_phone, 10, 7, null);
            }
        };
        // Установка длины текста в поле кода
        ((PlainDocument)codePasswordField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String string =fb.getDocument().getText(0, fb.getDocument().getLength())+text;
                if (string.length() <= 5)
                    super.replace(fb, offset, length, text, attrs);
            }
        });

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

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void setListeners() {
        // слушатель на кнопку "Продолжить"
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.checkCode();
            }
        });
        // Слушатель для поля ввода СМС-кода
        codePasswordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.checkCode();
            }
        });
    }
}
