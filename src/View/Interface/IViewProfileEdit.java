package View.Interface;

import Presenter.Interface.IPresenterProfileEdit;

public interface IViewProfileEdit extends IView {
    void setPresenter(IPresenterProfileEdit presenter);

    String getFirstName();
    String getLastName();

    void showView();
    void hideView();

    void setUserInfo(String firstName, String lastName, String phoneNumber);
}
