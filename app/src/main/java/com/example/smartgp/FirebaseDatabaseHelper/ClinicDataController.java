package com.example.smartgp.FirebaseDatabaseHelper;

import androidx.annotation.NonNull;

import com.example.smartgp.Model.Clinic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClinicDataController {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Clinic> mClinics = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Clinic> clinics, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public ClinicDataController() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Clinic");
    }

    public void readClinics(final DataStatus dataStatus){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mClinics.clear();
                List<String> keys = new ArrayList<>();

                //Datasnaphot keyNode: this object would contain the key and the value of the specific node
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Clinic clinic = keyNode.getValue(Clinic.class);
                    mClinics.add(clinic);
                }
                dataStatus.DataIsLoaded(mClinics, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addClinic(Clinic clinic, final DataStatus dataStatus){
        // key will be auto generated. It will then be stored in a variable
        String key = mReference.push().getKey();

        mReference.child(key).setValue(clinic).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void updateClinic(String key, Clinic clinic, final DataStatus dataStatus){
        mReference.child(key).setValue(clinic).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteClinic(String key, final DataStatus dataStatus){
        mReference.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
