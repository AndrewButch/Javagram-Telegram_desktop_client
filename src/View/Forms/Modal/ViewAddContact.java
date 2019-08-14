package View.Forms.Modal;

import Presenter.Interface.IPresenterChat;
import Presenter.Interface.IPresenterContactAdd;
import Presenter.PrAddContact;
import View.Interface.IViewContactAdd;
import View.Resources;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.text.ParseException;


public class ViewAddContact implements IViewContactAdd {
    private JPanel rootPanel;
    private JLabel viewLabel;
    private JTextPane titleTextPane;
    private JFormattedTextField phoneJFormattedText;
    private JTextField firstNameTF;
    private JTextField lastNameTF;
    private JButton addBtn;
    private JButton backBtn;

    private BufferedImage icon_phone;
    private BufferedImage backBtnImg;

    private IPresenterContactAdd presenter;

    public ViewAddContact(IPresenterChat prChat) {
        setPresenter(new PrAddContact(this, prChat));
        setListeners();
    }

    private void createUIComponents() {
        // Получение ресурсов изображений
        icon_phone = Resources.getImage(Resources.ICON_PHONE);
        backBtnImg = Resources.getImage(Resources.ICON_BACK);

        // настройка графического интерфейса
        setupRootPanel();
        setupTitleText();
        setupFormattedText();
        setupNameFields();
        setupBackButton();
    }
    public JPanel getRootPanel() {
        return rootPanel;
    }


    private void setupRootPanel() {
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

    private void setupFormattedText() {
        // задание форматированного поля с номером телефона
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
        // Подчеркивание поля телефона
        Border outerBorder, innerBorder;
        outerBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);
        innerBorder = BorderFactory.createEmptyBorder(2, 50, 2, 0);
        phoneJFormattedText.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
    }

    private void setupNameFields() {
        // Подчеркивание текстовых полей имя, фамилия
        Border border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);
        firstNameTF = new JTextField();
        lastNameTF = new JTextField();
        firstNameTF.setBorder(border);
        lastNameTF.setBorder(border);
    }

    private void setupBackButton() {
        // Рисование кнопки "назад"
        backBtn = new JButton(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 0.9f));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                g.drawImage(backBtnImg, 0, 0, null);
            }
        };
    }

    private void setupTitleText() {
        // Выравнивание текста с описанием
        StyleContext sc = new StyleContext();
        DefaultStyledDocument doc = new DefaultStyledDocument(sc);
        titleTextPane = new JTextPane(doc);
        Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
        Style mainStyle = sc.addStyle("My Style", defaultStyle);
        StyleConstants.setAlignment(mainStyle,StyleConstants.ALIGN_CENTER);
        doc.setLogicalStyle(0, mainStyle);
    }

    private void setListeners() {
        // Слушатель на кнопку "Назад"
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rootPanel.setVisible(false);
                System.err.println("AddContact Назад");
            }
        });

        // Слушатель кнопку "добавить"
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.addContact();
            }
        });
    }

    @Override
    public void setPresenter(IPresenterContactAdd presenter) {
        this.presenter = presenter;
    }

    @Override
    public String getFirstName() {
        return firstNameTF.getText();
    }

    @Override
    public String getLastName() {
        return lastNameTF.getText();
    }

    @Override
    public String getPhoneNumber() {
        return phoneJFormattedText.getText();
    }

    @Override
    public void showView() {
        rootPanel.setVisible(true);
        phoneJFormattedText.requestFocus();
    }

    @Override
    public void hideView() {
        rootPanel.setVisible(false);
    }

    @Override
    public void clearFields() {
        phoneJFormattedText.setText("");
        firstNameTF.setText("");
        lastNameTF.setText("");
    }
}
