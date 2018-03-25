package com.example.ryanluu2017.roomright;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


//TODO link utils code to handle image downloads throughout application

//TODO fix bugs related to download async task

//TODO implement UI for like button and backend link

public class BrowseUsersActivity extends AppCompatActivity {

    //Firebase variables
    FirebaseAuth mAuth;
    DatabaseReference mUsersRef;

    //List of Users
    ArrayList<User> users;

    //RecyclerView variables
    private RecyclerView mBrowseRv;
    private LinearLayoutManager mBrowseLlm;
    private simpleBrowseRvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_users);

        //Creates a new empty users list
        users= new ArrayList<User>();

        //Gets the current authenticated user
        mAuth= FirebaseAuth.getInstance();

        //Gets the database reference to the user
        mUsersRef= FirebaseDatabase.getInstance().getReference("users");

        initializeDataFromFb();
        Log.i("entered","I have entered");
        initializeRecyclerView();


    }

    //Loads data from the firebase
    private void initializeDataFromFb(){

        mUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Loops through all users and then adds them to the users ArrayList
                for (DataSnapshot child:dataSnapshot.getChildren()){

                    users.add(child.getValue(User.class));
                    Log.i("entered",users.toString());
                }

                Log.i("size",String.valueOf(users.size()));
                //Initializes the adapter with the new info
                initializeAdapter();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Initializes the recycler view
    private void initializeRecyclerView(){

        //Links the recycler view to frontend
        mBrowseRv=(RecyclerView) findViewById(R.id.simple_browse_rv);
        mBrowseRv.setHasFixedSize(true);

        //Creates the layout manager
        mBrowseLlm = new LinearLayoutManager(this);
        mBrowseRv.setLayoutManager(mBrowseLlm);
    }

    //Initializes adapter
    private void initializeAdapter(){

        Log.i("adapter",users.get(0).about);
        adapter=new simpleBrowseRvAdapter(users);
        mBrowseRv.setAdapter(adapter);
    }

}
