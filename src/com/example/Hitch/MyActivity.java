package com.example.Hitch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


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

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface tf_condensed = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Condensed.ttf");

        TextView welcomeMessage = (TextView) findViewById(R.id.welcome_title);
        TextView appName = (TextView) findViewById(R.id.app_name);
        Button loginBtn = (Button) findViewById(R.id.start_login_button);
        Button registerBtn = (Button) findViewById(R.id.start_register_button);

        welcomeMessage.setTypeface(tf);
        appName.setTypeface(tf_condensed);
        loginBtn.setTypeface(tf_condensed);
        registerBtn.setTypeface(tf_condensed);
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
