package View.JListComponents;

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
    private BufferedImage portraint;
    private User user;
    private Message message;

    public ContactListItem(){}

    public ContactListItem (User userContact, Message topMsg) {
        this.user = userContact;
        this.message = topMsg;
        setLastMsg(message.getMessage());
        setLastMsgDate(Utils.Utils.convertIntDateToStringShort(message.getDate()));
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
}
