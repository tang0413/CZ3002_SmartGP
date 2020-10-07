package com.example.smartgp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgp.Model.Patient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInController extends AppCompatActivity {
    EditText PatID,PatName,PatEmail,PatBloodType,PatAllergy;
    Button AddPatient,Back;
    Patient patient;
    FirebaseDatabase root;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patient = new Patient();
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("Patient");
        AddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int id= Integer.parseInt(PatID.getText().toString().trim());
                patient.setPatientId(PatID.getText().toString().trim());
                patient.setPatientName(PatName.getText().toString().trim());
                patient.setBloodType(PatBloodType.getText().toString().trim());
                patient.setPatientEmail(PatEmail.getText().toString().trim());
                reference.push().setValue(patient);
            }
        });



    }
}