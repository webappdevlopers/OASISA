package com.webapp.oasis.Model;

import java.util.ArrayList;

public class ReportComplaintModel {
    private String CustomerName;
    private String CustomerMobileNumber;
    private String Complaint;
    private String Date;
    private String Timing;
    private String Service;
    private String TechnicianName;
    private String TechnicianMobile;
    private String TechnicianEmail;
    private String ClosingDate;
    private String ClosingTime;
    private String TotalAmount;
    private String ItemsList;

    public ReportComplaintModel(String customerName, String customerMobileNumber, String complaint, String date, String timing, String service, String technicianName, String technicianMobile, String technicianEmail, String closingDate, String closingTime, String totalAmount, String itemsList) {
        CustomerName = customerName;
        CustomerMobileNumber = customerMobileNumber;
        Complaint = complaint;
        Date = date;
        Timing = timing;
        Service = service;
        TechnicianName = technicianName;
        TechnicianMobile = technicianMobile;
        TechnicianEmail = technicianEmail;
        ClosingDate = closingDate;
        ClosingTime = closingTime;
        TotalAmount = totalAmount;
        ItemsList = itemsList;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerMobileNumber() {
        return CustomerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        CustomerMobileNumber = customerMobileNumber;
    }

    public String getComplaint() {
        return Complaint;
    }

    public void setComplaint(String complaint) {
        Complaint = complaint;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTiming() {
        return Timing;
    }

    public void setTiming(String timing) {
        Timing = timing;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getTechnicianName() {
        return TechnicianName;
    }

    public void setTechnicianName(String technicianName) {
        TechnicianName = technicianName;
    }

    public String getTechnicianMobile() {
        return TechnicianMobile;
    }

    public void setTechnicianMobile(String technicianMobile) {
        TechnicianMobile = technicianMobile;
    }

    public String getTechnicianEmail() {
        return TechnicianEmail;
    }

    public void setTechnicianEmail(String technicianEmail) {
        TechnicianEmail = technicianEmail;
    }

    public String getClosingDate() {
        return ClosingDate;
    }

    public void setClosingDate(String closingDate) {
        ClosingDate = closingDate;
    }

    public String getClosingTime() {
        return ClosingTime;
    }

    public void setClosingTime(String closingTime) {
        ClosingTime = closingTime;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String  getItemsList() {
        return ItemsList;
    }

    public void setItemsList(String  itemsList) {
        ItemsList = itemsList;
    }
}
