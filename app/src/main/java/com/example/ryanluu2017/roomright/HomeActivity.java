package com.example.ryanluu2017.roomright;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*

@TODO Touch up the UI and make the home page easy to navigate
@TODO Reconfigure storage of images and complex data: look into Google Cloud Storage or Amazon S3

 */

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    EditText randomText;
    TextView randomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent getIntention=getIntent();

    }

    //Gets the database reference when the activity starts
    @Override
    public void onStart(){
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
    }

    //On write click, the app writes to the database
    public void onWriteDbClick(View v){

        randomText=(EditText) findViewById(R.id.randomText);

        //Gets the current user
        String randTxt=mAuth.getCurrentUser().getEmail();
        myRef.setValue(randTxt);

    }

    //On read click, reads from the database
    public void onReadDbClick(View v){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Called when the value initializes and whenever data is updated here
                String data=dataSnapshot.getValue().toString();

                randomTitle=(TextView)findViewById(R.id.randomTitle);
                randomTitle.setText(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Failed to read value
                Log.w("TAG","failed to read value", databaseError.toException());
            }
        });
    }

    public void onSignOutClick(View v){
        mAuth.signOut();
        Toast.makeText(HomeActivity.this,"You have been signed out",Toast.LENGTH_LONG).show();

        //Makes intent back to original page
        Intent mainIntent=new Intent(HomeActivity.this,MainActivity.class);
        startActivity(mainIntent);
    }

}
