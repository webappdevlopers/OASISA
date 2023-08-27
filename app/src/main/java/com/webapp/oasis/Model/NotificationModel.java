package com.webapp.oasis.Model;

public class NotificationModel {

    private String id,driver_id,place,vehical_np,date,name,status;
    private  int sadmin_approve_status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getVehical_np() {
        return vehical_np;
    }

    public void setVehical_np(String vehical_np) {
        this.vehical_np = vehical_np;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSadmin_approve_status() {
        return sadmin_approve_status;
    }

    public void setSadmin_approve_status(int sadmin_approve_status) {
        this.sadmin_approve_status = sadmin_approve_status;
    }
}
