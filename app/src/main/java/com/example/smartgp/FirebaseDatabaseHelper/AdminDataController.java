package com.example.smartgp.FirebaseDatabaseHelper;

import androidx.annotation.NonNull;

import com.example.smartgp.Model.Admin;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDataController {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Admin> mAdmins = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Admin> admins, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public AdminDataController() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Admin");
    }

    public void readAdmin(final DataStatus dataStatus){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAdmins.clear();
                List<String> keys = new ArrayList<>();

                //Datasnaphot keyNode: this object would contain the key and the value of the specific node
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Admin admin = keyNode.getValue(Admin.class);
                    mAdmins.add(admin);
                }
                dataStatus.DataIsLoaded(mAdmins, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addAdmin(Admin admin, final DataStatus dataStatus){
        //key will be generated, so we store the key in a variable
        String key = mReference.push().getKey();

        mReference.child(key).setValue(admin).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void updateAdmin(String key, Admin admin, final DataStatus dataStatus){
        mReference.child(key).setValue(admin).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteAdmin(String key, final DataStatus dataStatus){
        mReference.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
