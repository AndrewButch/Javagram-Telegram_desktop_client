package Presenter;

import Presenter.Interface.IPresenterPhoneInput;
import View.Forms.ViewPhoneInput;
import View.Interface.IViewPhoneInput;
import View.WindowManager;
import org.javagram.response.AuthSentCode;

public class PrPhoneInput implements IPresenterPhoneInput {
//    public static final String TEST_NUMBER = "9996622222";
    private IViewPhoneInput view;

    public PrPhoneInput(ViewPhoneInput view) {
        this.view = view;
    }

    @Override
    public void authorizationPhone(String phoneNumber) {
        if (phoneNumber != null) {
            String clearPhoneNumber = phoneNumber.replaceAll("[^\\d]+", "");
//            clearPhoneNumber = TEST_NUMBER;
            model.setPhone(clearPhoneNumber);
            sendSMS();
        } else {
            WindowManager.showWarningDialog("Номер телефона не соотетствует формату" );
            view.clearPhoneField();
        }
    }

    private void sendSMS() {
        AuthSentCode sendCode = model.getAuthSendCode();
        if (sendCode != null) {
            view.showSMSCheckView();
        } else {
            WindowManager.showWarningDialog("Что-то пошло не так");
        }
    }
}
