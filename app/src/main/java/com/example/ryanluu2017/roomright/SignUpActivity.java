package com.example.ryanluu2017.roomright;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

//TODO clean up sign in and sign up UI
//TODO add a method that checks if the user is already registered. Link it the R.string.resgistered_user_warning
public class SignUpActivity extends AppCompatActivity implements Serializable {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmailEt;
    private EditText mPasswordEt;
    private ProgressBar mProgressBar;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent=getIntent();

        //Initializes UI
        initializeUI();

        //Sets the FirebaseAuth object
        mAuth=FirebaseAuth.getInstance();

    }

    private void initializeUI(){

        //Accesses the edit texts
        mEmailEt=(EditText) findViewById(R.id.signUpEmail);
        mPasswordEt=(EditText) findViewById(R.id.signUpPassword);
        mProgressBar=(ProgressBar) findViewById(R.id.sign_up_pb);

    }

    //Creates an account for the new user using their email
    private void createAccount(String email,String password){

        toggleProgressBar();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                toggleProgressBar();
                if (!task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,R.string.wrong_user_warning,Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(SignUpActivity.this,"You have successfully signed up",Toast.LENGTH_SHORT).show();
                    verifyEmail();
                }
            }
        });

    }

    private void verifyEmail(){
        final FirebaseUser user=mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        //Checks to see if task is successful before sending verification
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("", "sendEmailVerification", task.getException());
                            Toast.makeText(SignUpActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    //Handles the button action from UI
    public void onCreateAccountClick(View v){

        //Extracts the passwords and emails from Edit Texts
        email=mEmailEt.getText().toString();
        password=mPasswordEt.getText().toString();

        //Catches the null argument exception
        if(email==null || password==null || email.equals("") || password.equals("")){
            //Alerts user to enter log in information
            Toast.makeText(getApplicationContext(),R.string.empty_login_warning,Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount(email,password);
    }

    //Switches progress bar visible or invisble
    private void toggleProgressBar(){

        //Sets appropriate visibility for UI elements
        if (mProgressBar.isShown()){
            mProgressBar.setVisibility(View.INVISIBLE);
            mEmailEt.setVisibility(View.VISIBLE);
            mPasswordEt.setVisibility(View.VISIBLE);

        } else{
            mProgressBar.setVisibility(View.VISIBLE);
            mEmailEt.setVisibility(View.INVISIBLE);
            mPasswordEt.setVisibility(View.INVISIBLE);
        }

    }

    //Transitions to the sign in page
    public void onSignUpInClick(View v){

        //Makes an intent to the sign in page
        Intent intent=new Intent(this,SignInActivity.class);
        startActivity(intent);

    }



}
