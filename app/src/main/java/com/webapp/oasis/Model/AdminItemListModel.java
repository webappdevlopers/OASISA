package com.webapp.oasis.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminItemListModel {
    private String ItemID;
    private String ItemName;
    private String BrandName;
    private String Price;
    private String ItemQty;
    private String Qty;

    public AdminItemListModel(String ItemName2, String brandName2, String qty2, String price2, String ItemQty, String ItemId) {
        this.ItemID = ItemId;
        this.ItemName = ItemName2;
        this.BrandName = brandName2;
        this.Qty = qty2;
        this.Price = price2;
        this.ItemQty = ItemQty;
    }
    public AdminItemListModel(String brandName2,String ItemName2, String price2, String ItemQty) {
        this.ItemName = ItemName2;
        this.BrandName = brandName2;
        this.Price = price2;
        this.Qty = ItemQty;
    }

    public String getItemQty() {
        return ItemQty;
    }

    public void setItemQty(String itemQty) {
        ItemQty = itemQty;
    }

    public AdminItemListModel() {
    }
    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getItemName() {
        return this.ItemName;
    }

    public String getBrandName() {
        return this.BrandName;
    }

    public String getQty() {
        return this.Qty;
    }

    public String getPrice() {
        return this.Price;
    }

    public String getItemID() {
        return this.ItemID;
    }

    @Override
    public String toString() {
        return  ", BrandName='" + BrandName + '\'' +
                "ItemName='" + ItemName + '\'' +
                ", Price='" + Price + '\'' +
                ", ItemQty='" + ItemQty + '\'' ;
    }
}
