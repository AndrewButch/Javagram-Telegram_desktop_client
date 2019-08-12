package View.Interface;

import View.ListItem.ContactListItem;
import View.ListItem.MessageItem;

import javax.swing.*;
import java.awt.image.BufferedImage;

public interface IViewChat extends IView {
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
}
