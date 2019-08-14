package Presenter;

import Presenter.Interface.IPresenterSMSCodeCheck;
import View.Interface.IViewSMSCodeCheck;
import View.WindowManager;

public class PrSMSCodeCheck implements IPresenterSMSCodeCheck {
    IViewSMSCodeCheck view;

    public PrSMSCodeCheck(IViewSMSCodeCheck view) {
        this.view = view;
        view.setPhoneNumber(model.getPhone());
    }

    @Override
    public void checkCode() {
        String code = view.getCode();
        if (code.isEmpty())
            WindowManager.showWarningDialog("Необходимо ввести SMS-код");
        else if (model.getAuthSendCode().isRegistered()) {
            model.setSmsCode(code);
            model.signIn(code);
            if (model.getAuthorization() != null)
                view.showChatView();
            else
                view.showPhoneInputView(); // Переход на на форму ввода телефона, если смс-код неверный
        } else
            view.showSignUpView(); // Переход к продолжению регистрации, если телефон не зарегистрирован
    }
}
