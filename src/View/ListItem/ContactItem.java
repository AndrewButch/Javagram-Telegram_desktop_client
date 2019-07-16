package View.ListItem;

import Utils.DateConverter;
import org.javagram.response.object.Message;
import org.javagram.response.object.User;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ContactItem {
    private JPanel portraitJPanel;
    private JLabel userName;
    private JLabel lastMsg;
    private JLabel lastMsgDate;
    private JPanel rootPanel;
    private JLabel unreadLabel;
    private BufferedImage portraint;
    private User user;
    private Message message;
    private int unreadCount;

    public ContactItem(){}

    public ContactItem(User userContact, Message topMsg, int unreadCount) {
        this.user = userContact;
        this.message = topMsg;
        setLastMsg(message.getMessage());
        setLastMsgDate(DateConverter.convertIntDateToStringShort(message.getDate()));
        this.unreadCount = 0;
        if (unreadCount == 0) {
            unreadLabel.setText("");
        } else {
            unreadLabel.setText(unreadCount + "");
        }
    }

    public ContactItem(User userContact, Message topMsg) {
        this(userContact, topMsg, 0);
    }

    public User getUser() {
        return user;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public BufferedImage getPortraint() {
        return portraint;
    }

    public void setPortraint(BufferedImage portraint) {
        this.portraint = portraint;
    }

    public JPanel getPortraitJPanel() {
        return portraitJPanel;
    }

    public void setPortraitJPanel(JPanel portraitJPanel) {
        this.portraitJPanel = portraitJPanel;
    }

    public JLabel getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.setText(userName);
    }

    public JLabel getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg.setText(lastMsg);
    }

    public JLabel getLastMsgDate() {
        return lastMsgDate;
    }

    public void setLastMsgDate(String lastMsgDate) {
        this.lastMsgDate.setText(lastMsgDate);
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
                if (portraint != null) {
                    g.drawImage(portraint, 0, 0, null);
                }
            }
        };
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void incrementUnread() {
        this.unreadCount++;
    }
}
