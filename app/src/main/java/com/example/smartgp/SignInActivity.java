package com.example.smartgp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgp.Model.Patient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private SignInButton signInButton;
    private Button btnsignOutButton;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "SignInActivity";
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser fuser;

    //new patient object for new user
    Patient patient = new Patient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInButton = findViewById(R.id.sign_in_btn);
        mAuth = FirebaseAuth.getInstance();
        btnsignOutButton = findViewById(R.id.sign_out_btn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btnsignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Toast.makeText(SignInActivity.this, "Sign Out Successful", Toast.LENGTH_SHORT).show();
                btnsignOutButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        System.out.println("handleABC");
        try{
            System.out.println("log try");
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(SignInActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
            System.out.println("Acc ID " + acc.getIdToken());
            FirebaseGoogleAuth(acc);
        }
        catch(ApiException e) {
            System.out.println("log catch");
            Toast.makeText(SignInActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            System.out.println("FAIL"+e.getStatusCode());
            //FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct){
        System.out.println("googleauth" + acct.getIdToken());
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();

                    //check whether new user
                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                    Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));
                    //new user have to profile activity
                    if(isNew)
                    {
                        newUser(user);
                    }
                    else{
                        //old user redirect to main activity
                        oldUser(user);
                    }
                }
                else{
                    Toast.makeText(SignInActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    oldUser(null);
                }
            }
        });
    }

    private void oldUser(FirebaseUser fUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount((getApplicationContext()));
        if(account != null) {
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Toast.makeText(SignInActivity.this, "Welcome back, " + personName, Toast.LENGTH_SHORT).show();

            //redirect to main activity
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void newUser(FirebaseUser fUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount((getApplicationContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = fuser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Patient").child(userID);

        String personName = account.getDisplayName();
        String personEmail = account.getEmail();

        patient.setPatientName(personName);
        patient.setPatientEmail(personEmail);
        //reference.push().setValue(patient);

        reference.setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(SignInActivity.this, EditProfileActivity.class);
                    Toast.makeText(SignInActivity.this, "Fill in your particulars", Toast.LENGTH_SHORT).show();
                    intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
