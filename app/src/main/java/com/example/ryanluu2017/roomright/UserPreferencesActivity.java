package com.example.ryanluu2017.roomright;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

//TODO fix consistency issues: snakecase for xml, camelcase for java
//TODO new user preference fields

public class UserPreferencesActivity extends AppCompatActivity {

    //UI Fields
    private EditText mFullNameEt;
    private EditText mAboutMeEt;
    private EditText mPersonalPrefEt;
    private ImageButton mUserProfPicIb;

    //User Data Fields
    public String uid = "";
    public String name = "";
    public String email = "";
    public String about = "";
    public String roommatePrefs = "";
    public String picture = "";

    //New user object
    User user;

    //FirebaseUtils
    public FirebaseUtils utils;
    public StorageReference mStorageRef;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);

        //Helper function for linking fields to vars
        initializeUI();

        //Gets the storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Loads the FirebaseUtils Class
        utils=new FirebaseUtils();

    }

    //Links the holder variables to UI elements
    private void initializeUI() {

        this.mFullNameEt = (EditText) findViewById(R.id.fullName);
        this.mAboutMeEt = (EditText) findViewById(R.id.aboutMeBlurb);
        this.mPersonalPrefEt = (EditText) findViewById(R.id.personalPrefBlurb);
        this.mUserProfPicIb=(ImageButton) findViewById(R.id.upload_image_btn);
    }

    //Allows upload of images and videos when clicked
    public void onCameraUploadClick(View v) {

        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent = chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, 1);

    }

    //Checks the result of the image upload
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        String path = "";
        if (requestCode == 1) {
            Uri uri = data.getData();
            Toast.makeText(UserPreferencesActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();

            //TODO Make function for using the action capture
            //TODO Create a preview for images in this section


            //TODO Create a profile page for each person with their profile video and information

            //Creates the string to write to
            String storagePath = "videos/user/profile/" + mAuth.getCurrentUser().getUid().toString() + ".mp4";

            StorageReference riversRef = mStorageRef.child(storagePath);

            riversRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl(); //Not an error. Just highlighted

                            //Sets the picture link field to the URI link
                            picture = downloadUrl.toString();

                            Toast.makeText(UserPreferencesActivity.this, downloadUrl.toString(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...

                            Toast.makeText(UserPreferencesActivity.this, exception.toString(), Toast.LENGTH_LONG).show();

                        }
                    });

        }
    }

    //Sets the preview image at the top of page
    private void setPreviewImage(){

    }

    //Starts the activity for the next pref page
    public void onNextPrefPageClick(View v) {

        extractData();

        //Creates a user
        user=new User(this.uid,this.name,this.email,this.about,this.roommatePrefs,this.picture);

        //Writes to the database before switching pages
        utils.writeUser(user);

        //Intent to the second preferences page

        Intent nextPrefPageIntent = new Intent(this, UserNumericalPreferencesActivity.class);
        startActivity(nextPrefPageIntent);

    }

    //Skips the user preferences and takes user directly to user profile
    public void onSkipPref(View v){

        Intent skipIntent = new Intent(this, UserProfileActivity.class);
        startActivity(skipIntent);

    }

    //Extracts data from the fields
    private void extractData() {

        this.uid = mAuth.getCurrentUser().getUid();
        this.email = mAuth.getCurrentUser().getEmail();
        this.name = mFullNameEt.getText().toString();
        this.about = mAboutMeEt.getText().toString();
        this.roommatePrefs = mPersonalPrefEt.getText().toString();
        this.picture = picture;

    }


}
