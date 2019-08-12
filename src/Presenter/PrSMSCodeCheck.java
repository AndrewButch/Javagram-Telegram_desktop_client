package Presenter;

import Presenter.Interface.IPresenter;
import Presenter.Interface.IPresenterSMSCodeCheck;
import View.Forms.ViewSMSCode;
import View.WindowManager;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrSMSCodeCheck implements IPresenterSMSCodeCheck {
    ViewSMSCode view;

    public PrSMSCodeCheck(ViewSMSCode view) {
        this.view = view;
        view.setPhoneNumber(model.getPhone());
    }

    @Override
    public void checkCode() {
        String code = new String(view.getCode());
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
