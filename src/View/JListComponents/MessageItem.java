package View.JListComponents;
import org.javagram.response.object.Message;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MessageItem {
    private JPanel rootPanel;
    private JPanel messageTopContainer;
    private JPanel messageBotContainer;
    private JPanel messageLeftContainer;
    private JPanel messageRightContainer;
    private JTextPane messageJTextPane;
    private JPanel messageJPanel;
    private JPanel gridGraphicContainer;
    private JLabel dateLabel;

    private BufferedImage topMessageIMG;
    private BufferedImage botMessageIMG;
    private BufferedImage leftMessageIMG;
    private BufferedImage rightMessageIMG;

    private Message message;

    public MessageItem() {
    }

    public MessageItem(Message msg) {
        this.message = msg;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JPanel getMessageJPanel() {
        return messageJPanel;
    }

    public JTextPane getMessageJTextPane() {
        return messageJTextPane;
    }

    public Message getMessage() {
        return message;
    }

    public void setDate(String date) {
        this.dateLabel.setText(date);
    }

    public void setMessage(String message) {
        this.messageJTextPane.setText(message);
    }

    public void setTopMessageIMG(BufferedImage topMessageIMG) {
        this.topMessageIMG = topMessageIMG;
    }

    public void setBotMessageIMG(BufferedImage botMessageIMG) {
        this.botMessageIMG = botMessageIMG;
    }

    public void setLeftMessageIMG(BufferedImage leftMessageIMG) {
        this.leftMessageIMG = leftMessageIMG;
    }

    public void setRightMessageIMG(BufferedImage rightMessageIMG) {
        this.rightMessageIMG = rightMessageIMG;
    }

    private void createUIComponents() {
        messageTopContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(topMessageIMG, 0,0,null);
            }
        };
        messageBotContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(botMessageIMG, 0,0,null);
            }
        };
        messageLeftContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(leftMessageIMG, 0,0,null);
            }
        };
        messageRightContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(rightMessageIMG, 0,0,null);
            }
        };

    }

}
