package Presenter;

import Model.Model;
import View.FormManager;
import org.javagram.response.object.Dialog;
import org.javagram.response.object.Message;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Presenter {
    private final FormManager view;
    private final Model model;

    public Presenter(FormManager view) {
        this.model = new Model();
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

    public void checkPhoneForm(String phoneNumber) {
        boolean autorizationPhone = model.authCheckPhone(phoneNumber);
        if (autorizationPhone) {
            view.goToCodeForm(phoneNumber);
        } else {
            view.showWarningDialog("Что-то пошло не так");
        }
    }


    public void checkCodeForm(String code) {
        if (code.isEmpty())
            view.showWarningDialog("Необходимо ввести SMS-код");
        else if (model.isRegistered()) {
            boolean authorizationSmS = model.signIn(code);
            if (authorizationSmS)
                view.goToChatForm();
            else
                view.goToPhoneForm(); // Переход на на форму ввода телефона, если смс-код неверный
        } else
            view.goToSignUpForm();
    }

    public void signUp(String firstName, String lastName, String code) {
        String error = "";              // Сообщение об ошибке, будет передано в showWarningDialog
        if (firstName.isEmpty()) {      // Проверка заполненности поля Имя
            error += "- Имя\n";
        }
        if (lastName.isEmpty()) {     // Проверка заполненности поля Фамилия
            error += "- Фамилия\n";
        }
        if (!error.isEmpty()) {         // Если в ошибке есть сообщения, то выводится диалог
            view.showWarningDialog("Не заполнены поля:\n" + error);
        } else {
            boolean authorization = model.signUp(firstName, lastName, code);
            if (authorization)
                view.goToChatForm();
            else
                view.goToPhoneForm(); // Переход на на форму ввода телефона, если не авторизовался
        }
    }

    public ArrayList<UserContact> getContacts() {
        return model.getContacts();
    }

    public User getSelfUser(){
        return model.getSelfUser();
    }

    /** Поиск сообщений от текущего пользователя к конкретному пользователю toID*/
    public ArrayList<Message> getMessageHistory(int contactId) {

        ArrayList<Message> allMsg = model.messagesGetHistory(contactId);

        return allMsg;
    }

    public ArrayList<Dialog> getDialogs() {
        return model.getDialogs();
    }

    public ArrayList<Message> getMessages(ArrayList<Integer> ids) {
        return model.getMessages(ids);
    }

    public ArrayList<User> getUsers(ArrayList<Integer> ids) {
        return model.getUsers(ids);
    }
}
