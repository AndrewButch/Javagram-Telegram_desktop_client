package Presenter;

import Presenter.Interface.IPresenter;
import View.Forms.Modal.ViewEditContact;
import View.ListItem.ContactListItem;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PrEditContact implements IPresenter {
    ViewEditContact view;
    PrChat prChat;

    public PrEditContact(ViewEditContact view, PrChat prChat) {
        this.view = view;
        this.prChat = prChat;
        setListeners();
    }

    private void setListeners() {
        // Слушатель на кнопку "сохраненя"
        view.getSaveBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO SAVE SETTING (НЕ работает метод в Bridge)
                System.err.println("EditContact Сохранить");
                ContactListItem selectedContact = prChat.getSelectedContact();
                UserContact contact = model.contactsGetContacts().get(selectedContact.getUser().getId());
                String name = view.getNameTF().getText();
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
                view.getRootPanel().setVisible(false);
            }
        });
        // Слушатель на кнопку "удалить контакт"
        view.getDeleteBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
