package View.Interface;

import Presenter.Interface.IPresenterSignUp;

public interface IViewSignUp extends IView {
    void setPresenter(IPresenterSignUp presenter);

    String getFirstName();
    String getLastName();

    void showPhoneInputView();
    void showChatView();
}
