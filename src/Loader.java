import View.Forms.ViewEnterPhone;
import View.WindowManager;

import java.io.IOException;

public class Loader {
    public static void main(String[] args) throws IOException {
        WindowManager.startFrame();
        new ViewEnterPhone();
    }
}
