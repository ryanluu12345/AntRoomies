package com.example.ryanluu2017.roomright;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;



//TODO Link edit, home, and browse buttons to handler methods in the backend
//TODO Clean up UI for "My Preferences" so that there is better spacing and divider lines
//TODO Add box shadows and slight rounded edges to the cards

public class UserProfileActivity extends AppCompatActivity {

    //New User refs
    private User user;
    private String uIdFromIntent;

    //Database Ref
    private DatabaseReference mUsersRef;

    //Firebase Auth Ref
    private FirebaseAuth mAuth;

    //UI Widgets
    private TextView mNameTv;
    private TextView mAboutMeTv;
    private TextView mRoommatePrefsTv;
    private TextView mSleepTv;
    private TextView mHousingTv;
    private TextView mCleanTv;
    private TextView mPartyTv;
    private TextView mDrinkTv;
    private TextView mFriendsTv;
    private ImageView mPictureIv;

    //Bool to determine profile load type
    private Boolean userProfBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Receives intent and intent parameters
        Intent getUserProfIntents=getIntent();

        //Tries to retrieve parameter from the intent
        try{
            //Successfully got the uid for another profile
            uIdFromIntent=getUserProfIntents.getStringExtra("uid");
            Log.i("jaboi",uIdFromIntent.toString());
            userProfBool=false;
        } catch(Exception e){
            //Own profile was selected
            userProfBool=true;
        }

        //Links UI elements to backend
        initializeUI();

        //Gets the current authenticated user
        mAuth=FirebaseAuth.getInstance();

        //Gets the database reference to the user
        mUsersRef= FirebaseDatabase.getInstance().getReference("users");

        //Checks to see which data to load
        if (userProfBool)
        {
            loadOwnUserData();
        } else {

            loadOtherUserData();
        }
    }

    //Links front end widgets to backend
    private void initializeUI(){

        mNameTv=(TextView) findViewById(R.id.name);
        mPictureIv=(ImageView) findViewById(R.id.picture);
        mAboutMeTv=(TextView) findViewById(R.id.about_me);
        mRoommatePrefsTv= (TextView) findViewById(R.id.roommate_prefs);
        mSleepTv=(TextView) findViewById(R.id.profile_sleep_tv);
        mDrinkTv=(TextView) findViewById(R.id.profile_drink_tv);
        mHousingTv=(TextView) findViewById(R.id.profile_housing_preference_tv);
        mCleanTv=(TextView) findViewById(R.id.profile_clean_tv);
        mPartyTv=(TextView) findViewById(R.id.profile_party_tv);
        mFriendsTv=(TextView) findViewById(R.id.profile_friends_tv);
    }

    //Loads own user data from the Firebase
    private void loadOwnUserData(){

        mUsersRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

              user=dataSnapshot.getValue(User.class);
              loadUserInfo(user);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Loads other user data from the Firebase
    private void loadOtherUserData(){

        mUsersRef.child(uIdFromIntent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user=dataSnapshot.getValue(User.class);
                loadUserInfo(user);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Sets the user info from Firebase to UI
    private void loadUserInfo(User user){

        //Sets the profile picture
        new DownLoadImageTask(mPictureIv).execute(user.picture);

        //Sets all the text in the UI
        mNameTv.setText(user.name);
        mAboutMeTv.setText(user.about);
        mRoommatePrefsTv.setText(user.roommatePrefs);
        mSleepTv.setText(user.sleep);
        mPartyTv.setText(user.party);
        mHousingTv.setText(user.housing);
        mCleanTv.setText(user.clean);
        mDrinkTv.setText(user.drink);
        mFriendsTv.setText(user.friends);

    }

    //Downloads the image from the link and then sets the profile picture image view
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    };

    public void onProfileBrowseClick(View v){

        //TODO change back to BrowserUsersActivity later
        Intent browseIntent=new Intent(this,SwipeBrowseUsersActivity.class);
        startActivity(browseIntent);

    }


}


