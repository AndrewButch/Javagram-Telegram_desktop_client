package Model;

import FakeData.FakeTelegramBridge;
import org.javagram.TelegramApiBridge;
import org.javagram.handlers.IncomingMessageHandler;
import org.javagram.response.AuthAuthorization;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.AuthSentCode;
import org.javagram.response.MessagesSentMessage;
import org.javagram.response.object.*;
import org.telegram.api.TLAbsMessage;
import org.telegram.api.TLInputPeerContact;
import org.telegram.api.auth.TLSentCode;
import org.telegram.api.engine.AppInfo;
import org.telegram.api.engine.TelegramApi;
import org.telegram.api.messages.TLAbsMessages;
import org.telegram.api.requests.TLRequestAuthSendCall;
import org.telegram.api.requests.TLRequestAuthSendCode;
import org.telegram.api.requests.TLRequestMessagesGetHistory;
import org.telegram.tl.TLBool;
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
//    TelegramApiBridge  bridge;

    private FakeTelegramBridge bridge;

    private AuthAuthorization authorization;
    private AuthCheckedPhone authCheckedPhone;
    private AuthSentCode authSendCode;
    private boolean loggedIn;         // Флаг логина

    private User selfUser;
    private static Model model;
    private String phone;
    private String smsCode;
    private State state;
    private HashMap<Integer, LinkedList<Message>> contactsMessageHistory; // Int - userID
    private HashMap<Integer, User> contacts; // Int - userID

    private Model() {
//        while (bridge == null) {
//            try {
//                bridge = new TelegramApiBridge(hostAddr, appId, appHash);
//            } catch (IOException e) {
//                e.printStackTrace();
//                try {
//                    Thread.sleep(request_interval);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
        bridge = new FakeTelegramBridge();
        contactsMessageHistory = new HashMap<>();
    }



    public static Model getInstance() {
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

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void logOut() {
        if (bridge != null)  {
            try {
                bridge.authLogOut();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        loggedIn = false;
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
        if (authorization != null) {
            return authorization.getUser();
        }
        return null;
    }

    public ArrayList<UserContact> getContacts() {
        ArrayList<UserContact> contacts = null;
        do {
            try {
                contacts = bridge.contactsGetContacts();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(request_interval);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        } while (contacts == null);
        return contacts;
    }



    public ArrayList<Dialog> getDialogs() {
        ArrayList<Dialog> dialogs = null;
        do {
            try {
                dialogs = bridge.messagesGetDialogs(0, Integer.MAX_VALUE, 0);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(request_interval);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }while (dialogs == null);
        return dialogs;
    }

    public ArrayList<Message> getMessagesById(ArrayList<Integer> ids) {
        ArrayList<Message> messages = null;
        do {
            try {
                messages = bridge.messagesGetMessages(ids);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(request_interval);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        } while (messages == null);
        return messages;
    }

    public ArrayList<User> getUsersById(ArrayList<Integer> ids) {
        ArrayList<User> users = null;
        do {
            try {
                users = bridge.usersGetUsers(ids);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(request_interval);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        } while (users == null);
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
//    private ArrayList<Message> getMessageHistoryByUserID(int userId, int offset, int maxId, int limit) throws IOException {
//        TLRequestMessagesGetHistory request = new TLRequestMessagesGetHistory(new TLInputPeerContact(userId), offset, maxId, limit);
//        TLVector<TLAbsMessage> tlAbsMessages = ((TLAbsMessages) getTelegramApi(bridge).doRpcCall(request)).getMessages();
//        ArrayList<Message> messages = new ArrayList();
//        Iterator var7 = tlAbsMessages.iterator();
//
//        while (var7.hasNext()) {
//            TLAbsMessage tlMessage = (TLAbsMessage) var7.next();
//            messages.add(new Message(tlMessage));
//        }
//        return messages;
//    }

    private LinkedList<Message> getMessageHistoryByUserID(int userId, int offset, int maxId, int limit) throws IOException {
        LinkedList<Message> foundedMsg = new LinkedList();
        for (Message msg : bridge.messageGetHistory()) {
            if (msg.getFromId() == userId || msg.getToId() == userId) {
                foundedMsg.add(msg);
            }
        }
        return foundedMsg;
    }

    public LinkedList<Message> getMessageHistoryByUserID(int userId) {
        if (contactsMessageHistory.get(userId) == null) {
            LinkedList<Message> history = null;
            do {
                try {
                    history = getMessageHistoryByUserID(userId, 0, Integer.MAX_VALUE, 50);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(request_interval);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            } while (history == null);
            return history;
        }
        return contactsMessageHistory.get(userId);
    }

//    public MessagesSentMessage sendMessage(int userId, String message, long randomId) {
//        MessagesSentMessage messageStatus = null;
//        try {
//
//            messageStatus = bridge.messagesSendMessage(userId, message, randomId);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return messageStatus;
//    }

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

    public State getState() {
        if (state == null) {
            try {
                state = bridge.updatesGetState();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return state;
    }

    public void addMessage(int contactId, Message msg) {
        contactsMessageHistory.get(contactId).add(0, msg);
    }


//    public AuthSentCode sendAppCode(String phoneNumber) throws IOException {
//        TelegramApi api = getTelegramApi(bridge);
//        TLRequestAuthSendCode sendCode = new TLRequestAuthSendCode(phoneNumber, 5, this.appId, this.appHash, "ru");
//        TLSentCode sentCode = (TLSentCode)api.doRpcCallNonAuth(sendCode);
//        String phoneCodeHash = sentCode.getPhoneCodeHash();
//        return new AuthSentCode(sentCode.getPhoneRegistered(), phoneCodeHash);
//    }
//    public TLBool sendCall(String phoneNumber, String phoneHash) throws IOException {
//        TelegramApi api = getTelegramApi(bridge);
//        TLRequestAuthSendCall sendCall = new TLRequestAuthSendCall(phoneNumber, phoneHash);
//        TLBool sentCode = (TLBool) api.doRpcCallNonAuth(sendCall);
//        return sentCode;
//    }
}
