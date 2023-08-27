package com.webapp.oasis.Model;

public class CartModel {

    private String _id, qty;

    public CartModel(String _id, String qty) {
        this._id = _id;
        this.qty = qty;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
