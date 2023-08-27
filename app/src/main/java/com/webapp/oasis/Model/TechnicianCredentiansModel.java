package com.webapp.oasis.Model;

public class TechnicianCredentiansModel {
    private String Mobile;
    private String password;

    public TechnicianCredentiansModel(String mobile, String password2) {
        this.Mobile = mobile;
        this.password = password2;
    }

    public String getMobile() {
        return this.Mobile;
    }

    public void setMobile(String mobile) {
        this.Mobile = mobile;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }
}
