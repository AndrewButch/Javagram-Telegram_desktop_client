package View.Forms.Modal;

import Presenter.Interface.IPresenter;
import Presenter.PrChat;
import Presenter.PrEditContact;
import View.IView;
import View.Resources;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;


public class ViewEditContact implements IView {
    private JPanel rootPanel;
    private JLabel viewLabel;
    private JPanel contactJPanel;
    private JPanel portrait;
    private JTextField nameTF;
    private JButton saveBtn;
    private JButton backBtn;
    private JButton deleteBtn;
    private JLabel phoneLabel;

    private BufferedImage backBtnImg;
    private BufferedImage borderImg;
    private BufferedImage photo;
    private Image scaledPhoto;

    private PrEditContact presenter;

    public ViewEditContact(PrChat prChat) {
        setPresenter(new PrEditContact(this, prChat));
        deleteBtn.setBorder(BorderFactory.createLineBorder(new Color(187,61,62), 2));

    }

    private void createUIComponents() {
        // Получение ресурсов изображений
        backBtnImg = Resources.getImage(Resources.ICON_BACK);
        borderImg = Resources.getImage(Resources.MASK_DARK_GRAY_BIG);
        photo = Resources.getPhoto(0, false);
        scaledPhoto = photo.getScaledInstance(66,66, 5);
        setupRootPanel();
        setupBackButton();
        setupContactPhoto();
        setupNameField();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JButton getSaveBtn() {
        return saveBtn;
    }

    public JButton getBackBtn() {
        return backBtn;
    }

    public JButton getDeleteBtn() {
        return deleteBtn;
    }

    public JLabel getPhoneLabel() {
        return phoneLabel;
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        this.presenter = (PrEditContact) presenter;

    }

    public void setContactInfo(String name, String phoneNumber, int userId) {
        nameTF.setText(name);
        phoneLabel.setText(phoneNumber);
        photo = Resources.getPhoto(userId, false);
        scaledPhoto = photo.getScaledInstance(66,66, 5);
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
        rootPanel.setVisible(false); // изначально панель не видна
        rootPanel.addMouseListener(new MouseAdapter() {}); // Перехват событий мышки окном
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

    private void setupContactPhoto() {
        // Рисование иконки контакта
        portrait = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(scaledPhoto, 0, 0, null);
//                g.drawImage(borderImg, 0, 0, null);
            }
        };
    }

    private void setupNameField() {
        // Подчеркивание текстового поля
        Border border;
        border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);
        nameTF = new JTextField();
        nameTF.setBorder(border);
    }

    public JTextField getNameTF() {
        return nameTF;
    }
}
