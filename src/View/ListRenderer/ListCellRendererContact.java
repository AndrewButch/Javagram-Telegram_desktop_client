package View.ListRenderer;

import View.Forms.ViewChat;
import View.ListItem.ContactItem;
import View.Resources;
import org.javagram.response.object.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.image.BufferedImage;


public class ListCellRendererContact extends ContactItem implements ListCellRenderer<ContactItem> {
    private BufferedImage contact_gray_online;
    private BufferedImage contact_white_online;
    private ViewChat view;

    public ListCellRendererContact(ViewChat view) {
        getPortraitJPanel().setOpaque(true);
        contact_gray_online = Resources.getImage(Resources.MASK_GRAY_ONLINE);
        contact_white_online = Resources.getImage(Resources.MASK_WHITE_ONLINE);
        this.view = view;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ContactItem> list, ContactItem value, int index, boolean isSelected, boolean cellHasFocus) {
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
                view.showMessages(value.getUser().getId());
                setPortraint(contact_white_online);
            }
        } else {
            getRootPanel().setBackground(list.getBackground());
            getRootPanel().setForeground(list.getForeground());
            getRootPanel().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(195, 195, 195)));
            setPortraint(contact_gray_online);
        }
        return this.getRootPanel();
    }

    private void setupContactCellText(ContactItem value) {
        User user = value.getUser();
        if (user != null) {
            if (user.getId() == 0) {
                setUserName("Telegram");
            } else {
                setUserName(user.getFirstName() + " " + user.getLastName());
            }
            setLastMsg(value.getLastMsg().getText());
            setLastMsgDate(value.getLastMsgDate().getText());
        }
    }
}
