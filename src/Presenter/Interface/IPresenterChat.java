package Presenter.Interface;

import View.ListItem.ContactListItem;
import org.javagram.response.object.User;

import java.awt.image.BufferedImage;

public interface IPresenterChat extends IPresenter {

    void updateDialogsLocal();
    void sendMessage(String message);
    void loadContactPhotos();
    void refreshChat();
    void search(String search);
    void showInterface();
    void setSelectedContact(ContactListItem item);
    ContactListItem getSelectedContact();
    void cleatSelectedContact();
    void refreshInterfaceBySelectedContact();
    void refreshDialogList();
    void updateUserPhoto(BufferedImage photo);
    void updateDialogName(User updatedUser);
    void deleteDialog();
    void deleteHistory();
}
