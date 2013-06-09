package com.example.Hitch;

import Domain.User;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.*;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

    /**
     * The default email to populate the email field with.
     */
    public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

    private static final String APP_SECRET = "KKyJUd6ulUMafZTe0rXmrguThQVtbRGkbdwsMM8Z";
    private static final String CLIENT_KEY = "ggOz6c5beJFCImMHlBqWqTyHuD0wuOk6sQyEtf8z";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // Values for email and password at the time of the login attempt.
    private String mEmail;
    private String mPassword;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;


    public Intent homescreen_intent;
    public Intent register_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homescreen_intent = new Intent(this, HomescreenActivity.class);
        register_intent = new Intent(this, RegisterActivity.class);

        Parse.initialize(this, APP_SECRET, CLIENT_KEY);

        setContentView(R.layout.login_activity);

        // Set up the login form.
        mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.setText(mEmail);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString().trim();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            mAuthTask = new UserLoginTask();
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            ParseUser.logInInBackground(mEmail, mPassword, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if(e == null && parseUser != null) {
                        
                        Log.d("Hitch:Login", "Success");
                        ParseObject locationObject = (ParseObject) parseUser.get("location");

                        User user = new User(parseUser.getObjectId(), parseUser.getUsername(),
                                parseUser.getEmail(), parseUser.getString("fullName"),
                                locationObject.getObjectId(), parseUser.getString("phoneNumber"));

                        Log.d("user", "Retrieved " + user.getUsername() );

                        SharedPreferences user_info = getSharedPreferences(getString(R.string.USER_PRFERENCES), 0);
                        user_info.edit()
                                .putString("username", user.getUsername())
                                .putString("userId", user.getID())
                                .putString("locationId", user.getLocationID())
                                .putString("phoneNumber", user.getPhoneNumber())
                                .putString("fullName", user.getFullName())
                                .putString("email", user.getEmail())
                                .commit();

                        homescreen_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        homescreen_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homescreen_intent);
                    } else if (parseUser == null) {
                        Log.d("Hitch:Login", "Bad Username/Password");
                        onCancelled();
                        Toast.makeText(getApplicationContext(), getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show();

                    } else {
                        Log.e("Hitch:Login", "Login Error: " + e.getCode() + " - " + e.getMessage() );
                        onCancelled();

                        Toast.makeText(getApplicationContext(), getString(R.string.login_error) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });


           return true;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    /*


    private void loginSuccessful(String username) {
        ParseQuery query = new ParseQuery("User");
        query.whereEqualTo("username", username);

        query.findInBackground(new FindCallback() {
            public void done(List<ParseObject> parseObjectList, ParseException e) {
                if (e == null && parseObjectList.size() > 0) {
                    if(parseObjectList.size() > 0) {
                        ParseObject userRow = parseObjectList.get(0);

                        User user = new User(userRow.getObjectId(), userRow.getString("username"),
                                userRow.getString("email"), userRow.getString("fullName"),
                                userRow.getString("location"), userRow.getString("phoneNumber"));


                        Log.d("user", "Retrieved " + user.getUsername() );
                    }
                }
                else {
                    Log.d("login", "Error: " + e.getMessage());
                }
            }
        });

    }
         */
}
