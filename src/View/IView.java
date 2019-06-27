package View;

import Presenter.IPresenter;

import javax.swing.*;

public interface IView {
    JComponent getRootPanel();
    void setPresenter(IPresenter presenter);
}
