package Presenter;

import Presenter.Interface.IPresenterChat;
import Presenter.Interface.IPresenterContactEdit;
import View.Interface.IViewContactEdit;
import View.ListItem.ContactListItem;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;

import java.io.IOException;

public class PrContactEdit implements IPresenterContactEdit {
    private IViewContactEdit view;
    private IPresenterChat prChat;

    public PrContactEdit(IViewContactEdit view, IPresenterChat prChat) {
        this.view = view;
        this.prChat = prChat;
    }

    @Override
    public void saveContact() {
        ContactListItem selectedContact = prChat.getSelectedContact();
        UserContact contact = model.contactsGetContacts().get(selectedContact.getUser().getId());
        String name = view.getName();
        try {
            if (contact != null) {
                String phoneNumber = contact.getPhone();
                int id = contact.getId();
                User updated = model.contactUpdateContactInfo(id, phoneNumber, name, "");
                if (updated != null) {
                    prChat.updateDialogName(updated);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void deleteContact() {
        boolean result = model.contactDeleteContact(prChat.getSelectedContact().getUser().getId());
        if (result) {
            // Удалить выбранный контакт из списка диалогов,сбросить селект и спрятать интерфейс
            prChat.deleteDialog();
            model.contactsUpdateContacts();
            model.contactsGetContacts();
            view.getRootPanel().setVisible(false);
            System.err.println("Контакт " + prChat.getSelectedContact().getUser() + " удален");
        } else {
            System.err.println("Контакт " + prChat.getSelectedContact().getUser() + " не удален");
        }
    }
}
