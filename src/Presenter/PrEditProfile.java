package Presenter;

import Presenter.Interface.IPresenterChat;
import Presenter.Interface.IPresenterProfileEdit;
import View.Interface.IViewProfileEdit;
import org.javagram.response.object.User;

public class PrEditProfile implements IPresenterProfileEdit {
    private IViewProfileEdit view;
    private IPresenterChat prChat;
    private User user;

    public PrEditProfile(IViewProfileEdit view, IPresenterChat prChat) {
        this.view = view;
        this.prChat = prChat;
        this.user = model.getSelfUser();
        view.setUserInfo(user.getFirstName(), user.getLastName(), user.getPhone());
    }

    @Override
    public void saveProfile() {
        String newFirstName = view.getFirstName();
        String newLastName = view.getLastName();
        User updatedUser = model.updateProfileInfo(newFirstName, newLastName);
        user = updatedUser;
        prChat.updateDialogName(updatedUser);
        System.err.println("EditProfile Сохранить");
        view.getRootPanel().setVisible(false);
    }

    @Override
    public void logout() {
        model.logOut();
    }

}
