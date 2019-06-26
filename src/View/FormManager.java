package View;

import Presenter.Presenter;
import View.Forms.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Класс View.
 * Управляет всеми подView
 */

public class FormManager extends JFrame{
    private Presenter presenter;
    private ViewEnterPhone phoneForm;        // форма ввода телефона
    private ViewSMSCode codeForm;          // форма ввода СМС-кода
    private ViewSignUp signUpForm;      // форма завершения регистрации
    private ViewChat chatWindow;      // форма с контактами и чатом
    private Decoration decoration;

    public FormManager() {
        presenter = new Presenter(this);
        decoration = new Decoration(this);
        setContentPane(decoration);
        setSize(900, 630);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        goToPhoneForm();
    }

    public void showWarningDialog( String message) {
        JOptionPane.showMessageDialog(
                decoration.getRootPane(),
                message,
                "Ошибка!",
                JOptionPane.ERROR_MESSAGE);
    }

    private String getPhoneNumber() {
        JFormattedTextField phoneNumberTextField = phoneForm.getPhoneJFormattedText();
        String phoneText = (String)phoneNumberTextField.getValue();
        if (phoneText != null) {
            phoneText = phoneText.replaceAll("[^\\d]+", "");
        } else {
            showWarningDialog("Номер телефона не соотетствует формату" );
            phoneNumberTextField.setText("");
        }
        return phoneText;
    }

    public String getSmsCode() {
        String code = new String(codeForm.getCodePasswordField().getPassword());
        return code;
    }


    /** ------------Переходы по формам------------------------------ */
    // Переход на форму ввода номера телефона
    public void goToPhoneForm() {
        initPhoneForm();
        decoration.setContentPanel(phoneForm.getRootPanel());
        revalidate();
        repaint();
        phoneForm.getPhoneJFormattedText().requestFocus();
    }

    // Переход на форму ввода СМС-кода
    public void goToCodeForm(String phoneNumber) {
        initCodeForm();
        decoration.setContentPanel(codeForm.getRootPanel());
        revalidate();
        repaint();
        codeForm.getPhoneNumberLabel().setText(phoneNumber);
        codeForm.getCodePasswordField().requestFocus();
    }

    // Переход на форму завершения регистрации (ввод имя, фамилия)
    public void goToSignUpForm() {
        initSignUpForm();
        decoration.setContentPanel(signUpForm.getRootPanel());
        revalidate();
        repaint();
        signUpForm.getFirstNameText().requestFocus();
    }

    // Переход на форму окна чата
    public void goToChatForm(){
        initChatForm();
        decoration.setContentPanel(chatWindow.getRootPanel());
        revalidate();
        repaint();
    }


    /** ------------Инициализация форм------------------------------ */
    // Инициализация Формы ввода телефона
    private void initPhoneForm() {
        phoneForm = new ViewEnterPhone();

        // слушатель  на кнопку "Продолжить"
        phoneForm.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.checkPhoneForm(getPhoneNumber());
            }
        });
        // слушатель на поле ввода телефона
        phoneForm.getPhoneJFormattedText().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.checkPhoneForm(getPhoneNumber());
            }
        });
        System.out.println("1111111111 - зарегистрированный номер телефона");
    }

    // Инициализация Формы ввода СМС-кода
    private void initCodeForm() {
        codeForm = new ViewSMSCode();

        // слушатель на кнопку "Продолжить"
        codeForm.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.checkCodeForm(getSmsCode());
            }
        });
        // Слушатель для поля ввода СМС-кода
        codeForm.getCodePasswordField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.checkCodeForm(getSmsCode());
            }
        });

        // инициализация поля смс-кода с возможностью вводить только 5 символов
        JPasswordField codeTextField = codeForm.getCodePasswordField();
        ((PlainDocument)codeTextField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String string =fb.getDocument().getText(0, fb.getDocument().getLength())+text;
                if (string.length() <= 5)
                    super.replace(fb, offset, length, text, attrs);
            }
        });
    }

    // Инициализация Формы ввода данных для регистрации
    private void initSignUpForm() {
        signUpForm = new ViewSignUp();

        // слушатель на кнопку "Продолжить"
        signUpForm.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = signUpForm.getFirstNameText().getText();
                String lastName = signUpForm.getLastNameText().getText();
                String code = new String(codeForm.getCodePasswordField().getPassword());
                presenter.signUp(firstName, lastName, code);
            }
        });
        // слушатель на поле ввода фамилии
        signUpForm.getLastNameText().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = signUpForm.getFirstNameText().getText();
                String secondName = signUpForm.getLastNameText().getText();
                String code = new String(codeForm.getCodePasswordField().getPassword());
                presenter.signUp(firstName, secondName, code);

            }
        });

    }

    // Инициализация Формы с контактами
    private void initChatForm() {
        chatWindow = new ViewChat(presenter);
    }
}
