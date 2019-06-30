package Presenter;

import Model.Model;
import View.Forms.Modal.ViewEditProfile;

public class PrEditProfile implements IPresenter {
    Model model;
    ViewEditProfile view;

    public PrEditProfile(ViewEditProfile view) {
        this.view = view;
        this.model = Model.getInstance();
        setListeners();
    }

    private void setListeners() {
        // TODO
    }
}
