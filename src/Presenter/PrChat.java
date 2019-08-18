package Presenter;

import Presenter.Interface.IPresenterChat;
import Utils.DateUtils;
import View.Interface.IViewChat;
import View.ListItem.ContactListItem;
import View.ListItem.MessageItem;
import View.Resources;
import org.javagram.handlers.IncomingMessageHandler;
import org.javagram.response.MessagesSentMessage;
import org.javagram.response.object.Dialog;
import org.javagram.response.object.Message;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;
import org.telegram.api.TLMessage;
import org.telegram.api.TLPeerUser;
import org.telegram.api.TLUserContact;
import org.telegram.api.TLUserStatusOffline;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class PrChat implements IPresenterChat, IncomingMessageHandler {
    private IViewChat view;
    private User user;
    private Random random;
    private ContactListItem selectedContact;
    private boolean alwaysScrollDownMessages;

    public PrChat(IViewChat view) {
        this.view = view;
        this.user = model.getSelfUser();
        this.random = new Random();
        model.setMessageHandler(this);

        updateDialogName(user);

        Thread contactListThread = new Thread(new Runnable() {
            @Override
            public void run() {
                model.contactsGetContacts();
                Resources.loadPhotos(model.contactsGetContacts());
                updateDialogsLocal();
                refreshDialogList();
                updateUserPhoto(Resources.getPhoto(user.getId(), true));
                view.hideLoadingView();
            }
        });
        contactListThread.start();
    }


    /** Получение диалогов и последних сообщений от пользователей
     *  На основании сообщений, упорядоченных по убыванию даты (делает Telegram)
     *  формируем список контактов
     *  */
    public void updateDialogsLocal() {
        while(!model.contactsUpdateContacts()) {
            System.err.println("Неудалось обновить контакты");
        }
        HashMap<Integer, UserContact> contacts = model.contactsGetContacts();

        ArrayList<Dialog> dialogs = model.dialogGetDialogs();
        ArrayList<Integer> dialogsTopMsgIds = getDialogsTopMsgIds(dialogs);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Message> dialogTopMsgs = model.messageGetMessagesById(dialogsTopMsgIds);

        for (Dialog d : dialogs) {
            int msgId = d.getTopMessage();
            for (int i = 0; i < dialogTopMsgs.size(); i++) {
                Message topMessage = dialogTopMsgs.get(i);
                if (msgId == topMessage.getId()) {
                    int contactId;
                    if (topMessage.isOut()) {
                        contactId = topMessage.getToId();
                    } else {
                        contactId = topMessage.getFromId();
                    }
                    UserContact userContact = contacts.get(contactId);
                    // проверка на пользователя телеграм
                    if (userContact == null) {
                        if (contactId == 777000) {
                            userContact = new UserContact(new TLUserContact(contactId, "Telegram", "", 0, "", null, new TLUserStatusOffline()));
                        } else {
                            continue;
                        }
                    }
                    ContactListItem contactItem = new ContactListItem(userContact, topMessage);
                    // добавление элемента в модель списка
                    // сохраненеи элемента с привязкой к ID контакта
                    model.dialogAddDialogToLocal(contactItem.getUser().getId(), contactItem);
                }
            }
        }
    }

    /**
     * Обработка входящего события выбора контакта из списка контактов
     * Добавлене в модель сообщений нового сообщения.
     * "Всплытие" диалога вверх.
     * @param i идентификатор пользователя
     * @param s текст сообщения
     */
    @Override
    public synchronized Object handle(int i, String s) {
        System.out.println("Add to " + i);
        if (model.messageGetMessageHistoryByUserID(i) == null) {
            System.err.println("Пользователя с ID " + i + " нет в контактах");
            return null;
        }
        TLPeerUser tlPeerUser = new TLPeerUser(this.user.getId());
        TLMessage tlMessage = new TLMessage(0, i, tlPeerUser, false, true, DateUtils.getDateInt(), s, null);
        Message msg = new Message(tlMessage);
        model.messageAddMessageToLocal(i, msg);
        updateDialogsOrder(i);
        return null;
    }

    public void sendMessage(String message) {
        int selectedContactId = selectedContact.getUser().getId();
        view.setAlwaysScrollDownMessages(true);
        // Формироване сообщения
        int messageId = random.nextInt();
        // Отправка сообщения
        MessagesSentMessage sent = model.messageSendMessage(selectedContactId, message, messageId);
        if (sent != null) {
            updateDialogsOrder(selectedContactId);
        }
        view.clearMessageField();
        refreshChat();
    }

    /** Обновляет информацию о текущем собеседнике и показывает сообщения */
    public synchronized void refreshChat() {
        refreshInterfaceBySelectedContact();
        int userId = this.selectedContact.getUser().getId();
        LinkedList<Message> messages = model.messageGetMessageHistoryByUserID(userId);
        DefaultListModel<MessageItem> model = new DefaultListModel<>();
        Iterator<Message> it =  messages.descendingIterator();
        while (it.hasNext()) {
            Message msg = it.next();
            model.addElement(new MessageItem(msg));
        }
        view.showMessages(model);
        view.showContactInterface();
    }

    /** Обновляет порядок диалогов. После отправки сообщения диалог всплывает вверх. */
    private synchronized void updateDialogsOrder(int userId) {
        // Получаем диалог по ID юзера
        ContactListItem lastMessageAdd = model.dialogGetDialogByUserId(userId);
        // Получаем последнее локальное сообщение, для юзера
        Message lastMessage = model.messageGetMessageHistoryByUserID(userId).getFirst();

        DefaultListModel<ContactListItem> contactModel = view.getModelContacts();
        // Проверям содержит ли модель списка имеющийся диалог
        // Если содержит, то удаляем и вставляем уже обновленный диалог в начало

        if (contactModel.contains(lastMessageAdd)) {
            int index = contactModel.indexOf(lastMessageAdd);
            ContactListItem replaceContact = contactModel.get(index);
            replaceContact.setMessage(lastMessage);
            contactModel.remove(index);
            contactModel.add(0, replaceContact);
        } else {
            lastMessageAdd.setMessage(lastMessage);
            contactModel.add(0, lastMessageAdd);
        }
        // Возвращаем select на прежний элемент после изменения порядка
        int selected = contactModel.indexOf(view.getContactListRenderer().getSelectedItem());
        view.showDialogs(contactModel);
        view.getContactsJList().clearSelection();
        view.getContactsJList().setSelectedIndex(selected);
    }

    public void search(String search) {
        // Поиск по контактам
        DefaultListModel<ContactListItem> modelContacts = new DefaultListModel<>();
        HashMap<Integer, UserContact> contacts = model.contactsGetContacts();
        // Поиск по локальным контактам
        for (Map.Entry<Integer, UserContact> entry : contacts.entrySet()) {
            String userName = entry.getValue().toString();
            if ("^".equals(search)) {
                ContactListItem contactItem = new ContactListItem(entry.getValue());
                modelContacts.addElement(contactItem);
            } else if (userName != null && userName.contains(search)) {
                ContactListItem contactItem = new ContactListItem(entry.getValue());
                modelContacts.addElement(contactItem);
            }
        }
        // Поиск сообщений через сервер Telegram
        try {
            ArrayList<ContactListItem> result = model.messageSearchMessage(search);
            for (ContactListItem item : result) {
                modelContacts.addElement(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.showDialogs(modelContacts);
    }

    /** Показывает иконку, имя собеседника и элементы отправки сообщений*/
    public void showInterface() {
        view.showContactInterface();
    }

    /** Получение идентификаторов сообщений из диалогов */
    private ArrayList<Integer> getDialogsTopMsgIds(ArrayList<Dialog> dialogs) {
        ArrayList<Integer> messageIds = new ArrayList<>();
        for (Dialog dialog : dialogs) {
            if(dialog != null)
                messageIds.add(dialog.getTopMessage());
        }
        return messageIds;
    }
    /** Запоминает ContactListItem, который выбран в данный момент */
    public ContactListItem getSelectedContact() {
        return selectedContact;
    }
    /** Устанавливает ContactListItem, который выбран в данный момент */
    public void setSelectedContact(ContactListItem item) {
        if (selectedContact == null || selectedContact.getUser().getId() != item.getUser().getId()) {
            view.setAlwaysScrollDownMessages(true);
        }
        this.selectedContact = item;
    }
    /** Очищает ContactListItem, который выбран в данный момент */
    public void cleatSelectedContact() {
        view.getContactListRenderer().clearSelectedItem();
        setSelectedContact(null);
    }

    /** Обновляет интерфейс текущего собеседника*/
    public void refreshInterfaceBySelectedContact() {
        User selected = selectedContact.getUser();
        view.setContactName(selected.toString());
        BufferedImage photo = Resources.getPhoto(selected.getId(), true);
        view.setContactPhoto(photo);
        view.getRootPanel().revalidate();
        view.getRootPanel().repaint();
    }

    /** Обновление списка диалогов из локального хранения*/
    public void refreshDialogList() {
        DefaultListModel<ContactListItem> modelContacts = new DefaultListModel<>();
       for (ContactListItem item : model.dialogGetDialogList().values()) {
           modelContacts.addElement(item);
       }
        view.showDialogs(modelContacts);
    }

    public void updateUserPhoto(BufferedImage photo) {
        view.setUserPhoto(photo);
    }

    /** Переименование имени контакта в диалоге*/
    public void updateDialogName(User updatedUser) {
        if (updatedUser.getId() == this.user.getId()) {
            user = updatedUser;
            view.setUserName(user.toString());
        } else {
            // Обновление имени в сохраненном списке диалогов
            model.dialogUpdateContactNameLocal(updatedUser.getId());
            // Обновление имени в списке диалогов на экране
            int index = view.getModelContacts().indexOf(selectedContact);
            ContactListItem contactListItem = model.dialogGetDialogByUserId(updatedUser.getId());
            view.getModelContacts().set(index, contactListItem);
        }
    }

    public void deleteDialog() {
        int index = view.getModelContacts().indexOf(view.getContactListRenderer().getSelectedItem());
        view.getModelContacts().remove(index);
        view.hideContactInterface();
    }
    /** Удалить историю текущего пользователя */
    public void deleteHistory() {
        try {
            int selected = selectedContact.getUser().getId();
            model.messageDeleteMessageHistory(selected);
            model.messageGetMessageHistoryByUserID(selected).clear();
            model.messageUpdateMessageHistoryByUserID(selected);
            refreshChat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getSelfUser() {
        return this.user;
    }
}
