package com.example.smartgp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartgp.MessageActivity;
import com.example.smartgp.Model.Admin;
import com.example.smartgp.Model.Chat;
import com.example.smartgp.Model.Patient;
import com.example.smartgp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<Admin> mAdmins;
    private boolean ischat;

    String theLastMessage;
    String theLastMessageTime;

    public UserAdapter(Context mContext, List<Admin> mAdmins, boolean ischat){
        this.mAdmins = mAdmins;
        this.mContext = mContext;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Admin admin = mAdmins.get(position);
        System.out.println("What is the username retrieved " + admin.getAdminName());
        holder.username.setText(admin.getClinicName());
        holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        if (ischat) {
            lastMessage(admin.getAdminId(), holder.last_msg, holder.last_msg_time);
        }
        else {
            holder.last_msg.setVisibility(View.GONE);
        }

        //if click into the specific userid from the list of messages
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", admin.getAdminId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdmins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;
        private TextView last_msg;
        private TextView last_msg_time;


        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            last_msg = itemView.findViewById(R.id.last_msg);
            last_msg_time = itemView.findViewById(R.id.last_msg_time);
        }
    }

//    //check for last message
    private void lastMessage(final String patientid, final TextView last_msg, final TextView last_msg_time) {
        theLastMessage = "default";
        theLastMessage = "-";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");


        if(firebaseUser != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(patientid) ||
                                chat.getReceiver().equals(patientid) && chat.getSender().equals(firebaseUser.getUid())){
                            theLastMessage = chat.getMessage();
                            theLastMessageTime = chat.getMessageTime();
                        }
                    }

                    switch (theLastMessage) {
                        case "default":
                            last_msg.setText("No message");
                            break;

                        default:
                            last_msg.setText(theLastMessage);
                            break;
                    }

                    switch (theLastMessageTime) {
                        case "-":
                            last_msg_time.setText("");
                            break;

                        default:
                            last_msg_time.setText(theLastMessageTime);
                            break;
                    }

                    theLastMessage = "default";
                    theLastMessageTime = "-";
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
