package com.example.ryanluu2017.roomright;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmailActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        Intent mGetIntent=getIntent();

        mAuth=FirebaseAuth.getInstance();
    }

    //Sends verification email to people who have not been verified
    public void onVerifyEmailClick(View v){
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button
                        findViewById(R.id.verifyEmail).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(VerifyEmailActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("", "sendEmailVerification", task.getException());
                            Toast.makeText(VerifyEmailActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    //Returns to the sign in page when button is clicked
    public void onReturnSignIn(View v){
        Intent signInIntent=new Intent(this, SignInActivity.class);
        startActivity(signInIntent);
    }


}
