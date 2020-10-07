package com.example.smartgp.Model;

public class Patient {
    private String patientId;
    private String patientName;
    private String patientDob;
    private String patientAddress;
    private String patientGender;
    private String patientEmail;
    private String bloodType;
    private String patientAllergy;

    public Patient(){

    }

    public Patient(String patientId, String patientName, String patientDob, String patientAddress, String patientGender, String patientEmail, String bloodType, String patientAllergy) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientDob = patientDob;
        this.patientAddress = patientAddress;
        this.patientGender = patientGender;
        this.patientEmail = patientEmail;
        this.bloodType = bloodType;
        this.patientAllergy = patientAllergy;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getPatientAllergy() {
        return patientAllergy;
    }

    public void setPatientAllergy(String patientAllergy) {
        this.patientAllergy = patientAllergy;
    }

    public String getPatientDob() {
        return patientDob;
    }

    public void setPatientDob(String patientDob) {
        this.patientDob = patientDob;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

}
