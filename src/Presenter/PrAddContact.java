package Presenter;

import Model.Model;
import View.Forms.Modal.ViewAddContact;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrAddContact implements IPresenter {
    Model model;
    ViewAddContact view;

    public PrAddContact(ViewAddContact view) {
        this.view = view;
        this.model = Model.getInstance();
        setListeners();
    }

    private void setListeners() {
        // Слушатель на кнопку "Назад"
        view.getBackBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.getRootPanel().setVisible(false);
                System.err.println("AddContact Назад");
            }
        });

        // Слушатель кнопку "добавить"
        view.getAddBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO ADD CONTACT IF NOT EXIST
                System.err.println("AddContact Добавить");
                view.getRootPanel().setVisible(false);
            }
        });
    }
}
