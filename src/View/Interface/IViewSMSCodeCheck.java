package View.Interface;

import Presenter.Interface.IPresenterSMSCodeCheck;

public interface IViewSMSCodeCheck extends IView {
    void setPresenter(IPresenterSMSCodeCheck presenter);
    String getCode();
    void setPhoneNumber(String phoneNumber);
    void showSignUpView();
    void showPhoneInputView();
    void showChatView();
}
