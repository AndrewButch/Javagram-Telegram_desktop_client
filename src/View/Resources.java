package View;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Resources {
    private static HashMap<String, BufferedImage> images = new HashMap<>();

    public static final String BACKGROUND = "BACKGROUND";
    public static final String LOGO = "LOGO";
    public static final String LOGO_MINI = "LOGO_MINI";
    public static final String LOGO_MICRO = "LOGO_MICRO";
    public static final String ICON_PHONE = "ICON_PHONE";
    public static final String ICON_SEARCH = "ICON_SEARCH";
    public static final String ICON_LOCK = "ICON_LOCK";
    public static final String ICON_BACK = "ICON_BACK";
    public static final String MASK_BLUE_MINI = "MASK_BLUE_MINI";
    public static final String MASK_WHITE_MINI = "MASK_WHITE_MINI";
    public static final String MASK_GRAY_ONLINE = "MASK_GRAY_ONLINE";
    public static final String MASK_WHITE_ONLINE = "MASK_WHITE_ONLINE";
    public static final String MASK_WHITE = "rMASK_WHITE";
    public static final String MASK_GRAY = "MASK_GRAY";
    public static final String MASK_DARK_GRAY_BIG = "MASK_DARK_GRAY_BIG";
    public static final String MESSAGE_IN_BOTTOM = "MESSAGE_IN_BOTTOM";
    public static final String MESSAGE_IN_LEFT = "MESSAGE_IN_LEFT";
    public static final String MESSAGE_IN_TOP = "MESSAGE_IN_TOP";
    public static final String MESSAGE_OUT_BOTTOM = "MESSAGE_OUT_BOTTOM";
    public static final String MESSAGE_OUT_RIGHT = "MESSAGE_OUT_RIGHT";
    public static final String MESSAGE_OUT_TOP = "MESSAGE_OUT_TOP";

    static {
        try {
            images.put(BACKGROUND, ImageIO.read(new File("res/img/GUI_Components/background.png")));
            images.put(LOGO, ImageIO.read(new File("res/img/GUI_Components/logo.png")));
            images.put(LOGO_MINI, ImageIO.read(new File("res/img/GUI_Components/logo-mini.png")));
            images.put(LOGO_MICRO, ImageIO.read(new File("res/img/GUI_Components/logo-micro.png")));
            images.put(ICON_PHONE, ImageIO.read(new File("res/img/GUI_Components/icon-phone.png")));
            images.put(ICON_SEARCH, ImageIO.read(new File("res/img/GUI_Components/icon-search.png")));
            images.put(ICON_LOCK, ImageIO.read(new File("res/img/GUI_Components/icon-lock.png")));
            images.put(ICON_BACK, ImageIO.read(new File("res/img/GUI_Components/icon-back.png")));
            images.put(MASK_BLUE_MINI, ImageIO.read(new File("res/img/GUI_Components/mask-blue-mini.png")));
            images.put(MASK_WHITE_MINI, ImageIO.read(new File("res/img/GUI_Components/mask-white-mini.png")));
            images.put(MASK_GRAY_ONLINE, ImageIO.read(new File("res/img/GUI_Components/mask-gray-online.png")));
            images.put(MASK_WHITE_ONLINE, ImageIO.read(new File("res/img/GUI_Components/mask-white-online.png")));
            images.put(MASK_WHITE, ImageIO.read(new File("res/img/GUI_Components/mask-white.png")));
            images.put(MASK_GRAY, ImageIO.read(new File("res/img/GUI_Components/mask-gray.png")));
            images.put(MASK_DARK_GRAY_BIG, ImageIO.read(new File("res/img/GUI_Components/mask-dark-gray-big.png")));
            images.put(MESSAGE_IN_TOP, ImageIO.read(new File ("res/img/GUI_Components/message-in-top.png")));
            images.put(MESSAGE_IN_BOTTOM, ImageIO.read(new File ("res/img/GUI_Components/message-in-bottom.png")));
            images.put(MESSAGE_IN_LEFT, ImageIO.read(new File ("res/img/GUI_Components/message-in-left.png")));
            images.put(MESSAGE_OUT_TOP, ImageIO.read(new File ("res/img/GUI_Components/message-out-top.png")));
            images.put(MESSAGE_OUT_BOTTOM, ImageIO.read(new File ("res/img/GUI_Components/message-out-bottom.png")));
            images.put(MESSAGE_OUT_RIGHT, ImageIO.read(new File ("res/img/GUI_Components/message-out-right.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage getImage(String img) {
        return images.get(img);
    }
}
