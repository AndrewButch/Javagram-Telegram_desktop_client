package Presenter;

import Model.Model;
import org.javagram.response.object.Dialog;
import org.javagram.response.object.Message;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Presenter {
    private final JFrame view;
    private final Model model;

    public Presenter(JFrame view) {
        this.model = Model.getInstance();
        this.view = view;

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (model.isLoggedIn()) {
                    model.logOut();
                }
                System.exit(0);
            }

            // Задание фокуса на поле ввода телефона
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                //view.requestFocus();
            }
        });
    }
}