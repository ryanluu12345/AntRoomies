package com.example.ryanluu2017.roomright;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ryanluu2017 on 3/20/2018.
 */

public class FirebaseUtils {

    private FirebaseDatabase mDatabase;

    //Constructor that instantiates reference to the database
    public FirebaseUtils(){
        this.mDatabase= FirebaseDatabase.getInstance();
    }

    //Writes user information
    public void writeUser(User user){

        //Gets reference to the user ref
        DatabaseReference mUserRef=this.mDatabase.getReference("users");

        //Writes the user to the "users" branch
        mUserRef.child(user.uid).setValue(user);

    }

    //Updates user preferences from the second user pref page
    public void updateUserPrefs(String uid,String clean, String cleanliness, String drink, String housing, String party, String sleep,String friends){

        //Gets reference to the user ref
        DatabaseReference mUserRef=this.mDatabase.getReference("users");

        //Updates values of the user in the DB ref
        mUserRef.child(uid).child("clean").setValue(clean);
        mUserRef.child(uid).child("cleanliness").setValue(cleanliness);
        mUserRef.child(uid).child("drink").setValue(drink);
        mUserRef.child(uid).child("housing").setValue(housing);
        mUserRef.child(uid).child("party").setValue(party);
        mUserRef.child(uid).child("sleep").setValue(sleep);
        mUserRef.child(uid).child("friends").setValue(friends);

    }

}
