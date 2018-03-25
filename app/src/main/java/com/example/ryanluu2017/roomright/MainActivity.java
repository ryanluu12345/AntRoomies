package com.example.ryanluu2017.roomright;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/*TODO:
- Take out extraneous code

*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void makeIntent(Class next,Object data){

        Intent intent=new Intent(this,next);
        //Change up the below line to match different data
        intent.putExtra("data",data.toString());
        startActivity(intent);

    }

    public void onSignInClick(View v){

        makeIntent(SignInActivity.class,"");

    }

    public void onSignUpClick(View v){

        makeIntent(SignUpActivity.class,"");

    }


}
