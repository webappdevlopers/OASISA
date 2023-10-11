package com.webapp.oasis.Model;

public class AdminItemListModel {
    private String itemID;
    private String itemName;
    private String brandName;
    private String price;
    private String itemQty;
    private String qty;

    public AdminItemListModel(String itemName, String brandName, String qty, String price, String itemQty, String itemID) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.brandName = brandName;
        this.qty = qty;
        this.price = price;
        this.itemQty = itemQty;
    }
    public AdminItemListModel(String brandName,String itemName, String price, String itemQty) {
        this.itemName = itemName;
        this.brandName = brandName;
        this.price = price;
        this.qty = itemQty;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public AdminItemListModel() {
    }
    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getItemName() {
        return itemName;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getQty() {
        return qty;
    }

    public String getPrice() {
        return price;
    }

    public String getItemID() {
        return itemID;
    }

}
