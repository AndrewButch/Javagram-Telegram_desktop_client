package Presenter;

import Presenter.Interface.IPresenterSignUp;
import View.Interface.IViewSignUp;
import View.WindowManager;

public class PrSignUp implements IPresenterSignUp {
    private IViewSignUp view;

    public PrSignUp(IViewSignUp view) {
        this.view = view;
    }

    @Override
    public void signup() {
        String firstName = view.getFirstName();
        String lastName = view.getLastName();
        String code = model.getSmsCode();

        String error = "";              // Сообщение об ошибке, будет передано в showWarningDialog
        if (firstName.isEmpty()) {      // Проверка заполненности поля Имя
            error += "- Имя\n";
        }
        if (lastName.isEmpty()) {     // Проверка заполненности поля Фамилия
            error += "- Фамилия\n";
        }
        if (!error.isEmpty()) {         // Если в ошибке есть сообщения, то выводится диалог
            WindowManager.showWarningDialog("Не заполнены поля:\n" + error);
        } else {
            model.signUp(firstName, lastName, code);
            if (model.getAuthorization() != null)
                view.showChatView();
            else
                view.showPhoneInputView(); // Переход на на форму ввода телефона, если не авторизовался
        }
    }
}
