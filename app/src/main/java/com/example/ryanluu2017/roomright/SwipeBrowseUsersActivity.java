package com.example.ryanluu2017.roomright;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

//TODO refactor code for the swipe portion (CLEAN UP QUALITY)
//TODO add link to the housing preferences in the UI and the initialize UI portion
//TODO look into storage options for images that are free and usable in large scales and call freqs

public class SwipeBrowseUsersActivity extends AppCompatActivity {

    //New User refs
    private User user;
    private ArrayList<User> users;

    //Database Ref
    private DatabaseReference mUsersRef;

    //Firebase Auth Ref
    private FirebaseAuth mAuth;

    //UI Widgets
    private LinearLayout mLlSwipeBrowse;
    private TextView mNameTv;
    private TextView mAboutMeTv;
    private TextView mRoommatePrefsTv;
    private ImageView mProfPicIv;
    private TextView mHousingTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_browse_users);

        //Initializes the UI and Users
        initializeUI();
        users=new ArrayList<User>();


        //Gets the current authenticated user
        mAuth=FirebaseAuth.getInstance();

        //Gets the database reference to the user
        mUsersRef= FirebaseDatabase.getInstance().getReference("users");

        //Loads data from Firebase
        loadDataFromFb();

    }

    //Links all frontend widgets to backend
    private void initializeUI(){

        mLlSwipeBrowse=(LinearLayout) findViewById(R.id.swipe_browse_ll);
        mNameTv=(TextView) findViewById(R.id.swipe_card_name_tv);
        mAboutMeTv=(TextView) findViewById(R.id.swipe_card_about_me_tv);
        mRoommatePrefsTv=(TextView) findViewById(R.id.swipe_card_roommate_prefs_tv);
        mProfPicIv=(ImageView) findViewById(R.id.swipe_card_profile_picture_iv);

    }

    //Loads data from the firebase
    private void loadDataFromFb(){

        mUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Loops through all users and then adds them to the users ArrayList
                for (DataSnapshot child:dataSnapshot.getChildren()){
                    users.add(child.getValue(User.class));
                    Log.i("entered",users.toString());
                }

                chooseRandomProfile();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Chooses a profile at random from the list
    private void chooseRandomProfile(){
        Random rand = new Random();
        int randInt = rand.nextInt(users.size()-1);
        Log.i("size", String.valueOf(randInt));
        user=users.get(randInt);
        loadUserInfo(user);

    }

    //Loads user info onto UI
    private void loadUserInfo(User user){

        //Sets the profile picture
        //TODO uncomment after finished with image storage solution new DownLoadImageTask(mProfPicIv).execute(user.picture);

        //Sets the text views
        mNameTv.setText(user.name);
        mAboutMeTv.setText(user.about);
        mRoommatePrefsTv.setText(user.roommatePrefs);

        //Sets the tag of the image view to the uid
        mProfPicIv.setTag(user.uid);

        //Initialize swipe functionality
        initializeSwipe();

        //Sets the onSwipeBrowse
        setOnSwipeBrowseImgClick();

    }


    //Downloads image TODO replace with image util version when that is hashed out
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

    //TODO take out the toasts and add functionality to add likes and dislikes to the DB
    private void initializeSwipe(){

        mLlSwipeBrowse.setOnTouchListener(new OnSwipeTouchListener(SwipeBrowseUsersActivity.this){

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                chooseRandomProfile();
                Toast.makeText(getApplicationContext(),"-1",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                chooseRandomProfile();
                Toast.makeText(getApplicationContext(),"+1",Toast.LENGTH_SHORT).show();
            }
        });


    }

    //Handles clicking of image and takes to profile
    private void setOnSwipeBrowseImgClick(){

        mProfPicIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Retrieves tag from the image view
                String uid=view.getTag().toString();

                //Starts intent to UserProfilePic
                Intent userProfIntent=new Intent(view.getContext(),UserProfileActivity.class);
                userProfIntent.putExtra("uid",uid);
                startActivity(userProfIntent);

            }
        });


    }


}
