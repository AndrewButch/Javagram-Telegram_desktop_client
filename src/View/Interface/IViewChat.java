package View.Interface;

import Presenter.Interface.IPresenterChat;
import View.ListItem.ContactListItem;
import View.ListItem.MessageItem;
import View.ListRenderer.ListCellRendererContact;

import javax.swing.*;
import java.awt.image.BufferedImage;

public interface IViewChat extends IView {

    void setPresenter(IPresenterChat presenter);

    void showContactAddView();
    void showContactEditView();
    void showProfileEditView();

    void clearMessageField();

    void showDialogs(DefaultListModel<ContactListItem> model);
    void showMessages(DefaultListModel<MessageItem> model);

    void showContactInterface();
    void hideContactInterface();

    void setContactName(String userName);
    void setUserName(String userName);
    void setContactPhoto(BufferedImage photo);
    void setUserPhoto(BufferedImage photo);

    DefaultListModel<ContactListItem> getModelContacts();
    DefaultListModel<MessageItem> getModelMessages();
    JList<ContactListItem> getContactsJList();
    ListCellRendererContact getContactListRenderer();

    void showLoadingView();
    void hideLoadingView();

    void scrollMessagesToEnd();
    void setAlwaysScrollDownMessages(boolean needScrolling);
}
