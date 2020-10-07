package com.example.smartgp.Model;

public class Admin {
    private String adminId;
    private String adminName;
    private String adminUsername;
    private String adminPw;
    private String clinicID;
    private String clinicName;

    public Admin(){}

    public Admin(String adminId, String adminName, String adminUsername, String adminPw, String clinicID, String clinicName) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminUsername = adminUsername;
        this.adminPw = adminPw;
        this.clinicID = clinicID;
        this.clinicName = clinicName;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPw() {
        return adminPw;
    }

    public void setAdminPw(String adminPw) {
        this.adminPw = adminPw;
    }

    public String getClinicID() {
        return clinicID;
    }

    public void setClinicID(String clinicID) {
        this.clinicID = clinicID;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }
}
