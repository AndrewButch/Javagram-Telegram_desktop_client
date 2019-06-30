package Presenter;

import Model.Model;
import View.Forms.ViewChat;
import org.javagram.response.object.Dialog;
import org.javagram.response.object.Message;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

public class PrChat implements IPresenter {
    Model model;
    ViewChat view;

    public PrChat(ViewChat view) {
        this.view = view;
        this.model = Model.getInstance();

        setListeners();
    }

    private void setListeners() {
        // Обработчик нажатия на кнопку редактирования профиля пользователя
        view.getUserSettingsBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showEditUserProfileView();
            }
        });

        // Обработчик нажатия на кнопку редактирования профиля контакта
        view.getContactEditBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showEditContactView();
            }
        });

        // Обработчик нажатия на кнопку добавления контакта
        view.getAddContactBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showAddContactView();
            }
        });

        final JScrollBar verticalBar = view.getMessageListScrollPane().getVerticalScrollBar();
        // Обработчик нажатия на кнопку отправки сообщений
        final JButton sendMsgBtn = view.getSendMessageBtn();
        sendMsgBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Send Message");
                verticalBar.setValue(verticalBar.getMaximum());
            }
        });
        // Обработчик нажатия на поле ввода сообщения
        final JTextField messageTF = view.getMessageTextField();
        messageTF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Send Message");
                verticalBar.setValue(verticalBar.getMaximum());
            }
        });
        // Обработчик фокуса для поля ввода сообщения
        messageTF.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("Текст сообщения".equals(messageTF.getText())) {
                    messageTF.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if ("".equals(messageTF.getText())) {
                    messageTF.setText("Текст сообщения");
                }
            }
        });
    }

    public User getSelfUser() {
        return model.getSelfUser();
    }

    public ArrayList<UserContact> getContacts() {
        return model.getContacts();
    }

    public ArrayList<Dialog> getDialogs() {
        return model.getDialogs();
    }

    public ArrayList<Message> getMessages (ArrayList<Integer> messageIds) {
        return model.getMessages(messageIds);
    }

    public ArrayList<User> getUsers(ArrayList<Integer> userIds) {
        return model.getUsers(userIds);
    }

    public ArrayList<Message> getMessageHistory(int userId) {
        return model.getMessageHistory(userId);
    }
}
