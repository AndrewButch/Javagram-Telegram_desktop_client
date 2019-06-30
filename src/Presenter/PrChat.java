package Presenter;

import Model.Model;
import View.Forms.ViewChat;
import View.ListItem.ContactListItem;
import View.ListRenderer.ListCellRendererContact;
import org.javagram.response.object.Dialog;
import org.javagram.response.object.Message;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class PrChat implements IPresenter {
    Model model;
    ViewChat view;
    private ListCellRendererContact contactCellRenderer; // Собственный визуализатор списка контактов
    private User user;
    private ArrayList<UserContact> contacts; // список конактов
    private ArrayList<Dialog> dialogs;      // диалоги для заполнения списка контактов
    private HashMap<Integer, UserContact> contactsHashMap;

    public PrChat(ViewChat view) {
        this.view = view;
        this.model = Model.getInstance();
        setListeners();
        this.user = getSelfUser();
        this.contacts = getContacts();
        this.contactsHashMap = new HashMap<>();
        for (UserContact contact : contacts) {
            contactsHashMap.put(contact.getId(), contact);
        }
        // Имя пользователя - метка
        view.getUserNameLabel().setText(user.getFirstName() + " " + user.getLastName());

        Thread contactListThread = new Thread(new Runnable() {
            @Override
            public void run() {
                setupContactList();
            }
        });
        contactListThread.start();
    }

    private void setListeners() {
        // Обработчик нажатия на кнопку редактирования профиля пользователя
        view.getUserSettingsBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showEditUserProfileView();
            }
        });

        // Обработчик нажатия на кнопку редактирования профиля контакта
        view.getContactEditBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showEditContactView();
            }
        });

        // Обработчик нажатия на кнопку добавления контакта
        view.getAddContactBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showAddContactView();
            }
        });

        final JScrollBar verticalBar = view.getMessageListScrollPane().getVerticalScrollBar();
        // Обработчик нажатия на кнопку отправки сообщений
        final JButton sendMsgBtn = view.getSendMessageBtn();
        sendMsgBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Send Message");
                verticalBar.setValue(verticalBar.getMaximum());
            }
        });
        // Обработчик нажатия на поле ввода сообщения
        final JTextField messageTF = view.getMessageTextField();
        messageTF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Send Message");
                verticalBar.setValue(verticalBar.getMaximum());
            }
        });
        // Обработчик фокуса для поля ввода сообщения
        messageTF.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("Текст сообщения".equals(messageTF.getText())) {
                    messageTF.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if ("".equals(messageTF.getText())) {
                    messageTF.setText("Текст сообщения");
                }
            }
        });
    }

    public User getSelfUser() {
        return model.getSelfUser();
    }

    public ArrayList<UserContact> getContacts() {
        return model.getContacts();
    }

    public ArrayList<Dialog> getDialogs() {
        return model.getDialogs();
    }

    public ArrayList<Message> getMessages (ArrayList<Integer> messageIds) {
        return model.getMessages(messageIds);
    }

    public ArrayList<User> getUsers(ArrayList<Integer> userIds) {
        return model.getUsers(userIds);
    }

    public ArrayList<Message> getMessageHistory(int userId) {
        return model.getMessageHistory(userId);
    }

    /** Получение диалогов и последних сообщений от пользователей
     *  На основании сообщений, упорядоченных по убыванию даты (делает Telegram)
     *  формируем список контактов
     *  */
    private void setupContactList() {
        // Получение диалогов
        dialogs = getDialogs();
        // Получение ID последних сообщений из списка диалогов
        ArrayList<Integer> messageIds = new ArrayList<>();
        for (Dialog dialog : dialogs) {
            messageIds.add(dialog.getTopMessage());
        }

        // Получение самих сообщений на основании ID сообщения
        ArrayList<Message> topMessages = getMessages(messageIds);


        // Создание своей модели JList
        DefaultListModel<ContactListItem> modelContacts = new DefaultListModel<>();

        // Получить ID пользователей из всех сообщений
        ArrayList<Integer> userIds = new ArrayList<>();
        for (Message msg : topMessages) {
            if (user.getId() == msg.getFromId()) {
                userIds.add(msg.getToId());
            } else {
                userIds.add(msg.getFromId());
            }
        }
        // Получить User по списку ID
        ArrayList<User> contactUsers = getUsers(userIds);
        for (User user : contactUsers) {
            System.err.println("ID: " + user.getId() + "\tName: " + user.getFirstName() + "\tLastName: " + user.getLastName());
        }
        System.err.println("------------------------------------------------\n");
        // дебаг на наличие сообщений от незнакомых контактов
        for (Message msg : topMessages) {
            System.err.println("Message: " + msg.getMessage().replace("\n", " ") + "\nFrom: " + msg.getFromId() + "\tTo: " + msg.getToId() );
        }

        // Сформировать модель контактов из ContactListItem()
        for (int i = 0; i < contactUsers.size(); i++ ) {
            modelContacts.addElement(new ContactListItem(contactUsers.get(i), topMessages.get(i)));
        }

        view.getContactsJList().setModel(modelContacts);

        // Передача в визуализатор контактов:
        // - JList для наполнения сообщениями
        // - JLabel для установки имени собеседника
        // - Presenter для получения сообщений
        contactCellRenderer = new ListCellRendererContact();
        view.getContactsJList().setCellRenderer(contactCellRenderer);
        ((ListCellRendererContact) view.getContactsJList().getCellRenderer()).setContacts(view.getMessagesJList(), view.getContactNameLable(), this, topMessages );

    }
}
