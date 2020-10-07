package com.example.smartgp.FirebaseDatabaseHelper;

import androidx.annotation.NonNull;

import com.example.smartgp.Model.WaitingEst;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WaitingEstDataController {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<WaitingEst> mWait = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<WaitingEst> waitingEsts, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public WaitingEstDataController() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("WaitingEstimated");
    }

    public void readWaitingEst(final DataStatus dataStatus){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mWait.clear();
                List<String> keys = new ArrayList<>();

                //Datasnaphot keyNode: this object would contain the key and the value of the specific node
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    WaitingEst waitingEst = keyNode.getValue(WaitingEst.class);
                    mWait.add(waitingEst);
                }
                dataStatus.DataIsLoaded(mWait, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addWaitingEst(WaitingEst waitingEst, final DataStatus dataStatus){
        // key will be auto generated. It will be stored in a variable
        String key = mReference.push().getKey();

        //data will be stored under the key
        mReference.child(key).setValue(waitingEst).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void updateWitingEst(String key, WaitingEst waitingEst, final DataStatus dataStatus){
        mReference.child(key).setValue(waitingEst).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteWaitingEst(String key, final DataStatus dataStatus){
        mReference.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
