package com.example.smartgp.FirebaseDatabaseHelper;

import androidx.annotation.NonNull;

import com.example.smartgp.Model.Eform;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EformDataController {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Eform> mEforms  = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Eform> eforms, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public EformDataController() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Eform");
    }

    public void readEforms(final DataStatus dataStatus){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mEforms.clear();
                List<String> keys = new ArrayList<>();

                //Datasnaphot keyNode: this object would contain the key and the value of the specific node
                for (DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Eform eform = keyNode.getValue(Eform.class);
                    mEforms.add(eform);
                }
                dataStatus.DataIsLoaded(mEforms, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addEform(Eform eform, final DataStatus dataStatus){
        // key will be geerated, so we store the key
        String key = mReference.push().getKey();

        mReference.child(key).setValue(eform).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void updateEform(String key, Eform eform, final DataStatus dataStatus){
        mReference.child(key).setValue(eform)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }

    public void deleteEform(String key, final DataStatus dataStatus){
        mReference.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }
}
