package Presenter;

import Model.Model;
import Utils.DateConverter;
import View.Forms.ViewChat;
import View.ListItem.ContactItem;
import View.ListItem.MessageItem;
import org.javagram.handlers.IncomingMessageHandler;
import org.javagram.response.MessagesSentMessage;
import org.javagram.response.object.*;
import org.javagram.response.object.Dialog;
import org.telegram.api.TLMessage;
import org.telegram.api.TLPeerUser;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PrChat implements IPresenter, IncomingMessageHandler {
    Model model;
    ViewChat view;
    private User user;
    private HashMap<Integer, ContactItem> dialogList = new HashMap<>();
    private Random random;
    private State state;

    public PrChat(ViewChat view) {
        this.view = view;
        this.model = Model.getInstance();
        this.user = model.getSelfUser();
        this.random = new Random();
        model.setMessageHandler(this);
        this.state = model.getState();

        // Имя пользователя - метка сверху над чатом
        view.getUserNameLabel().setText(user.getFirstName() + " " + user.getLastName());

        Thread contactListThread = new Thread(new Runnable() {
            @Override
            public void run() {
                setupContactList();
            }
        });
        contactListThread.start();
    }



    public ViewChat getView() {
        return view;
    }


    /** Получение диалогов и последних сообщений от пользователей
     *  На основании сообщений, упорядоченных по убыванию даты (делает Telegram)
     *  формируем список контактов
     *  */
    private void setupContactList() {
        // Получение диалогов
        ArrayList<Dialog> dialogs = model.getDialogs();
        // Получение ID последних сообщений из списка диалогов
        ArrayList<Integer> messageIds = new ArrayList<>();
        for (Dialog dialog : dialogs) {
            if(dialog != null)
                messageIds.add(dialog.getTopMessage());
        }

        // Последние сообщения на основании ID сообщения
        ArrayList<Message> topMessages = model.getMessagesById(messageIds);

        // Получить ID собеседников из последних сообщений
        ArrayList<Integer> userIds = new ArrayList<>();
        for (Message msg : topMessages) {
            if (msg.getToId() == 0 || msg.getFromId() == 0) {
                continue;
            } else if (user.getId() == msg.getFromId()) {
                userIds.add(msg.getToId());
            } else {
                userIds.add(msg.getFromId());
            }
            System.err.println("Message: " + msg.getMessage().replace("\n", " ") + "\nFrom: " + msg.getFromId() + "\tTo: " + msg.getToId() );
        }
        System.err.println("------------------------------------------------\n");

        // Получить список User по списку ID
        ArrayList<User> contactUsers = model.getUsersById(userIds);

        // Сформировать модель контактов из ContactItem()
        DefaultListModel<ContactItem> modelContacts = new DefaultListModel<>();
        for (int i = 0; i < contactUsers.size(); i++ ) {
            User user = contactUsers.get(i);
            if (user.getId() == 0) {

//                if (topMessages.get(i).getMessage() != null) {
                continue;
            }
            System.err.println("ID: " + user.getId() + "\tName: " + user.getFirstName() + "\tLastName: " + user.getLastName());
            ContactItem contactItem = new ContactItem(user, topMessages.get(i));
            // добавление элемента в модель списка
            modelContacts.addElement(contactItem);
            // сохраненеи элемента с привязкой к ID контакта
            dialogList.put(user.getId(), contactItem);
        }
        view.showDialogs(modelContacts);


        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1500);
                        handle(random.nextInt(4) + 13, "hello");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
    }





    public User getSelfUser() {
        return user;
    }


    /**
     * Обработка события выбора контакта из списка контактов
     * Установка в модель списка сообщений новые сообщения
     */
    @Override
    public synchronized Object handle(int i, String s) {
        System.out.println("Add to " + i);
        TLPeerUser tlPeerUser = new TLPeerUser(getSelfUser().getId());
        TLMessage tlMessage = new TLMessage(0, i, tlPeerUser, false, true, DateConverter.getDateInt(), s, null);
        Message msg = new Message(tlMessage);
        model.addMessage(i, msg);
        updateDialogsOrder(i, msg);
        ContactItem selected = view.getContactListRenderer().getSelectedItem();
        if (selected == null) {
            return null;
        }
        if (i == selected.getUser().getId()) {
            // TODO обновить список сообщений текущего контакта
            view.getModelMessages().addElement(new MessageItem(msg));
            view.scrollMessagesToEnd();
        }
        return null;
    }

    public void sendMessage(int userId, String message) {

        // Формироване сообщения
        int messageId = random.nextInt();
        // Отправка сообщения
        MessagesSentMessage sent = model.sendMessage(userId, message, messageId);
        // Создание локального сообщения
        TLPeerUser tlPeerUser = new TLPeerUser(getSelfUser().getId());
        TLMessage tlMessage = new TLMessage(sent.getId(), userId, tlPeerUser, true, true, DateConverter.getDateInt(), message, null);
        Message msg = new Message(tlMessage);

        // добавляем локальное сообщение в модель списка
        view.getModelMessages().addElement(new MessageItem(msg));
        // добавляем локальное сообщение в хранилище
        model.addMessage(userId, msg);

        updateDialogsOrder(userId, msg);

        view.scrollMessagesToEnd();

        view.clearMessageTextField();
    }

    public void updateChat(User user) {
        view.updateContactLabel(user.getFirstName() + " " + user.getLastName());

        // TODO возвратить модель, заполненную сообщениями в обратном порядке
        int userId = user.getId();
        LinkedList<Message> messages = model.getMessageHistoryByUserID(userId);

        Iterator<Message> it =  messages.iterator();
        DefaultListModel<MessageItem> model = new DefaultListModel<>();
        for (Message msg : messages) {
            model.addElement(new MessageItem(msg));
        }
        view.showMessages(model);
    }

    private void updateDialogsOrder(int userId, Message msg) {
        // Получаем элемент списка по ID юзера
        ContactItem lastMessageAdd = dialogList.get(userId);

        DefaultListModel<ContactItem> contactModel = view.getModelContacts();
        // Проверям содержит ли модель списка имеющийся диалог
        // Если содержит, то удаляем и вставляем в начало
        if (contactModel.contains(lastMessageAdd)) {
            int index = contactModel.indexOf(lastMessageAdd);
            ContactItem replaceContact = contactModel.get(index);
            replaceContact.setLastMsg(msg.getMessage());
            replaceContact.setLastMsgDate(DateConverter.convertIntDateToStringShort(DateConverter.getDateInt()));
            replaceContact.incrementUnread();
            contactModel.remove(index);
            contactModel.add(0, replaceContact);

            // Возвращаем select на прежний элемент после изменения порядка
            index = contactModel.indexOf(view.getContactListRenderer().getSelectedItem());
            view.getContactsJList().clearSelection();
            view.getContactsJList().setSelectedIndex(index);
        }
    }
}
