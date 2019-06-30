package Presenter;

import Model.Model;
import View.Forms.Modal.ViewEditProfile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrEditProfile implements IPresenter {
    Model model;
    ViewEditProfile view;

    public PrEditProfile(ViewEditProfile view) {
        this.view = view;
        this.model = Model.getInstance();
        setListeners();
    }

    private void setListeners() {
        // TODO

        // Слушатель на кнопку "сохранения"
        view.getSaveBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO SAVE SETTING
                System.err.println("EditProfile Сохранить");
                view.getRootPanel().setVisible(false);
            }
        });
        // Слушатель на кнопку "выйти"
        view.getExitBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO LOGOUT
                System.err.println("EditProfile Выйти");
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
