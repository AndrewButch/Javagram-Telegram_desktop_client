package View.Forms;

import Presenter.IPresenter;
import Presenter.PrChat;
import Utils.MyMouseWheelScroller;
import View.Forms.Modal.ViewAddContact;
import View.Forms.Modal.ViewEditContact;
import View.Forms.Modal.ViewEditProfile;
import View.IView;
import View.ListItem.ContactItem;
import View.ListItem.MessageItem;
import View.ListRenderer.ListCellRendererContact;
import View.ListRenderer.ListCellRendererMessage;
import View.Resources;
import View.WindowManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class ViewChat implements IView {
    private PrChat presenter;

    private ListCellRendererContact contactListRenderer; // визуализатор списка контактов
    private ListCellRendererMessage messageListRenderer; // визуализатор списка сообщений

    private DefaultListModel<ContactItem> modelContacts;
    private DefaultListModel<MessageItem> modelMessages;


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
    private JList<ContactItem> contactsJList;

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
        PrChat presenter = new PrChat(this);
        setPresenter(presenter);
        WindowManager.setContentView(this);
        setupBorders();
        setListeners();
        contactListRenderer = new ListCellRendererContact(presenter);
        messageListRenderer = new ListCellRendererMessage();
        contactsJList.setCellRenderer(contactListRenderer);
        messagesJList.setCellRenderer(messageListRenderer);
    }

    private void createUIComponents() {
        // Получение ресурсов изображений
        loadImages();
        rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setSize(900, 600);
        setupLists();
        setupLayerdPane();
        setupIcons();
    }

    /** --------- Методы инициализации ---------*/

    private void loadImages(){
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

    private void setupLists() {
        // Создание JScrollPane без ползунка для управления мышью
        // Для списка контаков
        contactsListScrollPane = new JScrollPane();
        contactsListScrollPane.addMouseWheelListener(new MyMouseWheelScroller());
        // Для списка сообщений
        messageListScrollPane = new JScrollPane();
        messageListScrollPane.addMouseWheelListener(new MyMouseWheelScroller());
    }

    private void setupLayerdPane() {
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
    }

    private void setupIcons() {
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

    @Override
    public void setPresenter(IPresenter presenter) {
        this.presenter = (PrChat) presenter;
    }

    private void setupBorders() {
        contactsJList.setBorder(null);
        contactJPanel.setBorder(BorderFactory.createMatteBorder(0, 1,1, 0, new Color(237, 237, 237)));
        messageTextField.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        searchContactTextField.setBorder(null);
        contactsListScrollPane.setBorder(null);
        messageListScrollPane.setBorder(null);
    }

    private void setListeners() {
        // Обработчик нажатия на кнопку отправки сообщений
        sendMessageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Send Message");
                presenter.sendMessage(contactListRenderer.getSelectedItem().getUser().getId(), messageTextField.getText());
                messageListScrollPane.getVerticalScrollBar().setValue(messageListScrollPane.getVerticalScrollBar().getMaximum());
            }
        });

        // Обработчик нажатия на поле ввода сообщения
        messageTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Send Message");
                //presenter.sendMessage();
                //verticalBar.setValue(verticalBar.getMaximum());
            }
        });

        // Обработчик фокуса для поля ввода сообщения
        messageTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("Текст сообщения".equals(messageTextField.getText())) {
                    messageTextField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if ("".equals(messageTextField.getText())) {
                    messageTextField.setText("Текст сообщения");
                }
            }
        });

        // Обработчик нажатия на кнопку редактирования профиля пользователя
        getUserSettingsBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditUserProfileView();
            }
        });

        // Обработчик нажатия на кнопку редактирования профиля контакта
        getContactEditBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditContactView();
            }
        });

        // Обработчик нажатия на кнопку добавления контакта
        getAddContactBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddContactView();
            }
        });
    }

    /** --------- Getters ------------ */
    @Override
    public JLayeredPane getRootPanel() {
        return layeredRootPane;
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

    public JLabel getUserNameLabel() {
        return userNameLabel;
    }

    public JLabel getContactNameLable() {
        return contactNameLable;
    }

    public JList<ContactItem> getContactsJList() {
        return contactsJList;
    }

    public JList<MessageItem> getMessagesJList() {
        return messagesJList;
    }

    public ListCellRendererContact getContactListRenderer() {
        return contactListRenderer;
    }

    public ListCellRendererMessage getMessageListRenderer() {
        return messageListRenderer;
    }

    public DefaultListModel<ContactItem> getModelContacts() {
        return modelContacts;
    }

    public DefaultListModel<MessageItem> getModelMessages() {
        return modelMessages;
    }

    public ViewAddContact getAddContact() {
        return addContact;
    }

    /** --------- Методы показа модальных окон ---------*/

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

    /** --------- Методы управления --------- */
    
    // Очистить поле ввода сообщения
    public void clearMessageTextField() {
        messageTextField.setText("");
    }

    public void showDialogs(DefaultListModel<ContactItem> model) {
        modelContacts = model;
        contactsJList.setModel(model);
        contactsJList.revalidate();
        contactsJList.repaint();
    }

    public void showMessages(DefaultListModel<MessageItem> model) {
        modelMessages = model;
        messagesJList.setModel(model);
        messagesJList.revalidate();
        messagesJList.repaint();
    }

    public void updateContactLabel(String userName) {
        contactNameLable.setText(userName);
    }
}

