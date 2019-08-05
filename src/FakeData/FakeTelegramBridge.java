package FakeData;

import org.javagram.TelegramApiBridge;
import org.javagram.handlers.IncomingMessageHandler;
import org.javagram.response.AuthAuthorization;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.AuthSentCode;
import org.javagram.response.MessagesSentMessage;
import org.javagram.response.object.*;
import org.telegram.api.*;
import org.telegram.api.auth.TLAuthorization;
import org.telegram.api.engine.TelegramApi;
import org.telegram.api.help.TLInviteText;
import org.telegram.api.messages.TLAbsMessages;
import org.telegram.api.messages.TLAffectedHistory;
import org.telegram.api.messages.TLSentMessage;
import org.telegram.api.requests.*;
import org.telegram.api.updates.TLAbsDifference;
import org.telegram.api.updates.TLDifference;
import org.telegram.api.updates.TLState;
import org.telegram.tl.TLIntVector;
import org.telegram.tl.TLVector;

import java.io.IOException;
import java.util.*;

/**
 * Класс имитирующий подключения через TelegramApiBridge
 * Имитирует необходимые методы
 */

public class FakeTelegramBridge {
    private TelegramApiBridge bridge;

    private static final String SMS_CORRECT = "11111";
    private static final String PHONE_REGISTRED = "79533749628";
    private final int FAKE_CONTACT_COUNT = 20;

    private FakeSelfUser selfUser;
    private final int SELF_ID = 5;
    private ArrayList<UserContact> userContacts;
    private int[] dialogsId = {10, 15, 19, 4,  0, 2, 3,  6, 7, 9,  13, 14,  16, 17};
    private ArrayList<ContactStatus> statuses;

    private SortedMap<Integer, Message> messages;
    private volatile int messageID = 0;

    private ArrayList<Dialog> dialogs;


    private String langCode = "ru";
    private TelegramApi api;
    private Random random;
    private IncomingMessageHandler incomingMessageHandler;

    public FakeTelegramBridge() {
        this.bridge = null;
        random = new Random();

        // Инициализаця контактов
        userContacts = new ArrayList<>();
        for (int i = 0; i < FAKE_CONTACT_COUNT; i++) {
            // 5-ым по счету передаем SelfUser
            if (i == SELF_ID) {
                selfUser = new FakeSelfUser(i);
                userContacts.add(selfUser);
            }
            userContacts.add(new FakeUserContact(i, "123456" + i));
        }
        // Инициализаця статусов контактов
        statuses = new ArrayList<>();
        for (int i = 0; i < FAKE_CONTACT_COUNT; i++) {
            statuses.add(new ContactStatus(new TLContactStatus(userContacts.get(i).getId(), random.nextInt(5 * 100))));
        }
        // Инициализация фейковых сообщений для всех контактов
        messages = new TreeMap<>();
        initMessages();
    }

    public AuthSentCode authSendCode(String phoneText) throws IOException{
        System.out.println("Отправлено СМС-сообщение: 11111 - верное");
        if (PHONE_REGISTRED.equals(phoneText))
            return new AuthSentCode(true, "12345");
        else
            return new AuthSentCode(false, "12345");
    }

    public AuthCheckedPhone authCheckPhone(String phoneText)throws IOException {
        return new AuthCheckedPhone(true, false);
    }

    public AuthAuthorization authSignIn (String smsCode)throws IOException {

        if (!SMS_CORRECT.equals(smsCode)) {
            throw new IOException("INCORRECT SMS CODE");
        }
        UserContact u = userContacts.get(SELF_ID);
        TLAbsUser absUser = new TLUserContact(u.getId(), u.getFirstName(), u.getLastName(), u.getAccessHash(), u.getPhone(), null, null);
        return new AuthAuthorization(new TLAuthorization(100, absUser));
    }

    public AuthAuthorization authSignUp (String smsCode, String firstName, String lastName)throws IOException{

        if (!SMS_CORRECT.equals(smsCode)) {
            throw new IOException("INCORRECT SMS CODE");
        }
        UserContact u = userContacts.get(SELF_ID);
        TLAbsUser absUser = new TLUserContact(u.getId(), u.getFirstName(), u.getLastName(), u.getAccessHash(), u.getPhone(), null, null);
        return new AuthAuthorization(new TLAuthorization(100, absUser));
    }

    public void authLogOut() throws IOException{

    }


    /** Done */
    public boolean authSendInvites(ArrayList<String> phoneNumbers, String message) throws IOException {
        Random random = new Random();
//        boolean result = random.nextBoolean();
        boolean result = true;
        if(result) {
            userContacts.add(new FakeUserContact(random.nextInt() + 20, phoneNumbers.get(0)));
        }
        return result;
    }
    /** Done */
    public User accountUpdateProfile(String firstName, String lastName) throws IOException {
        FakeSelfUser user = new FakeSelfUser(selfUser.getId(), firstName, lastName, selfUser.getPhone(), null, null, false);
        return user;
    }
    /** ??? Done */
    public boolean accountUpdateStatus(boolean offline) throws IOException {
        Random random = new Random();
        return random.nextBoolean();
    }
    /** Done */
    public boolean contactsDeleteContact(int userId) throws IOException {
        for (int i = 0; i < userContacts.size(); i++) {
            if (userContacts.get(i).getId() == userId) {
                userContacts.remove(i);
                break;
            }
        }
        return true;
    }

