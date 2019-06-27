package Presenter;

import Model.Model;
import View.Forms.ViewSMSCode;
import View.WindowManager;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrSMSCode implements IPresenter {
    Model model;
    ViewSMSCode view;

    public PrSMSCode(ViewSMSCode view) {
        this.view = view;
        this.model = Model.getInstance();
        view.getPhoneNumberLabel().setText(model.getPhone());
        setupCodeField();
        setListeners();
    }

    private void setListeners() {
        // слушатель на кнопку "Продолжить"
        view.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCodeForm();
            }
        });
        // Слушатель для поля ввода СМС-кода
        view.getCodePasswordField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCodeForm();
            }
        });
    }

    private void checkCodeForm() {
        String code = getSmsCode();
        if (code.isEmpty())
            WindowManager.showWarningDialog("Необходимо ввести SMS-код");
        else if (model.getAuthSendCode().isRegistered()) {
            model.setSmsCode(code);
            model.signIn(code);
            if (model.getAuthorization() != null)
                view.goToChatView();
            else
                view.goToEnterPhoneView(); // Переход на на форму ввода телефона, если смс-код неверный
        } else
            view.goToSignUpView();
    }

    private String getSmsCode() {
        String code = new String(view.getCodePasswordField().getPassword());
        model.setSmsCode(code);
        return code;
    }

    private void setupCodeField() {
        // инициализация поля смс-кода с возможностью вводить только 5 символов
        JPasswordField codeTextField = view.getCodePasswordField();
        ((PlainDocument)codeTextField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String string =fb.getDocument().getText(0, fb.getDocument().getLength())+text;
                if (string.length() <= 5)
                    super.replace(fb, offset, length, text, attrs);
            }
        });
    }
}
