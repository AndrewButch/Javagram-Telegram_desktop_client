package Presenter;

import View.Forms.Modal.ViewAddContact;
import View.ListItem.ContactListItem;
import org.javagram.response.object.UserContact;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PrAddContact implements IPresenter {
    private ViewAddContact view;
    private PrChat prChat;

    public PrAddContact(ViewAddContact view, PrChat prChat) {
        this.view = view;
        this.prChat = prChat;
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
                String phoneNumber = view.getPhoneJFormattedText().getText();
                phoneNumber = phoneNumber.replaceAll("[^\\d]+", "");
                String firstName = view.getFirstNameTF().getText();
                String lastName = view.getLastNameTF().getText();

                int importedId = 0;
                try {
                    importedId = model.contactSendInvite(phoneNumber, firstName, lastName);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (importedId != 0) {
                    model.contactsUpdateContacts();
                    UserContact contact = model.contactsGetContacts().get(importedId);
                    ContactListItem item = new ContactListItem(contact);
                    model.dialogAddDialogToLocal(importedId, item);
                    prChat.cleatSelectedContact();
                    prChat.setSelectedContact(item);
                    prChat.refreshChat();

                    System.err.println("Контакт добавлен: " + contact.toString());
                } else {
                    System.err.println("Контакт не добавлен");
                }
                view.getRootPanel().setVisible(false);
            }
        });
    }
}
