package View.Interface;

import Presenter.Interface.IPresenterContactEdit;

public interface IViewContactEdit extends IView {
    void setPresenter(IPresenterContactEdit presenter);
    String getName();

    void showView();
    void hideView();

    void setContactInfo(String firstName, String lastName, int userId);
}
