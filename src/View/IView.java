package View;

import Presenter.Interface.IPresenter;

import javax.swing.*;

public interface IView {
    JComponent getRootPanel();
    void setPresenter(IPresenter presenter);
}
