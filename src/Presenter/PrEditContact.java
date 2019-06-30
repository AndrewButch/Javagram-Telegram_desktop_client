package Presenter;

import Model.Model;
import View.Forms.Modal.ViewEditContact;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrEditContact implements IPresenter {
    Model model;
    ViewEditContact view;

    public PrEditContact(ViewEditContact view) {
        this.view = view;
        this.model = Model.getInstance();
        setListeners();
    }

    private void setListeners() {
        // TODO
        // Слушатель на кнопку "сохраненя"
        view.getSaveBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO SAVE SETTING
                System.err.println("EditContact Сохранить");
                view.getRootPanel().setVisible(false);
            }
        });
        view.getDeleteBtn().setBorder(BorderFactory.createLineBorder(new Color(187,61,62), 2));
        // Слушатель на кнопку "удалить контакт"
        view.getDeleteBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO DELETE CONTACT
                System.err.println("EditContact Удалить контакт");
            }
        });

        // Слушатель на кнопку "Назад"
        view.getBackBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println("EditContact Назад");
                view.getRootPanel().setVisible(false);
            }
        });
    }
}
