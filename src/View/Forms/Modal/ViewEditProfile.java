package View.Forms.Modal;

import Presenter.Interface.IPresenter;
import Presenter.Interface.IPresenterChat;
import Presenter.Interface.IPresenterProfileEdit;
import Presenter.PrChat;
import Presenter.PrEditProfile;
import View.Interface.IView;
import View.Interface.IViewProfileEdit;
import View.Resources;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

public class ViewEditProfile implements IViewProfileEdit {
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

    public ViewEditProfile(IPresenterChat prChat) {
        setPresenter(new PrEditProfile(this, prChat));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    @Override
    public void setPresenter(IPresenterProfileEdit presenter) {
        this.presenter = presenter;
    }

    private void createUIComponents() {
        backBtnImg = Resources.getImage(Resources.ICON_BACK);
        setupRootPanel();
        setupNameField();
        setupBackButton();
    }

    public void setUserInfo(String firstName, String lastName, String phoneNumber) {
        firstNameTF.setText(firstName);
        lastNameTF.setText(lastName);
        phoneLabel.setText(phoneNumber);
    }

    public JTextField getFirstNameTF() {
        return firstNameTF;
    }

    public JTextField getLastNameTF() {
        return lastNameTF;
    }

    public JButton getSaveBtn() {
        return saveBtn;
    }

    public JButton getBackBtn() {
        return backBtn;
    }

    public JButton getExitBtn() {
        return exitBtn;
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
}
