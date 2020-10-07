package com.example.smartgp.Model;

public class Location {
    private String coordinates;
    private String clinicID;
    private int postalCode;

    public Location() {
    }

    public Location(String coordinates, String clinicID, int postalCode) {
        this.coordinates = coordinates;
        this.clinicID = clinicID;
        this.postalCode = postalCode;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getClinicID() {
        return clinicID;
    }

    public void setClinicID(String clinicID) {
        this.clinicID = clinicID;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
}
