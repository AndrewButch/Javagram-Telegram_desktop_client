package Presenter;

import Presenter.Interface.IPresenter;
import View.Forms.ViewEnterPhone;
import View.WindowManager;
import org.javagram.response.AuthSentCode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrEnterPhone implements IPresenter {
    public static final String TEST_NUMBER = "9996622222";
    private ViewEnterPhone view;

    public PrEnterPhone(ViewEnterPhone view) {
        this.view = view;
        setListeners();
    }

    private void setListeners() {
        // слушатель  на кнопку "Продолжить"
        view.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authorizationPhone();
            }
        });
        // слушатель на поле ввода телефона
        view.getPhoneJFormattedText().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authorizationPhone();
            }
        });
        System.out.println("1111111111 - зарегистрированный номер телефона");
    }

    private void authorizationPhone() {
        // Получить номер телефона
        String phoneNumber = getPhoneNumber();
        model.setPhone(phoneNumber);
        if (phoneNumber != null) {
            sendSMS();
        } else {
            WindowManager.showWarningDialog("Номер телефона не соотетствует формату" );
            view.getPhoneJFormattedText().setText("");
        }
    }

    private String getPhoneNumber() {
        // Получить строку с номером телефона
        JFormattedTextField phoneNumberTextField = view.getPhoneJFormattedText();
        String phoneNumber = (String)phoneNumberTextField.getValue();
        if (phoneNumber != null) {
            phoneNumber = phoneNumber.replaceAll("[^\\d]+", "");
        } else {
            phoneNumberTextField.setText("");
        }
        phoneNumber = TEST_NUMBER;
        return phoneNumber;
    }

    private void sendSMS() {
        AuthSentCode sendCode = model.getAuthSendCode();
        if (sendCode != null) {
            view.goToSMSView();
        } else {
            WindowManager.showWarningDialog("Что-то пошло не так");
        }
    }
}
