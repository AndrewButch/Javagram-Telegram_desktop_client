package View.ListRenderer;

import Presenter.PrChat;
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
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;


public class ListCellRendererContact extends ContactListItem implements ListCellRenderer<ContactListItem> {
    private JList<MessageItem> messageJList;
    private JLabel contactNameJLabel;
    private ListCellRendererMessage messageRenderer;
    private PrChat presenter;
    private BufferedImage contact_gray_online;
    private BufferedImage contact_white_online;
    private ContactListItem selectedItem = null;
    private ArrayList<Message> topMessages = null;
    private ArrayList<Message> messages = null;
    private PropertyChangeSupport pcs;

    public ListCellRendererContact() {
        getPortraitJPanel().setOpaque(true);
        this.contact_gray_online = Resources.getImage(Resources.MASK_GRAY_ONLINE);
        this.contact_white_online = Resources.getImage(Resources.MASK_WHITE_ONLINE);
        pcs = new PropertyChangeSupport(this);

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ContactListItem> list, ContactListItem value, int index, boolean isSelected, boolean cellHasFocus) {
        // Установка текста для текущей ячейки контактов
        setupContactCellText(value);

        // Настройка стиля выделенной ячейки с контактом
        if (isSelected) {
            getRootPanel().setBackground(list.getSelectionBackground());
            getRootPanel().setForeground(list.getSelectionForeground());
            Border lineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(195, 195, 195));
            Border matteBorder = BorderFactory.createMatteBorder(0,0,0, 4, new Color(0, 179, 230));
            getRootPanel().setBorder(new CompoundBorder(lineBorder, matteBorder));
            if (selectedItem == null || value.getUser().getId() != selectedItem.getUser().getId()) {
                selectedItem = value;
                setupMessageList(value);
            }
        } else {
            getRootPanel().setBackground(list.getBackground());
            getRootPanel().setForeground(list.getForeground());
            getRootPanel().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(195, 195, 195)));
            setPortraint(contact_gray_online);
        }
        return this.getRootPanel();
    }

    public void setContacts(PrChat presenter, ArrayList<Message> topMessages) {
        this.presenter = presenter;
        this.messageJList = presenter.getView().getMessagesJList();
        this.contactNameJLabel = presenter.getView().getContactNameLable();
        this.topMessages = topMessages;
        this.pcs.addPropertyChangeListener("selectedItem", presenter);
    }


    private void setupContactCellText(ContactListItem value) {
        User user = value.getUser();
        if (user != null) {
            if (user.getId() == 0) {
                setUserName("Telegram");
            } else {
                setUserName(user.getFirstName() + " " + user.getLastName());
            }
            setLastMsg(value.getLastMsg().getText());
            setLastMsgDate(value.getLastMsgDate().getText());
        }
    }

    private void setupMessageList(ContactListItem selected) {
        User user = selected.getUser();
        System.err.print("Selected: ");
        System.err.println(user.getFirstName() );
        // установка заголовка контакта с которым ведётся диалог
        if (user.getId() != 0) {
            contactNameJLabel.setText(selected.getUser().getFirstName() + " " + selected.getUser().getLastName());
        } else {
            contactNameJLabel.setText("Telegram");
        }
        // TODO установка иконки контакта
        setPortraint(contact_white_online);
        setMessages(selected);
    }

    private void setMessages(ContactListItem selected) {
        int contactId = selected.getUser().getId();
        messages = presenter.getMessageHistory(contactId);
        Collections.reverse(messages);
        DefaultListModel<MessageItem> modelMessage = new DefaultListModel<>();
        for (Message msg : messages) {
            modelMessage.addElement(new MessageItem(msg));
        }
        messageJList.setModel(modelMessage);

        // Создание визуализатора JList с сообщениями
        messageRenderer = new ListCellRendererMessage(presenter.getSelfUser(), selected.getUser(), presenter);
        messageJList.setCellRenderer(messageRenderer);
        messageJList.revalidate();
        messageJList.repaint();
    }

    public ContactListItem getSelectedItem() {
        return this.selectedItem;
    }

    public void setTopMessages(ArrayList<Message> topMessages) {
        this.topMessages = topMessages;
    }

    public void pushMessage(Message msg) {
        messages.add(msg);
    }

}
