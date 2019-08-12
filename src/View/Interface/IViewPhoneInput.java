package View.Interface;

import Presenter.Interface.IPresenterPhoneInput;

public interface IViewPhoneInput extends IView{

    void setPresenter(IPresenterPhoneInput presenter);
    void clearPhoneField();
    void showSMSCheckView();
}
