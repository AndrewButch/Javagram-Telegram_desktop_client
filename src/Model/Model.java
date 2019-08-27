package Model;

import Utils.DateUtils;
import View.ListItem.ContactListItem;
import com.oracle.tools.packager.Log;
import org.javagram.TelegramApiBridge;
import org.javagram.handlers.IncomingMessageHandler;
import org.javagram.response.AuthAuthorization;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.AuthSentCode;
import org.javagram.response.MessagesSentMessage;
import org.javagram.response.object.Dialog;
import org.javagram.response.object.Message;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;
import org.telegram.api.*;
import org.telegram.api.contacts.TLImportedContacts;
import org.telegram.api.engine.TelegramApi;
import org.telegram.api.messages.TLAbsMessages;
import org.telegram.api.messages.TLAffectedHistory;
import org.telegram.api.requests.TLRequestContactsImportContacts;
import org.telegram.api.requests.TLRequestMessagesDeleteHistory;
import org.telegram.api.requests.TLRequestMessagesGetHistory;
import org.telegram.api.requests.TLRequestMessagesSearch;
import org.telegram.tl.TLVector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class Model {
    private final String hostAddr = "149.154.167.50:443";
    private final int appId = 568751;
    private final String appHash = "ec1d629d0855caa5425a9c83cdc5925d";
    private final int request_interval = 5000;
    TelegramApi api;
    TelegramApiBridge  bridge;

    private AuthAuthorization authorization;
    private AuthCheckedPhone authCheckedPhone;
    private AuthSentCode authSendCode;

    private User selfUser;
    private static Model model;
    private String phone;
    private String smsCode;

    // Хранилище сообщений <contactId, LinkedList<Messages>
    private volatile HashMap<Integer, LinkedList<Message>> contactsMessageHistory;
    // Список контактов <contactId, UserContact>
    private volatile HashMap<Integer, UserContact> contacts;
    // Список диалогов <contactId, ContactListItem>
    private volatile LinkedHashMap<Integer, ContactListItem> dialogList = new LinkedHashMap<>();


    private Model() {
        while (bridge == null) {
            try {
                bridge = new TelegramApiBridge(hostAddr, appId, appHash);
                api = getTelegramApi(bridge);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(request_interval);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        contactsMessageHistory = new HashMap<>();

    }

    public synchronized static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public AuthAuthorization getAuthorization() {
        return authorization;
    }

    public AuthSentCode getAuthSendCode() {
        try {
            authCheckedPhone = bridge.authCheckPhone(phone);
            if (authCheckedPhone.isRegistered()) {
                authSendCode = bridge.authSendCode(phone);
                return authSendCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void logOut() {
        try {
            if (authorization != null && bridge != null) {
                bridge.authLogOut();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signIn(String code) {
        try {
            authorization = bridge.authSignIn(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signUp(String firstName, String lastName, String code) {
        try {
            authorization = bridge.authSignUp(code, firstName, lastName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    /**
     * Получаем приватный объект TelegramApi используем рефлексию java.
     * @param bridge
     */
    private TelegramApi getTelegramApi(TelegramApiBridge bridge) {
        TelegramApi api = null;
        try {
            Field fieldApi = bridge.getClass().getDeclaredField("api");
            fieldApi.setAccessible(true);
            api = (TelegramApi) fieldApi.get(bridge);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return api;
    }

    public User getSelfUser() {
        if (selfUser == null) {
            if (authorization == null) {
                return null;
            } else {
                selfUser = authorization.getUser();
            }
        }
        return selfUser;
    }

    /** Получение контактов из локального хранения.
     * Предполагается, что контакты обновляются с помощью метода обновления контаков
     * @see #contactsUpdateContacts() */

    public synchronized HashMap<Integer, UserContact> contactsGetContacts() {
        if (this.contacts == null) {
            contactsUpdateContacts();
        }
        return this.contacts;
    }

    /** Обновление списка контактов из Telegram */
    public synchronized boolean contactsUpdateContacts() {
        try {
            ArrayList<UserContact> conts = bridge.contactsGetContacts();
            this.contacts = new HashMap<>();
            for (UserContact cont : conts) {
                this.contacts.put(cont.getId(), cont);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /** Добавление нового контакта */
    public int contactSendInvite(String phoneNumber, String firstName, String lastName) throws IOException {
        firstName = firstName.replaceAll("\\\\s+", " ");
        lastName = lastName.replaceAll("\\\\s+", " ");

        TLVector<TLInputContact> v = new TLVector();
        v.add(new TLInputContact(0, phoneNumber, firstName, lastName));
        TLRequestContactsImportContacts ci = new TLRequestContactsImportContacts(v, true);
        TLImportedContacts ic = this.api.doRpcCall(ci);
        Log.info(ic.getUsers().size() + " ic.getUsers()");

        TLVector<TLImportedContact> listIC = ic.getImported();
        for (TLImportedContact c : listIC) {
            System.out.println("TLImportedContact" + c.getUserId());
        }
        System.out.println(listIC.isEmpty());

        return listIC.size() == 0 ? 0 : listIC.get(0).getUserId();
    }

    /** Обновление имени и фамилии контакта путем импорта этого контакта с новыми данными и заменой старого*/
    public User contactUpdateContactInfo(int id, String phoneNumber, String newFirstName, String newLastName) throws IOException {
        newFirstName = newFirstName.replaceAll("\\\\s+", " ");
        newLastName = newLastName.replaceAll("\\\\s+", " ");

//        this.api = getTelegramApi(bridge);
        TLVector<TLInputContact> v = new TLVector<>();
        v.add(new TLInputContact(id, phoneNumber, newFirstName, newLastName));

        TLRequestContactsImportContacts ci = new TLRequestContactsImportContacts(v, true);
        TLImportedContacts ic = this.api.doRpcCall(ci);
        Log.info(ic.getUsers().size() + " ic.getUsers()");


        TLVector<TLImportedContact> listIC = ic.getImported();
        for (TLImportedContact c : listIC) {
            System.out.println("TLImportedContact" + c.getUserId());
        }
        System.out.println(listIC.isEmpty());

        int importedId = listIC.size() == 0 ? 0 : listIC.get(0).getUserId();
        if (importedId != 0) {
            contactsUpdateContacts();
            return contactsGetContacts().get(importedId);
        }
        return null;
    }

    /** Удаление контакта */
    public boolean contactDeleteContact(int userId) {
        boolean result = false;
        try {
            result = bridge.contactsDeleteContact(userId);
            contactsUpdateContacts();
            contacts = contactsGetContacts();
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        if (result) {
            dialogList.remove(userId);
            contacts.remove(userId);
        }
        return result;
    }

    /** Получение контакта из локального хранилища по номеру телефона */
    public ArrayList<Dialog> dialogGetDialogs() {
        ArrayList<Dialog> dialogs = null;
            try {
                dialogs = bridge.messagesGetDialogs(0, Integer.MAX_VALUE, 0);
            } catch (IOException e) {
                e.printStackTrace();
                return dialogs;
            }
        return dialogs;
    }

    public void dialogAddDialogToLocal(int userId, ContactListItem dialog) {
        dialogList.put(userId, dialog);
    }

    /** Возвращает элемент ContactListItem по идентификатору контакта*/
    public ContactListItem dialogGetDialogByUserId(int userId) {
        return dialogList.get(userId);
    }

    public LinkedHashMap<Integer, ContactListItem> dialogGetDialogList() {
        return dialogList;
    }

    /** Обновить имя в списке диалогов */
    public void dialogUpdateContactNameLocal(int contactId) {
        contactsUpdateContacts();
        UserContact userContact = contactsGetContacts().get(contactId);
        ContactListItem replacedItem = dialogList.get(contactId);
        dialogList.put(contactId, new ContactListItem(userContact, replacedItem.getMessage()));
    }

    /** Поиск сообщения по ID */
    public ArrayList<Message> messageGetMessagesById(ArrayList<Integer> ids) {
        ArrayList<Message> messages = null;
        try {
            messages = bridge.messagesGetMessages(ids);
        } catch (IOException e) {
            e.printStackTrace();
            return messages;
        }
        return messages;
    }

    /** Измененный метод класса TelegramApiBridge с добавлением ID контакта.
     * @see TelegramApiBridge#messagesGetHistory(int, int, int) */

    public ArrayList<Message> messageUpdateMessageHistoryByUserID(int userId) throws IOException {
        TLRequestMessagesGetHistory request = new TLRequestMessagesGetHistory(new TLInputPeerContact(userId), 0, Integer.MAX_VALUE, 50);
        TLVector<TLAbsMessage> tlAbsMessages =  getTelegramApi(bridge).doRpcCall(request).getMessages();
        ArrayList<Message> messages = new ArrayList<>();
        Iterator var7 = tlAbsMessages.iterator();

        while (var7.hasNext()) {
            TLAbsMessage tlMessage = (TLAbsMessage) var7.next();
            messages.add(new Message(tlMessage));
        }
        return messages;
    }

    /** Получение связного списка с историей сообщений текущего пользователя */
    public synchronized LinkedList<Message> messageGetMessageHistoryByUserID(int userId) {
        LinkedList<Message> history = contactsMessageHistory.get(userId);
        if (history == null) {
                try {
                    ArrayList<Message> messages = messageUpdateMessageHistoryByUserID(userId);
                    history = new LinkedList<>(messages);
                    contactsMessageHistory.put(userId, history);
                } catch (IOException e) {
                    e.printStackTrace();
                    return history;
                }
        }
        return history;
    }

    /** Отправка сообщения */
    public MessagesSentMessage messageSendMessage(int userId, String message, long randomId) {
        MessagesSentMessage messageStatus = null;
        try {

            messageStatus = bridge.messagesSendMessage(userId, message, randomId);
            // Создание локального сообщения
            TLPeerUser tlPeerUser = new TLPeerUser(getSelfUser().getId());
            TLMessage tlMessage = new TLMessage(messageStatus.getId(), userId, tlPeerUser, true, true, messageStatus.getDate(), message, null);
            Message msg = new Message(tlMessage);

            // добавляем локальное сообщение в хранилище
            model.messageAddMessageToLocal(userId, msg);
        } catch (IOException e) {
            e.printStackTrace();
            return messageStatus;
        }
        return messageStatus;
    }

    /** Создание запроса на поиск сообщений с ограничением
     * результатов в количестве 50 сообщений
     * @return список элементов готовый к вставке в DefaultListModel контактов*/

    public ArrayList<ContactListItem> messageSearchMessage(String searchQuery) throws IOException {
        TLRequestMessagesSearch search = new TLRequestMessagesSearch(
                new TLInputPeerEmpty(), searchQuery, new TLInputMessagesFilterEmpty(), -1, DateUtils.getDateInt(), 0, Integer.MAX_VALUE, 50);
        TLAbsMessages absMessages = this.api.doRpcCall(search);

        // Собираем сообщения
        TLVector<TLAbsMessage> tlAbsMessages = absMessages.getMessages();
        ArrayList<Message> messages = new ArrayList<>();
        Iterator var7 = tlAbsMessages.iterator();
        while (var7.hasNext()) {
            TLAbsMessage tlMessage = (TLAbsMessage) var7.next();
            messages.add(new Message(tlMessage));
        }
        // заполняем результат
        ArrayList<ContactListItem> result = new ArrayList<>();
        for (Message msg : messages) {
            int contactId;
            if (msg.isOut()) {
                contactId = msg.getToId();
            } else {
                contactId = msg.getToId();
            }
            UserContact contact = contacts.get(contactId);
            if (contact != null) {
                ContactListItem listItem = new ContactListItem(contact, msg);
                result.add(listItem);
            }
        }
        return result;
    }

    /** Сохранение сообщения в локальное хранение */
    public synchronized void messageAddMessageToLocal(int contactId, Message msg) {
        LinkedList<Message> messages = messageGetMessageHistoryByUserID(contactId);
        messages.addFirst(msg);
        ContactListItem contactListItem = dialogList.get(contactId);
        if (contactListItem == null) {
            UserContact contact = contactsGetContacts().get(contactId);
            dialogList.put(contactId, new ContactListItem(contact, msg));
        }
    }

    /** Удаление истории сообщений конкретного пользователя с сервера Telegram */
    public void messageDeleteMessageHistory(int userId) throws IOException {
        TLAbsInputPeer peer = new TLInputPeerContact(userId);
        TLRequestMessagesDeleteHistory delete = new TLRequestMessagesDeleteHistory(peer, 0);
        TLAffectedHistory history = this.api.doRpcCall(delete);
    }

    /** Установка обработчика сообщений */
    public void setMessageHandler(IncomingMessageHandler handler) {
        bridge.setIncomingMessageHandler(handler);
    }


    public ArrayList<User> userGetUsersById(ArrayList<Integer> ids) {
        ArrayList<User> users = null;
        try {
            users = bridge.usersGetUsers(ids);
        } catch (IOException e) {
            e.printStackTrace();
            return users;
        }
        return users;

    }

    /** Обновление имени и фамилии своего профиля */
    public User updateProfileInfo(String newFirstName, String newLastName) {
        newFirstName = newFirstName.replaceAll("\\\\s+", " ");
        newLastName = newLastName.replaceAll("\\\\s+", " ");

        User updatedUser;
        try {
            updatedUser = bridge.accountUpdateUsername(newFirstName, newLastName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Пользователь не обновлен");
            return selfUser;
        }
        selfUser = updatedUser;
        System.err.println("Обновленный пользователь: " + updatedUser);
        return updatedUser;
    }











}