    /** Done */
    public ArrayList<UserContact> contactsGetContacts()throws IOException {
        return userContacts;
    }

    /** Done */
    public ArrayList<ContactStatus> contactsGetStatuses() throws IOException {
        return statuses;
    }

    /** Done */
    public MessagesSentMessage messagesSendMessage(int userId, String message, long randomId) throws IOException {
        this.messages.put((int)randomId, new Message(
                new TLMessage((
                        (int) randomId), selfUser.getId(), new TLPeerUser(userId),
                        true, true, Calendar.getInstance().getTime().getDate(),
                        message, null )));

        MessagesSentMessage msm = new MessagesSentMessage(new TLSentMessage(
                (int)randomId , Calendar.getInstance().getTime().getDate(), 1, 1));
        return msm;
    }
//    public MessagesSentMessage messagesSendMessage(int userId, String message, long randomId) throws IOException {
//        TLInputPeerContact peerContact = new TLInputPeerContact(userId);
//        TLRequestMessagesSendMessage request = new TLRequestMessagesSendMessage(peerContact, message, randomId);
//        TLSentMessage sentMessage = (TLSentMessage)this.api.doRpcCall(request);
//        return new MessagesSentMessage(sentMessage);
//    }

    /** Неготово */
    public boolean messagesSetTyping(int userId, boolean isTyping) throws IOException {
        // TODO
//        TLInputPeerContact peerContact = new TLInputPeerContact(userId);
//        TLRequestMessagesSetTyping request = new TLRequestMessagesSetTyping(peerContact, isTyping);
        return false;
    }

    /** Done */
    public ArrayList<Message> messagesGetMessages(ArrayList<Integer> messageIds) throws IOException {
        ArrayList<Message> fondedMessages = new ArrayList();
        // Перебор заданных ID
        for (Integer id : messageIds) {
            // Перебор хэш-мап с сообщениями
            for (Map.Entry<Integer, Message> entry : messages.entrySet()) {
                if (entry.getValue().getId() == id) {
                    fondedMessages.add(entry.getValue());
                }
            }
        }
        return fondedMessages;
    }
    /** Done */
    public ArrayList<Dialog> messagesGetDialogs(int offset, int maxId, int limit) throws IOException {
        dialogs = new ArrayList();
        for (int id : dialogsId) {
            int size = messages.size();
            while (dialogs.size() < dialogsId.length) {
                Message msg = messages.get(random.nextInt(size));
                if (msg.getFromId() == id || msg.getToId() == id) {
                    dialogs.add(new Dialog(new TLDialog(
                            new TLPeerUser(id),
                            msg.getId(),
                            random.nextInt(5))));
                    break;
                }
            }
//            for (Map.Entry<Integer, Message> entry : messages.entrySet()) {
//                if (entry.getValue().getFromId() == id || entry.getValue().getToId() == id) {
//                    dialogs.add(new Dialog(new TLDialog(
//                            new TLPeerUser(id),
//                            entry.getValue().getId(),
//                            random.nextInt(5))));
//                    break;
//                }
//            }
        }
        return dialogs;
    }

    /** TODO */
    public ArrayList<Message> messageGetSearch(String searchStr, ArrayList<Integer> messageIds) throws IOException {
        TLIntVector intVector = new TLIntVector();
        intVector.addAll(messageIds);
        TLRequestMessagesGetMessages request = new TLRequestMessagesGetMessages(intVector);
        TLVector<TLAbsMessage> tlMessages = ((TLAbsMessages)this.api.doRpcCall(request)).getMessages();
        ArrayList<Message> messages = new ArrayList();
        Iterator var7 = tlMessages.iterator();

        while(var7.hasNext()) {
            TLAbsMessage tlMessage = (TLAbsMessage)var7.next();
            Message message = new Message(tlMessage);
            if (message.getMessage().contains(searchStr)) {
                messages.add(message);
            }
        }

        return messages;
    }

    /** TODO */
    public AffectedHistory messagesReadHistory(int offset, int maxId) throws IOException {
        TLRequestMessagesReadHistory request = new TLRequestMessagesReadHistory(new TLInputPeerSelf(), maxId, offset);
        TLAffectedHistory tlAffectedHistory = (TLAffectedHistory)this.api.doRpcCall(request);
        return new AffectedHistory(tlAffectedHistory);
    }
    /** TODO */
    public ArrayList<Message> messagesRecievedMessages(int maxId) throws IOException {
        TLRequestMessagesReceivedMessages request = new TLRequestMessagesReceivedMessages(maxId);
        TLIntVector tlIntVector = (TLIntVector)this.api.doRpcCall(request);
        ArrayList<Integer> ids = new ArrayList();
        int[] var5 = tlIntVector.toIntArray();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            int i = var5[var7];
            ids.add(i);
        }

