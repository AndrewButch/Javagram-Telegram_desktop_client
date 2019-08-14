package Presenter;

import Presenter.Interface.IPresenterChat;
import Presenter.Interface.IPresenterContactAdd;
import View.Interface.IViewContactAdd;
import View.ListItem.ContactListItem;
import org.javagram.response.object.Message;
import org.javagram.response.object.UserContact;

import java.io.IOException;
import java.util.LinkedList;

public class PrAddContact implements IPresenterContactAdd {
    private IViewContactAdd view;
    private IPresenterChat prChat;

    public PrAddContact(IViewContactAdd view, IPresenterChat prChat) {
        this.view = view;
        this.prChat = prChat;
    }

    @Override
    public void addContact() {
        // TODO ADD CONTACT IF NOT EXIST
        System.err.println("AddContact Добавить");
        String phoneNumber = view.getPhoneNumber();
        phoneNumber = phoneNumber.replaceAll("[^\\d]+", "");
        String firstName = view.getFirstName();
        String lastName = view.getLastName();

        int importedId = 0;
        try {
            importedId = model.contactSendInvite(phoneNumber, firstName, lastName);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (importedId != 0) {
            model.contactsUpdateContacts();
            UserContact contact = model.contactsGetContacts().get(importedId);
            LinkedList<Message> msgList = model.messageGetMessageHistoryByUserID(importedId);
            Message lastMessage = msgList.size() > 0 ? msgList.getFirst(): null;
            ContactListItem item = new ContactListItem(contact, lastMessage);
            model.dialogAddDialogToLocal(importedId, item);
            prChat.refreshDialogList();
            prChat.cleatSelectedContact();
            prChat.setSelectedContact(item);
            prChat.refreshChat();

            System.err.println("Контакт добавлен: " + contact.toString());
        } else {
            System.err.println("Контакт не добавлен");
        }
        view.hideView();
    }
}
