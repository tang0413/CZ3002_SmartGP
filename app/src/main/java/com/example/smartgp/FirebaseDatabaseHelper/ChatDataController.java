package com.example.smartgp.FirebaseDatabaseHelper;

import androidx.annotation.NonNull;

import com.example.smartgp.Model.Chat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatDataController {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Chat> mChat = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Chat> chats, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public ChatDataController() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Chat");
    }

    public void readChats(final DataStatus dataStatus){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                List<String> keys = new ArrayList<>();

                //Datasnaphot keyNode: this object would contain the key and the value of the specific node
                for (DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Chat chat = keyNode.getValue(Chat.class);
                    mChat.add(chat);
                }

                dataStatus.DataIsLoaded(mChat, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addChat(Chat chat, final DataStatus dataStatus){
        //key will be auto generated, store to variable
        String key = mReference.push().getKey();

        mReference.child(key).setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void updateChat(String key, Chat chat, final DataStatus dataStatus){
        mReference.child(key).setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteChat(String key, final DataStatus dataStatus){
        mReference.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
