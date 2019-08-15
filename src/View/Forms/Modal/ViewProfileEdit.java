package View.Forms.Modal;

import Presenter.Interface.IPresenterChat;
import Presenter.Interface.IPresenterProfileEdit;
import Presenter.PrProfileEdit;
import View.Forms.ViewPhoneInput;
import View.Interface.IViewProfileEdit;
import View.Resources;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

public class ViewProfileEdit implements IViewProfileEdit {
    private JPanel rootPanel;
    private JLabel viewLabel;
    private JTextField firstNameTF;
    private JTextField lastNameTF;
    private JButton saveBtn;
    private JButton backBtn;
    private JButton exitBtn;
    private JLabel phoneLabel;

    private BufferedImage backBtnImg;

    private IPresenterProfileEdit presenter;

    public ViewProfileEdit(IPresenterChat prChat) {
        setPresenter(new PrProfileEdit(this, prChat));
        setListeners();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        backBtnImg = Resources.getImage(Resources.ICON_BACK);
        setupRootPanel();
        setupNameField();
        setupBackButton();
    }

    private void setListeners() {
        // Слушатель на кнопку "сохранения"
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.saveProfile();
            }
        });
        // Слушатель на кнопку "выйти"
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.logout();
                new ViewPhoneInput();
            }
        });

        // Слушатель на кнопку "назад"
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideView();
            }
        });
    }

    private void setupRootPanel() {
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

    private void setupNameField() {
        // Подчеркивание текстовых полей
        Border border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE);
        firstNameTF = new JTextField();
        lastNameTF = new JTextField();
        firstNameTF.setBorder(border);
        lastNameTF.setBorder(border);
    }

    @Override
    public void setPresenter(IPresenterProfileEdit presenter) {
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
    public void showView() {
        rootPanel.setVisible(true);
    }

    @Override
    public void hideView() {
        rootPanel.setVisible(false);
    }

    @Override
    public void setUserInfo(String firstName, String lastName, String phoneNumber) {
        firstNameTF.setText(firstName);
        lastNameTF.setText(lastName);
        phoneLabel.setText(phoneNumber);
    }
}
