package Presenter;

import Model.Model;
import View.Forms.Modal.ViewEditContact;

public class PrEditContact implements IPresenter {
    Model model;
    ViewEditContact view;

    public PrEditContact(ViewEditContact view) {
        this.view = view;
        this.model = Model.getInstance();
        setListeners();
    }

    private void setListeners() {
        // TODO
    }
}
