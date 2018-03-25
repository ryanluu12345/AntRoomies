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

public class SignInActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_sign_in);

        //Receives intent from the previous page
        Intent receiveIntent=getIntent();

        //Initializes UI
        initializeUI();

        //Creates an auth instance
        mAuth=FirebaseAuth.getInstance();

        //Signs out just in case the user is signed in
        mAuth.signOut(); //Might be potential sign in bug

        mAuthListener=new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user=firebaseAuth.getCurrentUser();

                if(user !=null){
                    //The user is signed in
                    Log.i("User","Signed in user id: "+user.getUid());
                    Log.i("Creds",user.getEmail());

                    if(!user.isEmailVerified()){
                        Toast.makeText(SignInActivity.this,"Sorry, not verified. Please verify your email here",Toast.LENGTH_LONG).show();
                        Intent verifyIntent=new Intent(SignInActivity.this,VerifyEmailActivity.class);
                        startActivity(verifyIntent);
                    }

                } else{
                    //User is signed out
                    Log.i("User","User is now signed out");
                }
            }
        };
    }

    //Adds listener when the activity is started
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //Upon the activity stopping, the listener will be removed
    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener !=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Checks in the user's sign in to ensure they have been authenticated
    public void signIn(String email,String password){

        //Switches the progress bar on
        toggleProgressBar();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //Checks for the success of the sign in task
                if (!task.isSuccessful()){
                    toggleProgressBar();
                    Toast.makeText(SignInActivity.this,R.string.wrong_user_warning,Toast.LENGTH_SHORT).show();
                } else{

                    toggleProgressBar();
                    Toast.makeText(SignInActivity.this, "You have been signed in", Toast.LENGTH_LONG).show();

                    //Makes intent to the user's home page if email is verified
                    if(mAuth.getCurrentUser().isEmailVerified()) {
                        Intent makeIntent = new Intent(SignInActivity.this, UserPreferencesActivity.class);
                        startActivity(makeIntent);
                    }
                }
            }
        });
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

    //Links frontend widgets to backend
    private void initializeUI(){

        //Accesses the edit texts
        mEmailEt=(EditText) findViewById(R.id.email);
        mPasswordEt=(EditText) findViewById(R.id.password);
        mProgressBar=(ProgressBar) findViewById(R.id.sign_in_pb);
    }

    //Handles sign in intents
    public void onGoogleSignInClick(View v){

        //Extracts the passwords and emails from Edit Texts
        email=mEmailEt.getText().toString();
        password=mPasswordEt.getText().toString();

        //Catches the null argument exception
        if(email==null || password==null || email.equals("") || password.equals("")){
            //Alerts user to enter log in information
            Toast.makeText(getApplicationContext(),R.string.empty_login_warning,Toast.LENGTH_SHORT).show();
            return;
        }

        signIn(email,password);

    }

    //Handles sign up intents
    public void onSiPageSignUpClick(View v){

        Intent signUpIntent=new Intent(this,SignUpActivity.class);
        startActivity(signUpIntent);

    }

}
