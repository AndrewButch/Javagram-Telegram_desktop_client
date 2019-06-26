package View.Forms.Modal;

import View.Resources;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

public class ViewEditProfile {
    private JPanel rootPanel;
    private JButton saveBtn;
    private JTextField firstNameTF;
    private JTextField lastNameTF;
    private JButton backBtn;
    private JButton exitBtn;
    private JLabel titleLabel;
    private JLabel phoneLabel;

    private BufferedImage backBtnImg;

    public ViewEditProfile() {
        // Слушатель на кнопку "сохранения"
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO SAVE SETTING
                rootPanel.setVisible(false);
            }
        });
        // Слушатель на кнопку "выйти"
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO LOGOUT
            }
        });

        // Слушатель на кнопку "назад"
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rootPanel.setVisible(false);
            }
        });


    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
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
        rootPanel.addMouseListener(new MouseAdapter() {}); // перехват событий мыши окном

        backBtnImg = Resources.getImage(Resources.ICON_BACK);

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


        // Подчеркивание текстовых полей
        Border border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);
        firstNameTF = new JTextField();
        lastNameTF = new JTextField();
        firstNameTF.setBorder(border);
        lastNameTF.setBorder(border);
    }

    public void setUserInfo(String firstName, String lastName, String phoneNumber) {
        firstNameTF.setText(firstName);
        lastNameTF.setText(lastName);
        phoneLabel.setText(phoneNumber);
    }
}
