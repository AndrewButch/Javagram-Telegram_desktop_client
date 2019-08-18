package View.ListRenderer;

import Utils.DateUtils;
import View.ListItem.MessageItem;
import View.Resources;

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

    public ListCellRendererMessage() {
        loadImages();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends MessageItem> list, MessageItem value, int index, boolean isSelected, boolean cellHasFocus) {
        // Визуальная настройка сообщения
        setupMessage(value);
        if (isSelected) {
            getRootPanel().setBackground(list.getSelectionBackground());
            getRootPanel().setForeground(list.getSelectionForeground());
        } else {
            getRootPanel().setBackground(list.getBackground());
            getRootPanel().setForeground(list.getForeground());
        }
        String text = "";
        text = value.getMessage().getMessage();
        text = text == null ? "" : text;
        int rowCount = getRowCount(text);

        int height = rowCount * ROW_HEIGHT;
//        Font font = new Font("Helvetica", Font.PLAIN, 12);
        getMessageJTextPane().setPreferredSize(new Dimension(308, height));
        getMessageJTextPane().setSize(new Dimension(308, height));

        setMessage(value.getMessage().getMessage());
        setDate(DateUtils.convertIntDateToString(value.getMessage().getDate()));
        return this.getRootPanel();
    }

    private void setupMessage(MessageItem messageItem) {
        // Сообщение исходящее ???
        boolean isOut = messageItem.getMessage().isOut();
        LayoutManager layout = getRootPanel().getLayout();
        getRootPanel().setBackground(Color.WHITE);
        if (isOut) {
            ((FlowLayout) layout).setAlignment(FlowLayout.RIGHT);
            setTopMessageIMG(topOutMessageIMG);
            setBotMessageIMG(botOutMessageIMG);
            setRightMessageIMG(rightOutMessageIMG);
            setLeftMessageIMG(null);
            getMessageJPanel().setBackground(new Color(74, 68, 168));
        } else {
            ((FlowLayout) layout).setAlignment(FlowLayout.LEFT);
            setTopMessageIMG(topInMessageIMG);
            setBotMessageIMG(botInMessageIMG);
            setLeftMessageIMG(leftInMessageIMG);
            setRightMessageIMG(null);
            getMessageJPanel().setBackground(new Color(1, 167, 217));
        }
    }

    private void loadImages() {
        topInMessageIMG = Resources.getImage(Resources.MESSAGE_IN_TOP);
        botInMessageIMG = Resources.getImage(Resources.MESSAGE_IN_BOTTOM);
        leftInMessageIMG = Resources.getImage(Resources.MESSAGE_IN_LEFT);
        topOutMessageIMG = Resources.getImage(Resources.MESSAGE_OUT_TOP);
        botOutMessageIMG = Resources.getImage(Resources.MESSAGE_OUT_BOTTOM);
        rightOutMessageIMG = Resources.getImage(Resources.MESSAGE_OUT_RIGHT);
    }

    /** Подсчет количества строк в сообщении */
    private int getRowCount(String message) {
        // Разбить сообщение по переносам строки
        String[] enterSplitted = message.split("\n");
        int rowCount = 0;
        for (String s : enterSplitted) {
            // Если разбитое сообщение не содержит символов и это все равно +1
            int count = s.length() / MAX_ROW_LENGHT + 1;
            rowCount += count ;
        }
        return rowCount;
    }
}
