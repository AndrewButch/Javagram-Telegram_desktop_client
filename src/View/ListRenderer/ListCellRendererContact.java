package View.ListRenderer;

import Presenter.Interface.IPresenterChat;
import View.ListItem.ContactListItem;
import View.Resources;
import org.javagram.response.object.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.image.BufferedImage;


public class ListCellRendererContact extends ContactListItem implements ListCellRenderer<ContactListItem> {
    private BufferedImage contact_gray_online;
    private BufferedImage contact_gray_offline;
    private BufferedImage contact_white_online;
    private BufferedImage contact_white_offline;
    private IPresenterChat presenter;
    private volatile ContactListItem selectedItem;

    public ListCellRendererContact(IPresenterChat presenter) {
        getPortraitJPanel().setOpaque(true);
        contact_gray_online = Resources.getImage(Resources.MASK_GRAY_ONLINE);
        contact_white_online = Resources.getImage(Resources.MASK_WHITE_ONLINE);
        contact_gray_offline = Resources.getImage(Resources.MASK_GRAY);
        contact_white_offline = Resources.getImage(Resources.MASK_WHITE);
        this.presenter = presenter;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ContactListItem> list, ContactListItem value, int index, boolean isSelected, boolean cellHasFocus) {
        // Установка текста для текущей ячейки контактов
        setupContactCellText(value);

        // Настройка стиля выделенной ячейки с контактом
        if (isSelected) {
            getRootPanel().setBackground(list.getSelectionBackground());
            getRootPanel().setForeground(list.getSelectionForeground());
            Border lineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(195, 195, 195));
            Border matteBorder = BorderFactory.createMatteBorder(0,0,0, 4, new Color(0, 179, 230));
            getRootPanel().setBorder(new CompoundBorder(lineBorder, matteBorder));
            if (value.getUser() != null ) {
                selectedItem = value;
                presenter.setSelectedContact(value);
                presenter.refreshChat();
                presenter.showInterface();
                if (value.isOnline()) {
                    setStatusBorder(contact_white_online);
                } else {
                    setStatusBorder(contact_white_offline);
                }
            }
        } else {
            getRootPanel().setBackground(list.getBackground());
            getRootPanel().setForeground(list.getForeground());
            getRootPanel().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(195, 195, 195)));
            if (value.isOnline()) {
                setStatusBorder(contact_gray_online);
            } else {
                setStatusBorder(contact_gray_offline);
            }
        }
        return this.getRootPanel();
    }

    private void setupContactCellText(ContactListItem value) {
        User user = value.getUser();
        if (user != null) {
            if (user.getId() == 0) {
                setUserName("Telegram");
            } else {
                setUserName(user.getFirstName() + " " + user.getLastName());
            }
            setPhoto(value.getPhoto());
            setMessage(value.getMessage());
        }
    }

    public synchronized ContactListItem getSelectedItem() {
        return selectedItem;
    }

    public void clearSelectedItem() {
        selectedItem = null;
    }
}
