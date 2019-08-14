package View.Interface;

import Presenter.Interface.IPresenterContactAdd;

public interface IViewContactAdd extends IView {
    void setPresenter(IPresenterContactAdd presenter);

    String getPhoneNumber();
    String getFirstName();
    String getLastName();

    void showView();
    void hideView();
    void clearFields();
}
