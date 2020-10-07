package com.example.smartgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    DatePickerDialog picker;
    EditText patientName;
    EditText patientDob;
    EditText patientAddress;
    EditText patientAllergies;
    private Spinner patientGender;
    private Spinner patientBloodtype;
    Button btnSaveChanges;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDb;
    private String userId, userName, userDob, userAddress, userAllergies, userGender, userBloodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //allow layout to move up when keyboard is shown
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        patientName = (EditText) findViewById(R.id.name);
        patientAddress = (EditText) findViewById(R.id.address);
        patientAllergies = (EditText) findViewById(R.id.allergies);
        patientGender = (Spinner) findViewById(R.id.gender);
        patientGender.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        patientBloodtype = (Spinner) findViewById(R.id.bloodType);
        patientBloodtype.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        btnSaveChanges = (Button) findViewById(R.id.save);
        //if save btn clicked
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        patientDob =(EditText) findViewById(R.id.dob);
        patientDob.setInputType(InputType.TYPE_NULL);
        patientDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EditProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                patientDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        //get current user info
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();


        mUserDb = FirebaseDatabase.getInstance().getReference().child("Patient").child(userId);
        getUserInfo();
    }

    private void getUserInfo(){
        mUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("patientName")!= null){
                        userName = map.get("patientName").toString();
                        patientName.setText(userName);
                    }
                    if(map.get("patientDob")!= null){
                        userDob = map.get("patientDob").toString();
                        patientDob.setText(userDob);
                    }
                    if(map.get("patientAddress")!= null){
                        userAddress = map.get("patientAddress").toString();
                        patientAddress.setText(userAddress);
                    }
                    if(map.get("allergy")!= null){
                        userAllergies = map.get("allergy").toString();
                        patientAllergies.setText(userAllergies);
                    }
                    if(map.get("patientGender")!= null){
                         userGender = map.get("gender").toString();
                        if(userGender.equals("Male")){
                            patientGender.setSelection(1);
                        }
                        else{
                            patientGender.setSelection(0);
                        }
                    }
                    if(map.get("bloodType")!= null){
                        userBloodType = map.get("bloodType").toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void saveUserInformation() {
        userName = patientName.getText().toString();
        userAddress = patientAddress.getText().toString();
        userGender = String.valueOf(patientGender.getSelectedItem());
        userBloodType = String.valueOf(patientBloodtype.getSelectedItem());
        userAllergies = patientAllergies.getText().toString();
        userDob = patientDob.getText().toString();

        if (userName == "" || userAddress == "" || userGender == "" || userBloodType == "" || userAllergies == "" || userDob == "") {
            Toast.makeText(EditProfileActivity.this, "Please do not leave any fills blank", Toast.LENGTH_SHORT).show();
        }

        else {
            Map userInfo = new HashMap();
            userInfo.put("patientName", userName);
            userInfo.put("patientAddress", userAddress);
            userInfo.put("patientGender", userGender);
            userInfo.put("bloodType", userBloodType);
            userInfo.put("allergy", userAllergies);
            userInfo.put("patientDob", userDob);

            mUserDb.updateChildren(userInfo);

            Toast.makeText(EditProfileActivity.this, "Changes saved!", Toast.LENGTH_SHORT).show();
        }

    }

}
