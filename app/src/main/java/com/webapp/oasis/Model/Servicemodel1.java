package com.webapp.oasis.Model;

public class Servicemodel1 {


    //service list

    private String serviceId;
    private String name;
    private String img;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    //order list
    private String order_id;
    private String order_type;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    //service type
    private String service_type_name;
    private String service_type_id;
    private String service_img;

    public String getService_type_name() {
        return service_type_name;
    }

    public void setService_type_name(String service_type_name) {
        this.service_type_name = service_type_name;
    }

    public String getService_type_id() {
        return service_type_id;
    }

    public void setService_type_id(String service_type_id) {
        this.service_type_id = service_type_id;
    }

    public String getService_img() {
        return service_img;
    }

    public void setService_img(String service_img) {
        this.service_img = service_img;
    }


    //service item
    private String service_item_id;
    private String service_item_name;
    private String service_item_img;
    private String service_item_price;
    private String service_type_item_id;

    public String getService_type_item_id() {
        return service_type_item_id;
    }

    public void setService_type_item_id(String service_type_item_id) {
        this.service_type_item_id = service_type_item_id;
    }

    public String getService_item_id() {
        return service_item_id;
    }

    public void setService_item_id(String service_item_id) {
        this.service_item_id = service_item_id;
    }

    public String getService_item_name() {
        return service_item_name;
    }

    public void setService_item_name(String service_item_name) {
        this.service_item_name = service_item_name;
    }

    public String getService_item_img() {
        return service_item_img;
    }

    public void setService_item_img(String service_item_img) {
        this.service_item_img = service_item_img;
    }

    public String getService_item_price() {
        return service_item_price;
    }

    public void setService_item_price(String service_item_price) {
        this.service_item_price = service_item_price;
    }


    //getMycart
    public String id;
    public String category;
    public String service_item_name1;
    public String img1;
    public String service_type_name1;
    public String qty;
    public String price;
    public String total_amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getService_item_name1() {
        return service_item_name1;
    }

    public void setService_item_name1(String service_item_name1) {
        this.service_item_name1 = service_item_name1;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getService_type_name1() {
        return service_type_name1;
    }

    public void setService_type_name1(String service_type_name1) {
        this.service_type_name1 = service_type_name1;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }
}
