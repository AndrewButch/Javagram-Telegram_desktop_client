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
    private int unreadCount;
    private boolean isOnline;

    public ContactListItem(){}

    public ContactListItem(UserContact userContact, Message topMsg, int unreadCount) {
        this.user = userContact;
        this.isOnline = userContact.isOnline();
        if (topMsg != null) {
            setMessage(topMsg);
        }
        smallPhoto = Resources.getPhoto(Resources.DEFAULT_BIG, false);
        smallPhoto = Resources.getPhoto(userContact.getPhone(), true);
        photo = smallPhoto.getScaledInstance(41, 41, 5);

        this.unreadCount = 0;
        if (unreadCount == 0) {
            unreadLabel.setText("");
        } else {
            unreadLabel.setText(unreadCount + "");
        }
    }

    public ContactListItem(UserContact userContact, Message topMsg) {
        this(userContact, topMsg, 0);
    }

    public ContactListItem(UserContact userContact) {
        this(userContact, null, 0);
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

    public BufferedImage getStatusBorder() {
        return statusBorder;
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

    public JLabel getLastMsg() {
        return lastMsg;
    }

    public Image getPhoto() {
        return photo;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setMessage(Message msg) {
        this.message = msg;
        this.lastMsg.setText(msg.getMessage());
        this.lastMsgDate.setText(DateUtils.convertIntDateToStringShort(msg.getDate()));

    }

    public void setUnreadCount(String count) {
        if ("0".equals(count)) {
            this.unreadLabel.setText("");
        } else {
            this.unreadLabel.setText(count);
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

    public int getUnreadCount() {
        return unreadCount;
    }

    public void incrementUnread() {
        this.unreadCount++;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getUser() == null)
            return false;
        ContactListItem item = (ContactListItem) obj;
        return this.getUser().getId() == item.getUser().getId();
    }
}
