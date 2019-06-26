package View.Forms.Modal;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.text.ParseException;
import View.Resources;
import View.IView;


public class ViewAddContact implements IView {
    private JPanel rootPanel;
    private JButton addBtn;
    private JTextField firstNameTF;
    private JTextField lastNameTF;
    private JButton backBtn;
    private JLabel titleLabel;
    private JTextPane titleTextPane;
    private JFormattedTextField phoneJFormattedText;

    private BufferedImage icon_phone;
    private BufferedImage backBtnImg;

    public ViewAddContact() {
        icon_phone = Resources.getImage(Resources.ICON_PHONE);
        backBtnImg = Resources.getImage(Resources.ICON_BACK);

        // Рисование кнопку "добавить"
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO SAVE SETTING
                rootPanel.setVisible(false);
            }
        });
    }

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

        // Подчеркивание текстовых полей имя, фамилия
        Border border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);
        firstNameTF = new JTextField();
        lastNameTF = new JTextField();
        firstNameTF.setBorder(border);
        lastNameTF.setBorder(border);

        // Подчеркивание поля телефона
        Border outerBorder, innerBorder;
        outerBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);
        innerBorder = BorderFactory.createEmptyBorder(2, 50, 2, 0);
        phoneJFormattedText.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));


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
        // Слушатель на кнопку "Назад"
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rootPanel.setVisible(false);
            }
        });

        // Выравнивание текста с описанием
        StyleContext sc = new StyleContext();
        DefaultStyledDocument doc = new DefaultStyledDocument(sc);
        titleTextPane = new JTextPane(doc);
        Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
        Style mainStyle = sc.addStyle("My Style", defaultStyle);
        StyleConstants.setAlignment(mainStyle,StyleConstants.ALIGN_CENTER);
        doc.setLogicalStyle(0, mainStyle);
    }

    public void clearFields() {
        phoneJFormattedText.setText("");
        firstNameTF.setText("");
        lastNameTF.setText("");
    }
    public JPanel getRootPanel() {
        return rootPanel;
    }

    @Override
    public void setPresenter() {
        //TODO
    }

    public JFormattedTextField getPhoneJFormattedText() {
        return phoneJFormattedText;
    }
}
