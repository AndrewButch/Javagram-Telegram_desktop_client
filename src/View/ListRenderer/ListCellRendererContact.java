package View.ListRenderer;

import Presenter.Presenter;
import View.ListItem.ContactListItem;
import View.ListItem.MessageItem;
import View.Resources;
import org.javagram.response.object.Message;
import org.javagram.response.object.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;


public class ListCellRendererContact extends ContactListItem implements ListCellRenderer<ContactListItem> {
    private JList<MessageItem> messageJList;
    private JLabel contactNameJLabel;
    private ListCellRendererMessage messageRenderer;
    private Presenter presenter;
    private BufferedImage contact_gray_online;
    private BufferedImage contact_white_online;
    private ContactListItem selectedItem = null;
    private ArrayList<Message> topMessages = null;
    private ArrayList<Message> messages = null;

    public ListCellRendererContact() {
        getPortraitJPanel().setOpaque(true);
        this.contact_gray_online = Resources.getImage(Resources.MASK_GRAY_ONLINE);
        this.contact_white_online = Resources.getImage(Resources.MASK_WHITE_ONLINE);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ContactListItem> list, ContactListItem value, int index, boolean isSelected, boolean cellHasFocus) {
        // Настройка стиля выделенной ячейки с контактом
//        if (value != null) {
//            System.err.print("List: value " + value.getUser().getFirstName());
//        } else {
//            System.err.print("List: пусто ");
//        }
//        for (int i = 0; i < list.getModel().getSize(); i++) {
//            System.err.print(" " + list.getModel().getElementAt(i).getUser().getFirstName() + " ");
//        }
//        System.err.print("\n");


        if (isSelected) {
            System.err.print("Selected: ");
            ContactListItem item = list.getModel().getElementAt(index);
            User user = item.getUser();
            if (user.getId() != 0) {
                System.err.print(user.getFirstName() );
                System.err.print("\n");

                getRootPanel().setBackground(list.getSelectionBackground());
                getRootPanel().setForeground(list.getSelectionForeground());
                Border lineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(195, 195, 195));
                Border matteBorder = BorderFactory.createMatteBorder(0,0,0, 4, new Color(0, 179, 230));
                getRootPanel().setBorder(new CompoundBorder(lineBorder, matteBorder));

                if (selectedItem != value){
                    messages = null;
                }
                selectedItem = value;

                // При выборе контакат формируем окно с сообщениями для этого контакта
                int contactId = value.getUser().getId();
                if (messages == null) {
                    messages = presenter.getMessageHistory(contactId);
                    Collections.reverse(messages);
                    DefaultListModel<MessageItem> modelMessage = new DefaultListModel<>();
                    for (Message msg : messages) {
                        modelMessage.addElement(new MessageItem(msg));
                    }
                    // установка метки контакта с которым ведётся диалог
                    contactNameJLabel.setText(value.getUser().getFirstName() + " " + value.getUser().getLastName());
                    // Создание визуализатора JList с сообщениями
                    messageRenderer = new ListCellRendererMessage(presenter.getSelfUser(), value.getUser());
                    messageJList.setModel(modelMessage);
                    messageJList.setCellRenderer(messageRenderer);

                    setPortraint(contact_white_online);

                    messageJList.revalidate();
                    messageJList.repaint();
            }

            }
        } else {
            getRootPanel().setBackground(list.getBackground());
            getRootPanel().setForeground(list.getForeground());
            getRootPanel().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(195, 195, 195)));
            setPortraint(contact_gray_online);
        }

        ContactListItem item = list.getModel().getElementAt(index);
        User user = item.getUser();
        if (user != null) {
            setUserName(user.getFirstName() + " " + user.getLastName());
            setLastMsg(item.getLastMsg().getText());
            setLastMsgDate(item.getLastMsgDate().getText());
        }
        return this.getRootPanel();
    }

    public void setContacts(JList<MessageItem> messageList, JLabel contactLabel, Presenter presenter, ArrayList<Message> topMessages) {
        this.messageJList = messageList;
        this.contactNameJLabel = contactLabel;
        this.presenter = presenter;
        this.topMessages = topMessages;
    }

    public ContactListItem getSelectedItem() {
        return this.selectedItem;
    }

    public void setTopMessages(ArrayList<Message> topMessages) {
        this.topMessages = topMessages;
    }
}
