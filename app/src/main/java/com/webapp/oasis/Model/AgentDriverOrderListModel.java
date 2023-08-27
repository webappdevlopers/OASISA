package com.webapp.oasis.Model;

import java.io.Serializable;

public class AgentDriverOrderListModel implements Serializable {
    String CustomerID;
    String CustomerMobileNumber;
    String CustomertName;
    private String amount;
    String complaint;
    String complaintId;
    String date;
    private String destination;
    private String driver_name;
    private String closingTime,closingDate,TechnicianRemark;

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    /* renamed from: id */
    private String f29id;
    String image;
    private String mobile_no;
    private String order_id;
    private String parcel_img;
    private String qty;
    private String receiver_address;
    private String receiver_name;
    String service;
    private String source;
    String status;
    String timing;
    private String user_lat;
    private String user_lon;
    private String weight;

    public String getCustomertName() {
        return this.CustomertName;
    }

    public void setCustomertName(String customertName) {
        this.CustomertName = customertName;
    }

    public String getCustomerMobileNumber() {
        return this.CustomerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.CustomerMobileNumber = customerMobileNumber;
    }

    public String getComplaint() {
        return this.complaint;
    }

    public void setComplaint(String complaint2) {
        this.complaint = complaint2;
    }

    public String getService() {
        return this.service;
    }

    public void setService(String service2) {
        this.service = service2;
    }

    public String getTiming() {
        return this.timing;
    }

    public void setTiming(String timing2) {
        this.timing = timing2;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date2) {
        this.date = date2;
    }

    public String getCustomerID() {
        return this.CustomerID;
    }

    public void setCustomerID(String customerID) {
        this.CustomerID = customerID;
    }

    public String getComplaintId() {
        return this.complaintId;
    }

    public void setComplaintId(String complaintId2) {
        this.complaintId = complaintId2;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image2) {
        this.image = image2;
    }

    public AgentDriverOrderListModel() {
    }


    public AgentDriverOrderListModel(String customerName, String CustomerMobileNumber2, String complaint2, String service2, String status2, String image2, String timing2, String date2, String complaintId2, String CustomerID2, String closingDate, String closingTime, String TechnicianRemark) {
        this.CustomertName = customerName;
        this.CustomerMobileNumber = CustomerMobileNumber2;
        this.complaintId = complaintId2;
        this.complaint = complaint2;
        this.CustomerID = CustomerID2;
        this.service = service2;
        this.timing = timing2;
        this.date = date2;
        this.status = status2;
        this.image = image2;
        this.closingDate = closingDate;
        this.closingTime = closingTime;
        this.TechnicianRemark = TechnicianRemark;
    }

    public String getTechnicianRemark() {
        return TechnicianRemark;
    }

    public void setTechnicianRemark(String technicianRemark) {
        TechnicianRemark = technicianRemark;
    }

    public String getId() {
        return this.f29id;
    }

    public void setId(String id) {
        this.f29id = id;
    }

    public String getOrder_id() {
        return this.order_id;
    }

    public void setOrder_id(String order_id2) {
        this.order_id = order_id2;
    }

    public String getReceiver_name() {
        return this.receiver_name;
    }

    public void setReceiver_name(String receiver_name2) {
        this.receiver_name = receiver_name2;
    }

    public String getMobile_no() {
        return this.mobile_no;
    }

    public void setMobile_no(String mobile_no2) {
        this.mobile_no = mobile_no2;
    }

    public String getReceiver_address() {
        return this.receiver_address;
    }

    public void setReceiver_address(String receiver_address2) {
        this.receiver_address = receiver_address2;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source2) {
        this.source = source2;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination2) {
        this.destination = destination2;
    }

    public String getQty() {
        return this.qty;
    }

    public void setQty(String qty2) {
        this.qty = qty2;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight2) {
        this.weight = weight2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getParcel_img() {
        return this.parcel_img;
    }

    public void setParcel_img(String parcel_img2) {
        this.parcel_img = parcel_img2;
    }

    public String getUser_lat() {
        return this.user_lat;
    }

    public void setUser_lat(String user_lat2) {
        this.user_lat = user_lat2;
    }

    public String getUser_lon() {
        return this.user_lon;
    }

    public void setUser_lon(String user_lon2) {
        this.user_lon = user_lon2;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount2) {
        this.amount = amount2;
    }

    public String getDriver_name() {
        return this.driver_name;
    }

    public void setDriver_name(String driver_name2) {
        this.driver_name = driver_name2;
    }
}