        return this.messagesGetMessages(ids);
    }
    /** Done */
    public ArrayList<User> usersGetUsers(ArrayList<Integer> userIds) throws IOException {
        ArrayList<User> users = new ArrayList();
        for (Integer id : userIds) {
            for (UserContact user : userContacts) {
                if (id == user.getId()) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    /** Done */
    public UserFull usersGetFullUser(int userId) throws IOException {
        UserContact userFull = null;
        for (UserContact user: userContacts) {
            if (user.getId() == userId) {
                userFull = user;
            }
        }
        TLAbsUser fullUser = new TLUserContact(userFull.getId(), userFull.getFirstName(), userFull.getLastName(),
                userFull.getAccessHash(), userFull.getPhone(), null, null);
        return new UserFull(
                new TLUserFull(fullUser,
                        null, null, null, false,
                        "RealFirstName", "RealSeconName" ));

    }
    /** TODO */
    public State updatesGetState() throws IOException {
        TLState tlState = new TLState(0, 0, 0, 0, 0);
        return new State(tlState);
    }
    /** TODO */
    public Difference updatesGetDifference(int pts, int date, int qts) throws IOException {
        TLRequestUpdatesGetDifference request = new TLRequestUpdatesGetDifference(pts, date, qts);
        TLAbsDifference tlDifference = (TLAbsDifference)this.api.doRpcCall(request);
        return new Difference((TLDifference)tlDifference);
    }
    /** TODO Как определить какого пользователя изменять*/
    public User accountUpdateUsername(String firstName, String lastName) throws IOException {

        TLAbsUser tlAbsUser = new TLUserContact(selfUser.getId(), firstName, lastName, selfUser.getAccessHash(),
                selfUser.getPhone(), null, null);
        selfUser = new FakeSelfUser(selfUser.getId(), firstName, lastName,
                selfUser.getPhone(), null, null, false);
        return new User(tlAbsUser);
    }

    public String helpGetInviteText() throws IOException {
        return "Привет, я хочу с Вами пообщаться";
    }

    public void setIncomingMessageHandler(IncomingMessageHandler handler) {
        this.incomingMessageHandler = handler;
    }


/**
    setTelegramApi(bridge);  //Устанавливаем (берем у TelegramApiBridge) объект TelegramApi.
        for(Message message : getMessageHistory(203041606, 0, Integer.MAX_VALUE, 50)){
        System.out.println(message.getMessage());
    }
*/





    // Создание по 30 файковых сообщений для каждого диалога из dialogsID[]
    private void initMessages() {
        StringBuilder builder = new StringBuilder();
        for (int userId : dialogsId) {
            int to;
            int from;
            boolean out;

            // СОздание по 30 сообщений в обратном порядке для каждого диалога
            for (int i = 30; i > 0; i--) {
                // Случайно определяем кто кому отправляет сообщение
                if (random.nextBoolean()) {
                    to = userId;
                    from = SELF_ID;
                    out = true;
                } else {
                    from = userId;
                    to = SELF_ID;
                    out = false;
                }
                // Находим в списке контактов From ID, берем имя и фамилию, далее формируем первую часть сообщения
                for (UserContact user : userContacts) {
                    if (user.getId() == from) {
                        builder.append("Message from " + user.getFirstName() + " " + user.getLastName() + ".\n");
                        break;
                    }
                }
                // Находим в списке контактов To ID, берем имя и фамилию, далее формируем вторую часть сообщения
                for (UserContact user : userContacts) {
                    if (user.getId() == to) {
                        builder.append("To " + user.getFirstName() + " " + user.getLastName() + ".\n");
                        break;
                    }
                }
                // Завершение сообщения идентификатором
                builder.append("Message id: " + messageID);

                String msg = builder.toString();
                builder = new StringBuilder();

                TLMessage tlMessage = new TLMessage(messageID, from, new TLPeerUser(to), out, random.nextBoolean(),
                        (int)(Calendar.getInstance().getTimeInMillis() / 1000) - messageID * messageID * 60, msg, null);
                this.messages.put(messageID, new Message(tlMessage));
                increaseMessageId();
            }
        }
    }

    public ArrayList<Message> messageGetHistory() {
        ArrayList<Message> history = new ArrayList<>();
        for (Map.Entry<Integer, Message> msg : messages.entrySet()) {
            history.add(msg.getValue());
        }
        return history;
    }

    synchronized public void increaseMessageId() {
        messageID++;
    }

    synchronized public int getMessageID() {
        return messageID;
    }
}
