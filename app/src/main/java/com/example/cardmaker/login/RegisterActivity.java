package com.example.cardmaker.login;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cardmaker.R;
import com.example.cardmaker.models.PersonInfo;
import com.example.cardmaker.utils.FirebaseMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private String email, username, password, confirmPassword;
    private EditText mEmail, mPassword, mUsername, mConfirmPassword;
    private TextView loadingPleaseWait;
    private Button btnRegister;
    private ProgressBar mProgressBar;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseMethod firebaseMethod;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = RegisterActivity.this;

        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        mEmail = (EditText) findViewById(R.id.input_email);
        mUsername = (EditText) findViewById(R.id.input_username);
        btnRegister = (Button) findViewById(R.id.btn_Register);
        mProgressBar = (ProgressBar) findViewById(R.id.registerRequestLoadingProgressBar);
        loadingPleaseWait = (TextView) findViewById(R.id.loadingPleaseWait);
        mPassword = (EditText) findViewById(R.id.input_password);
        mConfirmPassword = findViewById(R.id.input_confirm_password);
        mProgressBar.setVisibility(View.GONE);
        loadingPleaseWait.setVisibility(View.GONE);

        init();
    }

    private void init() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();
                confirmPassword = mConfirmPassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String namePattern = "^[a-zA-Z ]+$";


                //else if
                if (isStringNull(email) && isStringNull(username) && isStringNull(password) && isStringNull(confirmPassword)){
                    Toast.makeText(mContext,"You should fill up all the field.",Toast.LENGTH_SHORT).show();
                } else if (email.equals("")){
                    Toast.makeText(mContext, "Email is required", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern) && email.length()>0) {
                    Toast.makeText(mContext, "Invalid email address", Toast.LENGTH_SHORT).show();
                } else if (username.equals("")){
                    Toast.makeText(mContext, "Username is required", Toast.LENGTH_SHORT).show();
                } else if (!username.matches(namePattern) && username.length()>0) {
                    Toast.makeText(mContext, "Invalid Name", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")){
                    Toast.makeText(mContext, "Password is required", Toast.LENGTH_SHORT).show();
                } else if (confirmPassword.equals("")){
                    Toast.makeText(mContext, "Password Confirmation is required", Toast.LENGTH_SHORT).show();
                }  else if (!password.equals(confirmPassword)){
                    Toast.makeText(mContext, "Password Should be matched", Toast.LENGTH_SHORT).show();
                } else if(password.equals(confirmPassword)){
                    mProgressBar.setVisibility(View.VISIBLE);
                    loadingPleaseWait.setVisibility(View.VISIBLE);
                    userRegistration(email,password,username);

                }
            }
        });
    }

    private boolean checkInputs(String email, String username, String password, String confirmPassword) {
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if (email.equals("") || username.equals("") || password.equals("") || confirmPassword.equals("")) {
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean isStringNull (String string){
        if (string.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private void userRegistration(String email,String password,String username){
        final  PersonInfo personInfo=new PersonInfo();
        personInfo.setEmail(email);
        personInfo.setName(username);
        personInfo.setPassword(password);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    personInfo.setUser_id(user.getUid());
                    mDatabase.child("user").child(user.getUid()).setValue(personInfo);
                    mProgressBar.setVisibility(View.GONE);
                    loadingPleaseWait.setVisibility(View.GONE);
                    mAuth.signOut();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }else {
                    mProgressBar.setVisibility(View.GONE);
                    loadingPleaseWait.setVisibility(View.GONE);
                    messageALert("Error occoured : "+task.getException());
                    mAuth.signOut();
                }
            }
        });
    }

    private void messageALert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(message);
        builder.setTitle("Message: ");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onStart () {
        super.onStart();
    }

    @Override
    public void onStop () {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
    }
}
