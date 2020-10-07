package com.example.smartgp.FirebaseDatabaseHelper;

import androidx.annotation.NonNull;

import com.example.smartgp.Model.Patient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PatientDataController {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Patient> mPatients = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Patient> patients, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public PatientDataController() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Patient");
    }

    public void readPatients(final DataStatus dataStatus){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPatients.clear();
                List<String> keys = new ArrayList<>();

                //Datasnaphot keyNode: this object would contain the key
                //and the value of the specific node
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    //we add the patientID in one array
                    keys.add(keyNode.getKey());

                    //the datas under patientID will be stored in another array
                    Patient patient = keyNode.getValue(Patient.class);
                    mPatients.add(patient);
                }
                dataStatus.DataIsLoaded(mPatients, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addPatient(Patient patient, final DataStatus dataStatus){
        // key will be generated , so we store the key aka patientID
        String key = mReference.push().getKey();

        //here we have made a branch, so we add the rest of the values under the key(patientID)
        mReference.child(key).setValue(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }

    public void updatePatient(String key, Patient patient, final DataStatus dataStatus){
        mReference.child(key).setValue(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }

    public void deletePatient(String key, final DataStatus dataStatus){
        //having to setValue to null will automatically delete the data
        mReference.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }
}
