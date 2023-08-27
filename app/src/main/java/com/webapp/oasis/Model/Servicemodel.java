package com.webapp.oasis.Model;

public class Servicemodel {


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
    private String service_cityid;
    private String service_qty;

    public String getService_qty() {
        return service_qty;
    }

    public void setService_qty(String service_qty) {
        this.service_qty = service_qty;
    }

    public String getService_cityid() {
        return service_cityid;
    }

    public void setService_cityid(String service_cityid) {
        this.service_cityid = service_cityid;
    }

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



    //Myorder

    public String count;
    public String date;
    public String paymentmode;
    public String price1;
    public String tltitem;
    public String status;
    public String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getTltitem() {
        return tltitem;
    }

    public void setTltitem(String tltitem) {
        this.tltitem = tltitem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String ordno;



    //package
    public String pak_id;
    public String pak_name;
    public String pak_baglimit;
    public String pak_desc;
    public String pak_amt;
    public String pak_status;
    public String pak_size;

    public String getPak_id() {
        return pak_id;
    }

    public void setPak_id(String pak_id) {
        this.pak_id = pak_id;
    }

    public String getPak_name() {
        return pak_name;
    }

    public void setPak_name(String pak_name) {
        this.pak_name = pak_name;
    }

    public String getPak_baglimit() {
        return pak_baglimit;
    }

    public void setPak_baglimit(String pak_baglimit) {
        this.pak_baglimit = pak_baglimit;
    }

    public String getPak_desc() {
        return pak_desc;
    }

    public void setPak_desc(String pak_desc) {
        this.pak_desc = pak_desc;
    }

    public String getPak_amt() {
        return pak_amt;
    }

    public void setPak_amt(String pak_amt) {
        this.pak_amt = pak_amt;
    }

    public String getPak_status() {
        return pak_status;
    }

    public void setPak_status(String pak_status) {
        this.pak_status = pak_status;
    }

    public String getPak_size() {
        return pak_size;
    }

    public void setPak_size(String pak_size) {
        this.pak_size = pak_size;
    }



    //my subscription list
    public String sub_date;
    public String sub_bagord;
    public String sub_ordstatus;
    public String sub_bagleft;

    public String getSub_date() {
        return sub_date;
    }

    public void setSub_date(String sub_date) {
        this.sub_date = sub_date;
    }

    public String getSub_bagord() {
        return sub_bagord;
    }

    public void setSub_bagord(String sub_bagord) {
        this.sub_bagord = sub_bagord;
    }

    public String getSub_ordstatus() {
        return sub_ordstatus;
    }

    public void setSub_ordstatus(String sub_ordstatus) {
        this.sub_ordstatus = sub_ordstatus;
    }

    public String getSub_bagleft() {
        return sub_bagleft;
    }

    public void setSub_bagleft(String sub_bagleft) {
        this.sub_bagleft = sub_bagleft;
    }

    //my wallet

    public String wdate;
    public String wtime;
    public String wamount;
    public String wdescription;
    public String wid;

    public String getWdate() {
        return wdate;
    }

    public void setWdate(String wdate) {
        this.wdate = wdate;
    }

    public String getWtime() {
        return wtime;
    }

    public void setWtime(String wtime) {
        this.wtime = wtime;
    }

    public String getWamount() {
        return wamount;
    }

    public void setWamount(String wamount) {
        this.wamount = wamount;
    }

    public String getWdescription() {
        return wdescription;
    }

    public void setWdescription(String wdescription) {
        this.wdescription = wdescription;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }
}
