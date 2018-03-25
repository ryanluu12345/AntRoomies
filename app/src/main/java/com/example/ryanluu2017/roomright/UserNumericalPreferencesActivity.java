package com.example.ryanluu2017.roomright;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class UserNumericalPreferencesActivity extends AppCompatActivity {

    private AppCompatSeekBar mCleanSeekBar;
    private AppCompatSeekBar mCleanlinessSeekBar;
    private AppCompatSeekBar mDrinkSeekBar;
    private Switch mFriendsSwitch;
    private Switch mPartySwitch;
    private Spinner mHousingSpinner;
    private EditText mSleepEt;
    private TextView mCleanTv;
    private TextView mCleanlinessTv;
    private TextView mDrinkTv;
    private Button mAmButton;
    private Button mPmButton;

    //UI Related Text
    private String ampm="";

    //New User Object
    public User user;

    //User related fields
    public String party;
    public String friends;
    public String clean;
    public String drink;
    public String cleanliness;
    public String sleep;
    public String housing;

    //Firebase Auth
    FirebaseAuth mAuth;

    //Firebase Utilities
    FirebaseUtils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_numerical_preferences);

        //Gets the authentication of user
        mAuth=FirebaseAuth.getInstance();

        //Instantiates the Firebase Utilities
        utils=new FirebaseUtils();

        initiliazeUILinks();
        seekBarListener();
        switchListener();
    }

    //Links the seek bars to listeners that change their text based on value
    private void seekBarListener(){

        mCleanSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                mCleanTv.setText("Times a week: "+String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mDrinkSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                mDrinkTv.setText("Times a week: "+String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mCleanlinessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                mCleanlinessTv.setText("Value: "+String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //Links all switches to listeners
    private void switchListener(){

        mPartySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //Changes text based on checked status
                if (isChecked){
                    mPartySwitch.setText("Yes");
                } else{
                    mPartySwitch.setText("No");
                }

            }
        });

        mFriendsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //Changes text based on checked status
                if (isChecked){
                    mFriendsSwitch.setText("Yes");
                } else{
                    mFriendsSwitch.setText("No");
                }

            }
        });

    }

    //Links frontend widgets to backend
    private void initiliazeUILinks(){

        mCleanSeekBar=(AppCompatSeekBar) findViewById(R.id.clean_seek_bar);
        mCleanlinessSeekBar=(AppCompatSeekBar) findViewById(R.id.cleanliness_seek_bar);
        mDrinkSeekBar=(AppCompatSeekBar) findViewById(R.id.drink_seek_bar);
        mFriendsSwitch=(Switch) findViewById(R.id.friends_switch);
        mPartySwitch=(Switch) findViewById(R.id.party_switch);
        mHousingSpinner=(Spinner) findViewById(R.id.housing_spinner);
        mSleepEt=(EditText) findViewById(R.id.sleep_edit_text);
        mCleanTv=(TextView) findViewById(R.id.clean_tv);
        mCleanlinessTv=(TextView) findViewById(R.id.cleanliness_tv);
        mDrinkTv=(TextView) findViewById(R.id.drink_tv);
        mAmButton=(Button) findViewById(R.id.am_button);
        mPmButton=(Button) findViewById(R.id.pm_button);

    }

    // TODO FIX AM PM BUTTONS TO CHANGE TO CORRECT COLOR

    //Handles AM button clicks
    public void onAmButtonClick(View v){
        mPmButton.setBackgroundColor(Color.WHITE);
        mAmButton.setBackgroundColor(Color.GRAY);
        this.ampm="AM";
    }

    //Handles PM button clicks
    public void onPmButtonClick(View v){
        mAmButton.setBackgroundColor(Color.WHITE);
        mPmButton.setBackgroundColor(Color.GRAY);
        this.ampm="PM";
    }

    //Handles saving data from user numerical prefs and intents to browse page
    public void onSaveNextClick(View v){

        //Extracts data from the UI and then updates user profile with the data
        extractDataFromUI();
        utils.updateUserPrefs(mAuth.getCurrentUser().getUid(),this.clean,this.cleanliness,this.drink,this.housing,this.party,this.sleep,this.friends);
        Toast.makeText(getApplicationContext(),"You have successfully saved your preferences",Toast.LENGTH_SHORT).show();

        //Makes intent to the user profile
        Intent profileIntent=new Intent(this,UserProfileActivity.class);
        startActivity(profileIntent);

    }

    //Returns to previous pref page
    public void onBackPrefClick(View v){

        Intent backIntent=new Intent(this, UserPreferencesActivity.class);
        startActivity(backIntent);

    }

    //Extracts data from the different UI elements
    private void extractDataFromUI(){

        this.friends=mFriendsSwitch.getText().toString();
        this.party=mPartySwitch.getText().toString();
        this.cleanliness=String.valueOf(mCleanlinessSeekBar.getProgress());
        this.clean=String.valueOf(mCleanSeekBar.getProgress());
        this.drink=String.valueOf(mDrinkSeekBar.getProgress());
        this.sleep=mSleepEt.getText().toString()+this.ampm;
        this.housing=mHousingSpinner.getSelectedItem().toString();

    }



}
