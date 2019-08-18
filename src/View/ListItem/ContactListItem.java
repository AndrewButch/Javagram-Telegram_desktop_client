package View.ListItem;

import Utils.DateUtils;
import View.Resources;
import org.javagram.response.object.Message;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ContactListItem {
    private JPanel portraitJPanel;
    private JLabel userName;
    private JLabel lastMsg;
    private JLabel lastMsgDate;
    private JPanel rootPanel;
    private JLabel unreadLabel;
    private BufferedImage statusBorder;
    private BufferedImage smallPhoto;
    private Image photo;
    private UserContact user;
    private Message message;
    private boolean isOnline;

    public ContactListItem(){}

    public ContactListItem(UserContact userContact, Message topMsg) {
        this.user = userContact;
        this.isOnline = userContact.isOnline();
        setMessage(topMsg);
        smallPhoto = Resources.getPhoto(0, false);
        smallPhoto = Resources.getPhoto(userContact.getId(), true);
        photo = smallPhoto.getScaledInstance(41, 41, 5);
    }

    public ContactListItem(UserContact userContact) {
        this(userContact, null);
    }

    public User getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void setStatusBorder(BufferedImage statusBorder) {
        this.statusBorder = statusBorder;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public JPanel getPortraitJPanel() {
        return portraitJPanel;
    }

    public void setUserName(String userName) {
        this.userName.setText(userName);
    }

    public Image getPhoto() {
        return photo;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setMessage(Message msg) {
        this.message = msg;
        if (msg != null) {
            this.lastMsg.setText(msg.getMessage());
            this.lastMsgDate.setText(DateUtils.convertIntDateToStringShort(msg.getDate()));
        } else {
            this.lastMsg.setText("");
            this.lastMsgDate.setText("");
        }

    }

    private void createUIComponents() {
        portraitJPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(photo, 0, 0, null);
                g.drawImage(statusBorder, 0, 0, null);
            }
        };
    }

}
