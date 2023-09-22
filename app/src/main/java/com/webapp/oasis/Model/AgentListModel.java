package com.webapp.oasis.Model;

public class AgentListModel {
    private String AdhaarCard;
    private String License;
    private String Technician_Password;
    private String email;
    private String mobile;
    private String name;
    private String technician_id;

    private String isDeleted;

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getTechnician_id() {
        return this.technician_id;
    }

    public void setTechnician_id(String technician_id2) {
        this.technician_id = technician_id2;
    }

    public AgentListModel(String name2, String mobile2, String technician_Password, String email2, String adhaarCard, String license, String technician_id2, String isDeleted) {
        this.name = name2;
        this.mobile = mobile2;
        this.Technician_Password = technician_Password;
        this.email = email2;
        this.AdhaarCard = adhaarCard;
        this.License = license;
        this.technician_id = technician_id2;
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile2) {
        this.mobile = mobile2;
    }

    public String getTechnician_Password() {
        return this.Technician_Password;
    }

    public void setTechnician_Password(String technician_Password) {
        this.Technician_Password = technician_Password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getAdhaarCard() {
        return this.AdhaarCard;
    }

    public void setAdhaarCard(String adhaarCard) {
        this.AdhaarCard = adhaarCard;
    }

    public String getLicense() {
        return this.License;
    }

    public void setLicense(String license) {
        this.License = license;
    }
}
