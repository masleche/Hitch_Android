package com.example.Hitch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;




public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences userInfo = getSharedPreferences(getString(R.string.USER_PRFERENCES),0);
        String username = userInfo.getString("username", "");

        if(!username.equals("")) {
            goToHomescreen();
            Log.d("HitchApp", "Found user info, going to homescreen");
        }

        setContentView(R.layout.main);
    }


    public void goToHomescreen() {
        Intent homescreen_intent;

        homescreen_intent = new Intent(this, HomescreenActivity.class);

        homescreen_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homescreen_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(homescreen_intent);
    }
    /**
     * Starts the login activity
     *
     * @param view
     */
    public void startLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void registerAccount(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
