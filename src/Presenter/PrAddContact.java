package Presenter;

import Model.Model;
import View.Forms.Modal.ViewAddContact;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrAddContact implements IPresenter {
    Model model;
    ViewAddContact view;

    public PrAddContact(ViewAddContact view) {
        this.view = view;
        this.model = Model.getInstance();
        setListeners();
    }

    private void setListeners() {

    }
}
