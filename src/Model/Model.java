package Model;

import View.ListItem.ContactListItem;
import com.oracle.tools.packager.Log;
import org.javagram.TelegramApiBridge;
import org.javagram.handlers.IncomingMessageHandler;
import org.javagram.response.AuthAuthorization;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.AuthSentCode;
import org.javagram.response.MessagesSentMessage;
import org.javagram.response.object.*;
import org.telegram.api.*;
import org.telegram.api.contacts.TLImportedContacts;
import org.telegram.api.engine.TelegramApi;
import org.telegram.api.messages.TLAbsMessages;
import org.telegram.api.messages.TLAffectedHistory;
import org.telegram.api.requests.*;
import org.telegram.tl.TLVector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class Model {
    private final int request_interval = 5000;
    private final String hostAddr = "149.154.167.40:443";
    private final int appId = 568751;
    private final String appHash = "ec1d629d0855caa5425a9c83cdc5925d";
    TelegramApi api;
    TelegramApiBridge  bridge;

//    private FakeTelegramBridge bridge;

    private AuthAuthorization authorization;
    private AuthCheckedPhone authCheckedPhone;
    private AuthSentCode authSendCode;

    private User selfUser;
    private static Model model;
    private String phone;
    private String smsCode;
    private State state;

    // Хранилище сообщений
    private volatile HashMap<Integer, LinkedList<Message>> contactsMessageHistory; // <contactId, LinkedList<Messages>
    // Список контактов
    private volatile HashMap<Integer, UserContact> contacts; // <contactId, UserContact>
    // Список диалогов
    private volatile HashMap<Integer, ContactListItem> dialogList = new HashMap<>(); // <contactId, ContactListItem>


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
//        bridge = new FakeTelegramBridge();
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
        } catch (NullPointerException e) {
            e.printStackTrace();
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

    /** Получение контактов из Telegram*/
    public synchronized HashMap<Integer, UserContact> getContacts(boolean forceUpdate) {
        if (contacts == null || forceUpdate) {

            try {
                ArrayList<UserContact> conts = bridge.contactsGetContacts();
                contacts = new HashMap<>();
                for (UserContact cont : conts) {
                    contacts.put(cont.getId(), cont);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return contacts;
            }
        }
        return contacts;
    }

    public ArrayList<ContactStatus> getContactStatuses() {
        ArrayList<ContactStatus> statuses = null;
        try {
            statuses = bridge.contactsGetStatuses();
        } catch (IOException e) {
            e.printStackTrace();
            return statuses;
        }
        return statuses;
    }

    /** Получение контакта из локального хранилища по номеру телефона */
    public ArrayList<Dialog> getDialogs() {
        ArrayList<Dialog> dialogs = null;
            try {
                dialogs = bridge.messagesGetDialogs(0, Integer.MAX_VALUE, 0);
            } catch (IOException e) {
                e.printStackTrace();
                return dialogs;
            }
        return dialogs;
    }

    public void addDialog(int userId, ContactListItem dialog) {
        dialogList.put(userId, dialog);
    }

    public ContactListItem getDialogByUserId(int userId) {
        return dialogList.get(userId);

    }


    public ArrayList<Message> getMessagesById(ArrayList<Integer> ids) {
        ArrayList<Message> messages = null;
        try {
            messages = bridge.messagesGetMessages(ids);
        } catch (IOException e) {
            e.printStackTrace();
            return messages;
        }
        return messages;
    }

    public ArrayList<User> getUsersById(ArrayList<Integer> ids) {
        ArrayList<User> users = null;
        try {
            users = bridge.usersGetUsers(ids);
        } catch (IOException e) {
            e.printStackTrace();
            return users;
        }
        return users;

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

    /**
     * Это фактически чуть-чуть измененный метод класса TelegramApiBridge. Сравните их - увидите,
     * что сюда передаем еще параметр контакта целевого (его Id = userContact.getId()).
     * @param userId
     * @param offset
     * @param maxId
     * @param limit
     * @return
     * @throws IOException
     */
    private ArrayList<Message> getMessageHistoryByUserID(int userId, int offset, int maxId, int limit) throws IOException {
        TLRequestMessagesGetHistory request = new TLRequestMessagesGetHistory(new TLInputPeerContact(userId), offset, maxId, limit);
        TLVector<TLAbsMessage> tlAbsMessages = ((TLAbsMessages) getTelegramApi(bridge).doRpcCall(request)).getMessages();
        ArrayList<Message> messages = new ArrayList<>();
        Iterator var7 = tlAbsMessages.iterator();

        while (var7.hasNext()) {
            TLAbsMessage tlMessage = (TLAbsMessage) var7.next();
            messages.add(new Message(tlMessage));
        }
        return messages;
    }

//    private synchronized ArrayList<Message> getMessageHistoryByUserID(int userId, int offset, int maxId, int limit) throws IOException {
//        ArrayList<Message> foundedMsg = new ArrayList();
//        for (Message msg : bridge.messageGetHistory()) {
//            if (msg.getFromId() == userId || msg.getToId() == userId) {
//                foundedMsg.add(msg);
//            }
//        }
//        return foundedMsg;
//    }

    public synchronized LinkedList<Message> getMessageHistoryByUserID(int userId, boolean forceUpdate) {
        LinkedList<Message> history = contactsMessageHistory.get(userId);
        if (history == null || forceUpdate) {
                try {
                    ArrayList<Message> messages = getMessageHistoryByUserID(userId, 0, Integer.MAX_VALUE, 50);
                    history = new LinkedList<>(messages);
                    contactsMessageHistory.put(userId, history);
                } catch (IOException e) {
                    e.printStackTrace();
                    return history;
                }
        }
        return history;
    }

    public MessagesSentMessage sendMessage(int userId, String message, long randomId) {
        MessagesSentMessage messageStatus = null;
        try {

            messageStatus = bridge.messagesSendMessage(userId, message, randomId);
            // Создание локального сообщения
            TLPeerUser tlPeerUser = new TLPeerUser(getSelfUser().getId());
            TLMessage tlMessage = new TLMessage(messageStatus.getId(), userId, tlPeerUser, true, true, messageStatus.getDate(), message, null);
            Message msg = new Message(tlMessage);

            // добавляем локальное сообщение в хранилище
            model.addMessageToLocal(userId, msg);
        } catch (IOException e) {
            e.printStackTrace();
            return messageStatus;
        }
        return messageStatus;
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

    public void setMessageHandler(IncomingMessageHandler handler) {
        bridge.setIncomingMessageHandler(handler);
    }


    public synchronized void addMessageToLocal(int contactId, Message msg) {
        LinkedList<Message> messages = getMessageHistoryByUserID(contactId, false);
        messages.addFirst(msg);
        ContactListItem contactListItem = dialogList.get(contactId);
        if (contactListItem != null) {
            contactListItem.incrementUnread();
        } else {
            UserContact contact = model.getContacts(false).get(contactId);
            dialogList.put(contactId, new ContactListItem(contact, msg));
        }
    }


    public int sendInvite(String phoneNumber, String firstName, String lastName) throws IOException {
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

        int importedId = listIC.size() == 0 ? 0 : listIC.get(0).getUserId();

        return importedId;
    }

    public User updateProfileInfo(String newFirstName, String newLastName) throws IOException {
        newFirstName = newFirstName.replaceAll("\\\\s+", " ");
        newLastName = newLastName.replaceAll("\\\\s+", " ");

        User updatedUser = null;
        updatedUser = bridge.accountUpdateUsername(newFirstName, newLastName);
        selfUser = updatedUser;
        System.err.println("Обновленный пользователь: " + updatedUser);

        return updatedUser;
    }

    public User updateContactInfo(int id, String phoneNumber, String newFirstName, String newLastName) throws IOException {
        newFirstName = newFirstName.replaceAll("\\\\s+", " ");
        newLastName = newLastName.replaceAll("\\\\s+", " ");

//        this.api = getTelegramApi(bridge);
        TLVector<TLInputContact> v = new TLVector();
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
            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(importedId);
            ArrayList<User> users = getUsersById(ids);
            return users.get(0);
        }
        return null;
    }

    public boolean deleteContact(int userId) {
        boolean result = false;
        try {
            result = bridge.contactsDeleteContact(userId);
            contacts = getContacts(true);
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

    public ArrayList<Message> messagesSearch(String searchQuery) throws IOException {
//        TLRequestMessagesGetHistory request = new TLRequestMessagesGetHistory(new TLInputPeerContact(userId), offset, maxId, limit);
        TLRequestMessagesSearch search = new TLRequestMessagesSearch(
                new TLInputPeerEmpty(),searchQuery, new TLInputMessagesFilterEmpty(), -1, -1, 0, Integer.MAX_VALUE, 50);
        TLAbsMessages absMessages = this.api.doRpcCall(search);
        TLVector<TLAbsMessage> tlAbsMessages = absMessages.getMessages();
        ArrayList<Message> messages = new ArrayList<>();
        Iterator var7 = tlAbsMessages.iterator();

        while (var7.hasNext()) {
            TLAbsMessage tlMessage = (TLAbsMessage) var7.next();
            messages.add(new Message(tlMessage));
        }
        return messages;
    }

    public void updateLocalDialog(int contactId) {
        UserContact userContact = model.getContacts(true).get(contactId);
        ContactListItem replacedItem = dialogList.get(contactId);
        dialogList.put(contactId, new ContactListItem(userContact, replacedItem.getMessage(), replacedItem.getUnreadCount()));
    }

    public void deleteContactMessageHistory(int userId) throws IOException {
        TLAbsInputPeer peer = new TLInputPeerContact(userId);
        TLRequestMessagesDeleteHistory delete = new TLRequestMessagesDeleteHistory(peer, 0);
        TLAffectedHistory history = this.api.doRpcCall(delete);

    }


}
