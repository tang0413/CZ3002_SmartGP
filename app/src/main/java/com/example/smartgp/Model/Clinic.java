package com.example.smartgp.Model;

public class Clinic {
    private String clinicID;
    private String clinicName;
    private String address;
    private String clinicDescription;
    private String openingHour;
    public Clinic(){}

    public Clinic(String clinicID, String clinicName, String address, String clinicDescription, String openingHour) {
        this.clinicID = clinicID;
        this.clinicName = clinicName;
        this.address = address;
        this.clinicDescription = clinicDescription;
        this.openingHour = openingHour;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClinicDescription() {
        return clinicDescription;
    }

    public void setClinicDescription(String clinicDescription) {
        this.clinicDescription = clinicDescription;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }
}
