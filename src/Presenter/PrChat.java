package Presenter;

import Model.Model;
import View.Forms.ViewChat;
import org.javagram.response.object.Dialog;
import org.javagram.response.object.Message;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;

import java.util.ArrayList;

public class PrChat implements IPresenter {
    Model model;
    ViewChat view;

    public PrChat(ViewChat view) {
        this.view = view;
        this.model = Model.getInstance();

//        setupCodeField();
//        setListeners();
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
