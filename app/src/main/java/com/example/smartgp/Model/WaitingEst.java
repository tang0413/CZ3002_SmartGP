package com.example.smartgp.Model;

public class WaitingEst {
    private String clinicID;
    private int estimateTime;
    private int patientCount;

    public WaitingEst() {
    }

    public WaitingEst(String clinicID, int estimateTime, int patientCount) {
        this.clinicID = clinicID;
        this.estimateTime = estimateTime;
        this.patientCount = patientCount;
    }

    public String getClinicID() {
        return clinicID;
    }

    public void setClinicID(String clinicID) {
        this.clinicID = clinicID;
    }

    public int getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(int estimateTime) {
        this.estimateTime = estimateTime;
    }

    public int getPatientCount() {
        return patientCount;
    }

    public void setPatientCount(int patientCount) {
        this.patientCount = patientCount;
    }
}
