package com.webapp.oasis.Model;

import java.io.Serializable;

public class ComplaintStatusModel implements Serializable {
    String CustomerID;
    String complaintId;
    private String complaint;
    private String date;
    private String destination;
    private String endDate;
    String image;
    private String parcel_img;
    private String qty;
    private String receiver_address;
    private String service;
    private String source;
    private String startDate;
    private String status;
    private String timing;
    private String weight;
    private String CustomerMobileNumber;
    private String CustomerName;
    private String TechnicianRemark;

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate2) {
        this.startDate = startDate2;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate2) {
        this.endDate = endDate2;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image2) {
        this.image = image2;
    }

    public ComplaintStatusModel(String Complaint, String service2, String status2, String Date, String timing2, String image2, String startDate2, String endDate2, String CustomerID, String complaintId, String CustomerName, String
            CustomerMobileNumber, String TechnicianRemark) {
        this.complaint = Complaint;
        this.service = service2;
        this.date = Date;
        this.timing = timing2;
        this.status = status2;
        this.image = image2;
        this.startDate = startDate2;
        this.endDate = endDate2;
        this.CustomerID = CustomerID;
        this.complaintId = complaintId;
        this.CustomerName = CustomerName;
        this.CustomerMobileNumber = CustomerMobileNumber;
        this.TechnicianRemark = TechnicianRemark;
    }

    public String getTechnicianRemark() {
        return TechnicianRemark;
    }

    public void setTechnicianRemark(String technicianRemark) {
        TechnicianRemark = technicianRemark;
    }

    public String getCustomerMobileNumber() {
        return CustomerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        CustomerMobileNumber = customerMobileNumber;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
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

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination2) {
        this.destination = destination2;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight2) {
        this.weight = weight2;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date2) {
        this.date = date2;
    }

    public String getComplaint() {
        return this.complaint;
    }

    public void setComplaint(String complaint2) {
        this.complaint = complaint2;
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

    public String getQty() {
        return this.qty;
    }

    public void setQty(String qty2) {
        this.qty = qty2;
    }

    public String getParcel_img() {
        return this.parcel_img;
    }

    public void setParcel_img(String parcel_img2) {
        this.parcel_img = parcel_img2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }
}
