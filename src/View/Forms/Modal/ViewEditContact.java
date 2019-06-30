package View.Forms.Modal;

import Presenter.IPresenter;
import Presenter.PrEditContact;
import View.IView;
import View.Resources;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;


public class ViewEditContact implements IView {
    private JPanel rootPanel;
    private JPanel contactJPanel;
    private JPanel portrait;
    private JButton saveBtn;
    private JButton backBtn;
    private JButton deleteBtn;
    private JTextField nameTF;
    private JLabel phoneLabel;
    private JLabel titleLabel;

    private BufferedImage backBtnImg;
    private BufferedImage contactImg;

    private PrEditContact presenter;


    public ViewEditContact() {
        setPresenter(new PrEditContact(this));
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
        rootPanel.setVisible(false); // изначально панель не видна
        rootPanel.addMouseListener(new MouseAdapter() {}); // Перехват событий мышки окном

        backBtnImg = Resources.getImage(Resources.ICON_BACK);
        contactImg = Resources.getImage(Resources.MASK_DARK_GRAY_BIG);

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

        // Рисование иконки контакта
        portrait = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(contactImg, 0, 0, null);
            }
        };

        // Подчеркивание текстового поля
        Border border;
        border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);
        nameTF = new JTextField();
        nameTF.setBorder(border);

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        this.presenter = (PrEditContact) presenter;

    }

    public void setContactInfo(String name, String phoneNumber) {
        nameTF.setText(name);
        phoneLabel.setText(phoneNumber);
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
}
