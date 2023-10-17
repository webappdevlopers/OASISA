package com.webapp.oasis.Model;

public class CustomerAllListModel {
    private String name;
    private String mobileNumber;
    private String email;
    private String address;
    private String machineModel;
    private String machineMake;

    public CustomerAllListModel(String name, String mobileNumber, String email, String address, String machineModel, String machineMake) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.address = address;
        this.machineModel = machineModel;
        this.machineMake = machineMake;
    }

    // Getters and setters for each field
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(String machineModel) {
        this.machineModel = machineModel;
    }

    public String getMachineMake() {
        return machineMake;
    }

    public void setMachineMake(String machineMake) {
        this.machineMake = machineMake;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
