package Presenter;

import Model.Model;
import View.Forms.ViewSignUp;
import View.WindowManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrSignUp implements IPresenter {
    Model model;
    ViewSignUp view;

    public PrSignUp(ViewSignUp view) {
        this.view = view;
        this.model = Model.getInstance();
        setListeners();
    }

    private void setListeners() {
        // слушатель на кнопку "Продолжить"
        view.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
        // слушатель на поле ввода фамилии
        view.getLastNameText().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
    }

    public void signUp() {
        String firstName = view.getFirstNameText().getText();
        String lastName = view.getLastNameText().getText();
        String code = model.getSmsCode();

        String error = "";              // Сообщение об ошибке, будет передано в showWarningDialog
        if (firstName.isEmpty()) {      // Проверка заполненности поля Имя
            error += "- Имя\n";
        }
        if (lastName.isEmpty()) {     // Проверка заполненности поля Фамилия
            error += "- Фамилия\n";
        }
        if (!error.isEmpty()) {         // Если в ошибке есть сообщения, то выводится диалог
            WindowManager.showWarningDialog("Не заполнены поля:\n" + error);
        } else {
            model.signUp(firstName, lastName, code);
            if (model.getAuthorization() != null)
                view.goToChatView();
            else
                view.goToEnterPhoneView(); // Переход на на форму ввода телефона, если не авторизовался
        }
    }
}
