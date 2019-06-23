package View.JListComponents;

import View.Resources;
import org.apache.commons.lang3.StringUtils;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class ListCellRendererMessage extends MessageItem implements ListCellRenderer<MessageItem> {
    private static final int MAX_ROW_LENGHT = 30; // максимальное кол-во символов в одной строке сообщения
    private static final int ROW_HEIGHT = 20; // высота строки

    private BufferedImage topInMessageIMG;
    private BufferedImage botInMessageIMG;
    private BufferedImage leftInMessageIMG;
    private BufferedImage topOutMessageIMG;
    private BufferedImage botOutMessageIMG;
    private BufferedImage rightOutMessageIMG;

    private User currentContact;
    private User selfUser;

    public ListCellRendererMessage(User selfUser, User currentContact) {
        this.selfUser = selfUser;
        this.currentContact = currentContact;
        topInMessageIMG = Resources.getImage(Resources.MESSAGE_IN_TOP);
        botInMessageIMG = Resources.getImage(Resources.MESSAGE_IN_BOTTOM);
        leftInMessageIMG = Resources.getImage(Resources.MESSAGE_IN_LEFT);
        topOutMessageIMG = Resources.getImage(Resources.MESSAGE_OUT_TOP);
        botOutMessageIMG = Resources.getImage(Resources.MESSAGE_OUT_BOTTOM);
        rightOutMessageIMG = Resources.getImage(Resources.MESSAGE_OUT_RIGHT);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends MessageItem> list, MessageItem value, int index, boolean isSelected, boolean cellHasFocus) {
        // Визуальная настройка сообщения
        setupMessage(value);
        String text = "";
        text = value.getMessage().getMessage();
        text = text == null ? "" : text;
        // Определение высоты контейнер сообщения из кол-ва строк сообщения
        // 1-й параметр вычисляем считая, что в тексте нет переносов строки
        int rowCount = text.length() / MAX_ROW_LENGHT;
        // 2-й параметр считает кол-во переносов строки
        int enterCount = StringUtils.countMatches(text, "\n" );
        // Число строк выбирается, как самый большой параметр (1-й или 2-ой)
        rowCount = rowCount < enterCount ? enterCount : rowCount;

        int height = ROW_HEIGHT + rowCount * ROW_HEIGHT;
        getMessageJTextPane().setPreferredSize(new Dimension(308, height));

        setMessage(value.getMessage().getMessage());
        setDate(Utils.Utils.convertIntDateToString(value.getMessage().getDate()));



        return this.getRootPanel();
    }

    private void setupMessage(MessageItem messageItem) {
        int toId = messageItem.getMessage().getToId();
        int fromId = messageItem.getMessage().getFromId();

        LayoutManager layout = getRootPanel().getLayout();
        getRootPanel().setBackground(Color.WHITE);
        if (selfUser != null) {
            if (fromId == selfUser.getId()) {
                ((FlowLayout) layout).setAlignment(FlowLayout.RIGHT);
                setTopMessageIMG(topOutMessageIMG);
                setBotMessageIMG(botOutMessageIMG);
                setRightMessageIMG(rightOutMessageIMG);
                setLeftMessageIMG(null);
                getMessageJPanel().setBackground(new Color(74, 68, 168));
            } else if (fromId == currentContact.getId() && toId == selfUser.getId()) {
                ((FlowLayout) layout).setAlignment(FlowLayout.LEFT);
                setTopMessageIMG(topInMessageIMG);
                setBotMessageIMG(botInMessageIMG);
                setLeftMessageIMG(leftInMessageIMG);
                setRightMessageIMG(null);
                getMessageJPanel().setBackground(new Color(1, 167, 217));
            } else {
                ((FlowLayout) layout).setAlignment(FlowLayout.CENTER);
                setTopMessageIMG(null);
                setBotMessageIMG(null);
                setLeftMessageIMG(null);
                setRightMessageIMG(null);
                getMessageJPanel().setBackground(new Color(231, 17, 28));
            }
        }

    }


}
