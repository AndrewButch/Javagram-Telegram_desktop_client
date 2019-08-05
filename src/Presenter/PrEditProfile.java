package Presenter;

import View.Forms.Modal.ViewEditProfile;
import View.Forms.ViewEnterPhone;
import org.javagram.response.object.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PrEditProfile implements IPresenter {
    ViewEditProfile view;
    PrChat prChat;
    User user;

    public PrEditProfile(ViewEditProfile view, PrChat prChat) {
        this.view = view;
        this.prChat = prChat;
        setListeners();
        this.user = model.getSelfUser();
        view.setUserInfo(user.getFirstName(), user.getLastName(), user.getPhone());
    }

    private void setListeners() {
        // TODO
        // Слушатель на кнопку "сохранения"
        view.getSaveBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO SAVE SETTING
                String newFirstName = view.getFirstNameTF().getText();
                String newLastName = view.getLastNameTF().getText();
                User updatedUser = null;
                try {
                    updatedUser = model.updateProfileInfo(newFirstName, newLastName);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (updatedUser != null) {
                    user = updatedUser;
                    prChat.updateContactInfo(updatedUser);
                } else {
                    System.err.println("Не удалось обновить профиль");
                }
                System.err.println("EditProfile Сохранить");
                view.getRootPanel().setVisible(false);
            }
        });
        // Слушатель на кнопку "выйти"
        view.getExitBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO LOGOUT
                model.logOut();
                System.err.println("EditProfile Выйти");
                new ViewEnterPhone();
            }
        });

        // Слушатель на кнопку "назад"
        view.getBackBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println("EditProfile Назад");
                view.getRootPanel().setVisible(false);
            }
        });
    }
}
