package com.example.smartgp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.smartgp.Model.Admin;
import com.example.smartgp.Model.Eform;
import com.example.smartgp.Model.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EformActivity extends AppCompatActivity {

    String declaration;
    String symptoms = "";
    String comments;
    String clinicName;
    String adminEmail;
    String adminName;
    String adminID;


    //symptoms boolean
    boolean fever = false;
    boolean cough = false;
    boolean sorethroat = false;
    boolean flu = false;
    boolean diarrhea = false;
    boolean vomit = false;

    //travel details
    boolean travelled = false;

    RelativeLayout countryDetails;
    EditText countryVisitted;
    EditText dateOfReturn;
    DatePickerDialog picker;
    EditText others;
    EditText comment;
    Button btnSubmit;

    //new eform for every submitted one
    Eform eform = new Eform();
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    String patientID;
    String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_eform);
        //allow layout to move up when keyboard is shown
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        countryDetails = (RelativeLayout) findViewById(R.id.travelDetails);
        countryVisitted = (EditText) findViewById(R.id.countryVisitted);
        dateOfReturn = (EditText) findViewById(R.id.dateOfReturn);
        dateOfReturn.setInputType(InputType.TYPE_NULL);
        others = (EditText) findViewById(R.id.others);
        comment = (EditText) findViewById(R.id.comments);
        btnSubmit = (Button) findViewById(R.id.submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmation();
            }
        });

        //get current user info
        mAuth = FirebaseAuth.getInstance();
        patientID = mAuth.getCurrentUser().getUid();

        reference = FirebaseDatabase.getInstance().getReference("Patient").child(patientID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Patient patient = dataSnapshot.getValue(Patient.class);
                patientName = patient.getPatientName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Intent intent = getIntent();
        clinicName = intent.getStringExtra("clinicName");
        adminName = intent.getStringExtra("adminName");
        adminEmail = intent.getStringExtra("adminEmail");
        adminID = intent.getStringExtra("adminID");

        System.out.println("Intents received: Clinic Name - " + clinicName + " Admin Name - " + adminName + " Admin Email - " + adminEmail);
    }

    private void confirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Submit e-form to clinic");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveEformToDatabase(); 
                    }
                });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveEformToDatabase() {

        //declaration
        if(travelled == false) {
            declaration = "Did not travel overseas for the last 14 days";
        }
        else {
            declaration = "Travelled overseas for the last 14 days, " + "Country of Visit: " + countryVisitted.getText() + ", Date of Return: " + dateOfReturn.getText();
        }

        //symptoms
        if(fever == true)
            symptoms += "Fever, ";
        if(cough == true)
            symptoms += "Cough, ";
        if(sorethroat == true)
            symptoms += "Sore Throat, ";
        if(flu == true)
            symptoms += "Flu, ";
        if(diarrhea == true)
            symptoms += "Diarrhea, ";
        if(vomit == true)
            symptoms += "Vomit, ";
        if(others.getText().toString() != "")
            symptoms += "Others: " + others.getText();

        comments = comment.getText().toString();
        if(comments == "") {
            comments = "None";
        }

        System.out.println("Symptoms: " + symptoms);
        System.out.println("Declaration: " + declaration);

        reference = FirebaseDatabase.getInstance().getReference("Eform").push();
        eform.setDeclaration(declaration);
        eform.setPatientID(patientID);
        eform.setSymptoms(symptoms);
        eform.setComment(comments);
        eform.setAdminUsername(adminEmail);
        eform.setAdminID(adminID);

        reference.setValue(eform).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    sendEmail(declaration, symptoms, comments);
                    finish();
                }
            }
        });
    }

    private void sendEmail(String declaration, String symptoms, String comment){
        StringBuffer message = new StringBuffer();

        message.append("Travel Declaration: " + declaration + "\n");
        message.append("Symptoms: " + symptoms + "\n");
        message.append("Additional comments: " + comment);

        String mMessage = message.toString();
        String mSubject = "E-form sent from " + patientName;
        String mEmail = adminEmail;
        System.out.println("Email " + adminEmail);

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);
        javaMailAPI.execute();
    }

    public void travelSelected(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioNo:
                if (checked)
                {
                    travelled = false;
                    countryDetails.setVisibility(View.GONE);
                    break;
                }
            case R.id.radioYes:
                if (checked)
                {
                    travelled = true;
                    countryDetails.setVisibility(View.VISIBLE);
                    dateOfReturn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar cldr = Calendar.getInstance();
                            int day = cldr.get(Calendar.DAY_OF_MONTH);
                            int month = cldr.get(Calendar.MONTH);
                            int year = cldr.get(Calendar.YEAR);
                            // date picker dialog
                            picker = new DatePickerDialog(EformActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            dateOfReturn.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                        }
                                    }, year, month, day);
                            picker.show();
                        }
                    });
                    break;
                }
        }
    }

    public void onCheckboxSymptoms(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked/unclicked
        switch(view.getId()) {
            case R.id.checkbox_fever:
                if (checked)
                    fever = true;
                else
                    fever = false;
                break;
            case R.id.checkbox_cough:
                if (checked)
                    cough = true;
                else
                    cough = false;
                break;
            case R.id.checkbox_sorethroat:
                if (checked)
                    sorethroat = true;
                else
                    sorethroat = false;
                break;
            case R.id.checkbox_flu:
                if (checked)
                    flu = true;
                else
                    flu = false;
                break;
            case R.id.checkbox_diarrhea:
                if (checked)
                    diarrhea = true;
                else
                    diarrhea = false;
                break;
            case R.id.checkbox_vomit:
                if (checked)
                    vomit = true;
                else
                    vomit = false;
                break;
        }
    }
}