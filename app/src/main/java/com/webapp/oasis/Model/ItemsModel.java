package com.webapp.oasis.Model;

public class ItemsModel {
    private String ItemID;
    private String ItemName;
    private String brandName;
    private String price;
    private String qty;

    public ItemsModel(String ItemName2, String brandName2, String qty2, String price2, String ItemId) {
        this.ItemID = ItemId;
        this.ItemName = ItemName2;
        this.brandName = brandName2;
        this.qty = qty2;
        this.price = price2;
    }

    public String getItemName() {
        return this.ItemName;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public String getQty() {
        return this.qty;
    }

    public String getPrice() {
        return this.price;
    }

    public String getItemID() {
        return this.ItemID;
    }
}
