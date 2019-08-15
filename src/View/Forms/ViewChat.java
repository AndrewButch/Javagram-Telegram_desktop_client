package View.Forms;

import Presenter.Interface.IPresenterChat;
import Presenter.PrChat;
import Utils.MyMouseWheelScroller;
import View.Forms.Modal.ViewContactAdd;
import View.Forms.Modal.ViewContactEdit;
import View.Forms.Modal.ViewProfileEdit;
import View.Forms.Modal.ViewLoading;
import View.Interface.*;
import View.ListItem.ContactListItem;
import View.ListItem.MessageItem;
import View.ListRenderer.ListCellRendererContact;
import View.ListRenderer.ListCellRendererMessage;
import View.Resources;
import View.WindowManager;
import org.javagram.response.object.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class ViewChat implements IViewChat {

    private IPresenterChat presenter;
    private ListCellRendererContact contactListRenderer; // визуализатор списка контактов
    private ListCellRendererMessage messageListRenderer; // визуализатор списка сообщений
    private volatile DefaultListModel<ContactListItem> modelContacts; // Список диалогов
    private volatile DefaultListModel<MessageItem> modelMessages;       // Список сообщений

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
    private JButton clearBtn;

    // Компоненты доавления контактов
    private JPanel addContactJPanel;
    private JButton addContactBtn;

    // Компоненты сообщений
    private JPanel sendMessageJPanel;
    private JButton sendMessageBtn;
    private JTextField sendMessageTextField;
    private JScrollPane messageListScrollPane;
    private JList<MessageItem> messagesJList;
    private JButton updateDialogButton;
    private JButton clearMsg;

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
    private BufferedImage userPhotoBlue;
    private BufferedImage contactPhotoWhite;

    // Масштабированные фото
    private Image defaultPhoto;
    private Image contactPhotoWhiteMini;
    private Image userPhotoBlueMini;


    // Модальные view с редактированием профиля, добавлением и редактированием контактов
    private IViewProfileEdit editProfile;
    private IViewContactAdd addContact;
    private IViewContactEdit editContact;
    private IViewLoading viewLoading;

    private int width;
    private int height;

    public ViewChat() {
        setPresenter(new PrChat(this));
        WindowManager.setContentView(this);
        setupBorders();
        setListeners();

        contactListRenderer = new ListCellRendererContact(this.presenter);
        messageListRenderer = new ListCellRendererMessage();
        contactsJList.setCellRenderer(contactListRenderer);
        messagesJList.setCellRenderer(messageListRenderer);
        messagesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        userPhotoBlueMini = defaultPhoto;
        contactPhotoWhiteMini = defaultPhoto;
        updateDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        updateDialogs();
                    }
                });
            }
        });
        clearMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.deleteHistory();
            }
        });

        // Добавение rootPanel в z-index на нижний слой
        layeredRootPane.add(rootPanel, JLayeredPane.DEFAULT_LAYER, -1);

        // Форма ViewProfileEdit добавляется в z-index на слой выше
        editProfile = new ViewProfileEdit(presenter);
        editProfile.getRootPanel().setSize(width, height);
        layeredRootPane.add(editProfile.getRootPanel(), JLayeredPane.PALETTE_LAYER, -1);

        // Форма ViewContactEdit добавляется в z-index на слой выше
        editContact = new ViewContactEdit(presenter);
        editContact.getRootPanel().setSize(width, height);
        layeredRootPane.add(editContact.getRootPanel(), JLayeredPane.PALETTE_LAYER, -1);

        // Форма ViewContactAdd добавляются в z-index на слой выше
        addContact = new ViewContactAdd(presenter);
        addContact.getRootPanel().setSize(width, height);
        layeredRootPane.add(addContact.getRootPanel(), JLayeredPane.PALETTE_LAYER, -1);

        // Форма Загрузки добавляются в z-index на слой выше
        viewLoading = new ViewLoading();
        viewLoading.getRootPanel().setSize(width, height);
        layeredRootPane.add(viewLoading.getRootPanel(), JLayeredPane.PALETTE_LAYER, -1);

        // Обработка события на изменение размера
        WindowManager.getContentPanel().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layeredRootPane.setSize(e.getComponent().getSize());
                rootPanel.setSize(e.getComponent().getSize());
                editProfile.getRootPanel().setSize(e.getComponent().getSize());
                editContact.getRootPanel().setSize(e.getComponent().getSize());
                addContact.getRootPanel().setSize(e.getComponent().getSize());
            }
        });
        viewLoading.showView();
    }

    private void createUIComponents() {
        // Получение ресурсов изображений
        loadImages();
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
        width = WindowManager.getScreenWidth();
        height = WindowManager.getScreenHeight();

        // Создание панели с Z-index (слои)
        layeredRootPane = new JLayeredPane();
        layeredRootPane.setMinimumSize(new Dimension(500, 300));

//        layeredRootPane.setMinimumSize(new Dimension(width, height));
        layeredRootPane.setSize(new Dimension(width, height));
        layeredRootPane.setPreferredSize(new Dimension(width, height));

        rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setMinimumSize(new Dimension(width, height));
        rootPanel.setSize(new Dimension(width, height));
        rootPanel.setPreferredSize(new Dimension(width, height));
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
                g.drawImage(contactPhotoWhiteMini, 0, 0, null);
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
                g.drawImage(userPhotoBlueMini, 0, 0, null);
                g.drawImage(maskBlueMiniImg, 0, 0, null);
            }
        };
    }

    private void setupBorders() {
        contactsJList.setBorder(null);
        contactJPanel.setBorder(BorderFactory.createMatteBorder(0, 1,1, 0, new Color(237, 237, 237)));
        sendMessageTextField.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
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
                presenter.sendMessage(sendMessageTextField.getText());
                messageListScrollPane.getVerticalScrollBar().setValue(messageListScrollPane.getVerticalScrollBar().getMaximum());
            }
        });

        // Обработчик нажатия на поле ввода сообщения
        sendMessageTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Send Message");
                presenter.sendMessage(sendMessageTextField.getText());
                messageListScrollPane.getVerticalScrollBar().setValue(messageListScrollPane.getVerticalScrollBar().getMaximum());
            }
        });

        // Обработчик фокуса для поля ввода сообщения
        sendMessageTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("Текст сообщения".equals(sendMessageTextField.getText())) {
                    sendMessageTextField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if ("".equals(sendMessageTextField.getText())) {
                    sendMessageTextField.setText("Текст сообщения");
                }
            }
        });

        // Обработчик нажатия кнопки поиска
        searchContactTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.search(searchContactTextField.getText());
            }
        });
        searchContactTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("Поиск".equals(searchContactTextField.getText())) {
                    searchContactTextField.setText("");
                }
                clearBtn.setVisible(true);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if ("Поиск".equals(searchContactTextField.getText())) {
                    clearBtn.setVisible(false);
                }
                if ("".equals(searchContactTextField.getText())) {
                    clearBtn.setVisible(false);
                    searchContactTextField.setText("Поиск");
                }
            }
        });
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchContactTextField.setText("");
                searchContactTextField.requestFocus();
                presenter.refreshDialogList();
                hideContactInterface();
            }
        });

        // Обработчик нажатия на кнопку редактирования профиля пользователя
        userSettingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProfileEditView();
            }
        });

        // Обработчик нажатия на кнопку редактирования профиля контакта
        contactEditBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showContactEditView();
            }
        });

        // Обработчик нажатия на кнопку добавления контакта
        addContactBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showContactAddView();
            }
        });
    }

    /** --------- Getters ------------ */
    @Override
    public JLayeredPane getRootPanel() {
        return layeredRootPane;
    }

    public JList<ContactListItem> getContactsJList() {
        return contactsJList;
    }

    public ListCellRendererContact getContactListRenderer() {
        return contactListRenderer;
    }

    @Override
    public DefaultListModel<ContactListItem> getModelContacts() {
        return modelContacts;
    }

    @Override
    public DefaultListModel<MessageItem> getModelMessages() {
        return modelMessages;
    }

    /** --------- Setters ------------ */
    public void setContactName(String userName) {
        contactNameLable.setText(userName);
    }

    public void setUserName(String userName) {
        userNameLabel.setText(userName);
    }

    public void setContactPhoto(BufferedImage photo) {
        contactPhotoWhiteMini = photo.getScaledInstance(29, 29, 5);
    }

    public void setUserPhoto(BufferedImage photo) {
        userPhotoBlueMini = photo.getScaledInstance(29, 29, 5);
    }

    @Override
    public void setPresenter(IPresenterChat presenter) {
        this.presenter = presenter;
    }

    /** --------- Методы показа модальных окон ---------*/

    // Показать модальное окно с добавлением контакта
    @Override
    public void showContactAddView() {
        addContact.clearFields();
        addContact.showView();
    }

    // Показать модальное окно с редактированием своего профиля
    @Override
    public void showProfileEditView() {
        User user = presenter.getSelfUser();
        editProfile.setUserInfo(user.getFirstName(), user.getLastName(), user.getPhone());
        editProfile.showView();
    }

    // Показать модальное окно с редактированием контакта
    @Override
    public void showContactEditView() {
        User user = presenter.getSelectedContact().getUser();
        editContact.setContactInfo(user.toString(), user.getPhone(), user.getId());
        editContact.showView();
    }

    /** --------- Методы управления --------- */

    // Очистить поле ввода сообщения
    @Override
    public void clearMessageField() {
        sendMessageTextField.setText("");
    }

    @Override
    public void showDialogs(DefaultListModel<ContactListItem> model) {
        modelContacts = model;
        contactsJList.setModel(model);
        contactsJList.revalidate();
        contactsJList.repaint();
    }

    @Override
    public void showMessages(DefaultListModel<MessageItem> model) {
        modelMessages = model;
        messagesJList.setModel(model);
        scrollMessagesToEnd();
    }

    private void scrollMessagesToEnd() {
        messagesJList.revalidate();
        messagesJList.repaint();
        JScrollBar verticalBar = messageListScrollPane.getVerticalScrollBar();
        verticalBar.setValue(verticalBar.getMaximum());
    }

    // Очищаем выбор контакта, сообщения, скрываем компоненты имени, фото и кнопки редактирования  контакта,
    // поле ввода сообщения и кнопку отправки.
    @Override
    public void hideContactInterface() {
        contactListRenderer.clearSelectedItem();
        modelMessages.clear();
        contactNameLable.setText("");

        contaclLogoMiniJPanel.setVisible(false);
        contactEditBtn.setVisible(false);
        sendMessageJPanel.setVisible(false);
    }

    // Отображаем компоненты имя, фото и кнопку редактирования контакта,
    // поле ввобда сообщения и кнопку отправки
    @Override
    public void showContactInterface() {
        contaclLogoMiniJPanel.setVisible(true);
        contactEditBtn.setVisible(true);
        sendMessageJPanel.setVisible(true);
    }

    @Override
    public void showLoadingView() {
        viewLoading.showView();
    }

    @Override
    public void hideLoadingView() {
        viewLoading.hideView();
    }


    private void updateDialogs() {
        presenter.updateDialogsLocal();
        presenter.refreshDialogList();
    }
}

