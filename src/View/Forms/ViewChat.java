package View.Forms;

import Presenter.IPresenter;
import Presenter.PrChat;
import Utils.MyMouseWheelScroller;
import View.Forms.Modal.ViewAddContact;
import View.Forms.Modal.ViewEditContact;
import View.Forms.Modal.ViewEditProfile;
import View.IView;
import View.ListRenderer.*;
import View.ListItem.ContactListItem;
import View.ListItem.MessageItem;
import View.Resources;
import View.WindowManager;
import org.javagram.response.object.Dialog;
import org.javagram.response.object.Message;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewChat implements IView {
    private PrChat presenter;
    private ListCellRendererContact contactCellRenderer; // Собственный визуализатор списка контактов
    private User user;
    private ArrayList<UserContact> contacts; // список конактов
    private ArrayList<Dialog> dialogs;      // диалоги для заполнения списка контактов
    private HashMap<Integer, UserContact> contactsHashMap;

    private JLayeredPane layeredRootPane; // Панель с z-index. В неё кладем все JPanel
    private JPanel rootPanel;
    private JPanel javagramLogoJPanel; // Мини логотип

    // Компоненты пользователя
    private JPanel userJPanel;
    private JPanel userLogoMiniJPanel;
    private JButton userSettingsBtn;
    private JLabel userNameLabel;

    // Компоненты контакта
    private JPanel contactJPanel;
    private JPanel contaclLogoMiniJPanel;
    private JLabel contactNameLable;
    private JButton contactEditBtn;
    private JList<ContactListItem> contactsJList;

    // Компоненты поиска контактов
    private JPanel searchContactsJPanel;
    private JPanel searchIconJPanel;
    private JTextField searchContactTextField;
    private JScrollPane contactsListScrollPane;

    // Компоненты доавления контактов
    private JPanel addContactJPanel;
    private JButton addContactBtn;

    // Компоненты сообщений
    private JPanel sendMessageJPanel;
    private JButton sendMessageBtn;
    private JTextField messageTextField;
    private JScrollPane messageListScrollPane;
    private JList<MessageItem> messagesJList;

    // Изображения
    private BufferedImage logoMicroImg;
    private BufferedImage iconSearch;
    private BufferedImage maskBlueMiniImg;
    private BufferedImage maskWhiteMiniImg;
    private BufferedImage maskGrayOnlineImg;
    private BufferedImage maskWhiteOnlineImg;
    private BufferedImage maskWhiteImg;
    private BufferedImage maskGrayImg;
    private BufferedImage maskDarkGrayBigImg;

    // Модальные view с редактированием профиля, добавлением и редактированием контактов
    private ViewEditProfile editProfile;
    private ViewAddContact addContact;
    private ViewEditContact editContact;

    public ViewChat() {
        setPresenter(new PrChat(this));
        this.user = presenter.getSelfUser();
        this.contacts = presenter.getContacts();
        this.contactsHashMap = new HashMap<>();
        for (UserContact contact : contacts) {
            contactsHashMap.put(contact.getId(), contact);
        }
        // Имя пользователя - метка
        userNameLabel.setText(user.getFirstName() + " " + user.getLastName());

        // Установка информации о пользователе в окне ViewEditProfile
        editProfile.setUserInfo(user.getFirstName(), user.getLastName(), user.getPhone());

        Thread contactListThread = new Thread(new Runnable() {
            @Override
            public void run() {
                setupContactList();
            }
        });
        contactListThread.start();
        WindowManager.setContentView(this);
    }

    /** Создание UI */
    private void createUIComponents() {
        // Получение картинок
        initBufferedImages();

        rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setSize(900, 600);

        // Список контактов и CellRenderer для каждой ячейки
        contactsJList = new JList<>();
        contactsJList.setBorder(null);


        // Панель информации контакта
        contactJPanel = new JPanel();
        contactJPanel.setBorder(BorderFactory.createMatteBorder(0, 1,1, 0, new Color(237, 237, 237)));

        // Поле ввода сообщения
        messageTextField = new JTextField();
        messageTextField.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // Поле поиска
        searchContactTextField = new JTextField();
        searchContactTextField.setBorder(null);

        // JScrollPane без ползунка и управляемы мышью
        // Для списка контаков
        contactsListScrollPane = new JScrollPane();
        contactsListScrollPane.setBorder(null);
        contactsListScrollPane.addMouseWheelListener(new MyMouseWheelScroller());
        // Для списка сообщений
        messageListScrollPane = new JScrollPane();
        messageListScrollPane.setBorder(null);
        messageListScrollPane.addMouseWheelListener(new MyMouseWheelScroller());

        // -----------------------------------------------------------------------
        // Создание панели с Z-index (слои)
        layeredRootPane = new JLayeredPane();
        layeredRootPane.setPreferredSize(new Dimension(900, 600));
        layeredRootPane.setMinimumSize(new Dimension(900, 600));
        layeredRootPane.setSize(new Dimension(900, 600));

        // Добавение rootPanel в самый в z-index на нижний слой
        layeredRootPane.add(rootPanel, JLayeredPane.DEFAULT_LAYER, -1);

        // Форма ViewEditProfile добавляется в z-index на слой выше
        editProfile = new ViewEditProfile();
        layeredRootPane.add(editProfile.getRootPanel(), JLayeredPane.PALETTE_LAYER, -1);

        // Форма ViewEditContact добавляется в z-index на слой выше
        editContact = new ViewEditContact();
        layeredRootPane.add(editContact.getRootPanel(), JLayeredPane.PALETTE_LAYER, -1);

        // Форма ViewAddContact добавляются в z-index на слой выше
        addContact = new ViewAddContact();
        layeredRootPane.add(addContact.getRootPanel(), JLayeredPane.PALETTE_LAYER, -1);

//        loadingForm = new LoadingForm();
//        layeredRootPane.add(loadingForm.getRootPanel(), JLayeredPane.POPUP_LAYER, -1);

        // Обработка события на изменение размера
        layeredRootPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                rootPanel.setSize( e.getComponent().getSize() );
                editProfile.getRootPanel().setSize(e.getComponent().getSize());
                editContact.getRootPanel().setSize(e.getComponent().getSize());
                addContact.getRootPanel().setSize(e.getComponent().getSize());
            }
        });

        //------------------------------------------------------------------
        // Рисование иконки лупы
        searchIconJPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(iconSearch, 0, 0, null);
            }
        };
        // Рисование мини-иконки собеседника
        contaclLogoMiniJPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(maskWhiteMiniImg, 0, 0, null);
            }
        };
        // Рисование иконки Javagram
        javagramLogoJPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(logoMicroImg, 30, 13, null);
            }
        };
        // Рисование мини-иконки юзера
        userLogoMiniJPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(maskBlueMiniImg, 0, 0, null);
            }
        };
    }

    /** Загрузка изображений */
    private void initBufferedImages(){
        logoMicroImg = Resources.getImage(Resources.LOGO_MICRO);
        iconSearch = Resources.getImage(Resources.ICON_SEARCH);
        maskBlueMiniImg = Resources.getImage(Resources.MASK_BLUE_MINI);
        maskWhiteMiniImg = Resources.getImage(Resources.MASK_WHITE_MINI);
        maskGrayOnlineImg = Resources.getImage(Resources.MASK_GRAY_ONLINE);
        maskWhiteOnlineImg = Resources.getImage(Resources.MASK_WHITE_ONLINE);
        maskWhiteImg = Resources.getImage(Resources.MASK_WHITE);
        maskGrayImg = Resources.getImage(Resources.MASK_GRAY);
        maskDarkGrayBigImg = Resources.getImage(Resources.MASK_DARK_GRAY_BIG);
    }
    @Override
    public JLayeredPane getRootPanel() {
        return layeredRootPane;
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        this.presenter = (PrChat) presenter;
    }


    /** Получение диалогов и последних сообщений от пользователей
     *  На основании сообщений, упорядоченных по убыванию даты (делает Telegram)
     *  формируем список контактов
     *  */
    private void setupContactList() {
        // Получение диалогов
        dialogs = presenter.getDialogs();
        // Получение ID последних сообщений из списка диалогов
        ArrayList<Integer> messageIds = new ArrayList<>();
        for (Dialog dialog : dialogs) {
            messageIds.add(dialog.getTopMessage());
        }

        // Получение самих сообщений на основании ID сообщения
        ArrayList<Message> topMessages = presenter.getMessages(messageIds);


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
        ArrayList<User> contactUsers = presenter.getUsers(userIds);
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

        contactsJList.setModel(modelContacts);

        // Передача в визуализатор контактов:
        // - JList для наполнения сообщениями
        // - JLabel для установки имени собеседника
        // - Presenter для получения сообщений
        contactCellRenderer = new ListCellRendererContact();
        contactsJList.setCellRenderer(contactCellRenderer);
        ((ListCellRendererContact) contactsJList.getCellRenderer()).setContacts(messagesJList, contactNameLable, presenter, topMessages );

    }

    public JButton getUserSettingsBtn() {
        return userSettingsBtn;
    }

    public JButton getContactEditBtn() {
        return contactEditBtn;
    }

    public JButton getAddContactBtn() {
        return addContactBtn;
    }

    public JButton getSendMessageBtn() {
        return sendMessageBtn;
    }

    public JTextField getMessageTextField() {
        return messageTextField;
    }

    public JScrollPane getMessageListScrollPane() {
        return messageListScrollPane;
    }

    // Показать модальное окно с добавлением контакта
    public void showAddContactView() {
        addContact.clearFields();
        addContact.getRootPanel().setVisible(true);
        addContact.getPhoneJFormattedText().requestFocus();
    }

    // Показать модальное окно с редактированием своего профиля
    public void showEditUserProfileView() {
        editProfile.getRootPanel().setVisible(true);
    }

    // Показать модальное окно с редактированием контакта
    public void showEditContactView() {
        editContact.getRootPanel().setVisible(true);
    }
}

