package com.example.Hitch;

import Domain.Location;
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
import android.view.View;
import android.widget.*;
import com.parse.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Reece Engle
 * Date: 5/18/13
 * Time: 2:27 AM
 */
public class RegisterActivity extends Activity {
   
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mEmailView;
    private EditText mPhoneView;
    private EditText mFullnameView;

    private Button mRegisterAccountButton;
    private Button mRegisterCancelButton;

    private View mRegisterStatusView;
    private View mRegisterFormView;

    private Spinner mLocationSpinnerView;

    private String mEmail;
    private String mPassword;
    private String mUsername;
    private String mPhone;
    private String mFullname;


    public Intent homescreen_intent;

    private ArrayList<Location> _AllLocations;
    private ArrayAdapter<Location> _LocationAdapter;
    private Location _SelectedLocation;

    private TextView mRegisterStatusMessageView;

    private static final String APP_SECRET = "KKyJUd6ulUMafZTe0rXmrguThQVtbRGkbdwsMM8Z";
    private static final String CLIENT_KEY = "ggOz6c5beJFCImMHlBqWqTyHuD0wuOk6sQyEtf8z";

    /**
     * Keep track of the registration task to ensure we can cancel it if requested.
     */
    private UserRegistrationTask mRegisterTask = null;
    private GetAllLocationsTask _GetAllLocationsTask = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_account);

        Parse.initialize(this, APP_SECRET, CLIENT_KEY);

        _GetAllLocationsTask =  new GetAllLocationsTask();
        _GetAllLocationsTask.execute((Void) null);


        _AllLocations = new ArrayList<Location>();
        mLocationSpinnerView = (Spinner) findViewById(R.id.location_spinner);
        _LocationAdapter = new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1, _AllLocations);
        _LocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocationSpinnerView.setAdapter(_LocationAdapter);

        _LocationAdapter.add(new Location("-1", "Please select a location.", false));



        mUsernameView = (EditText) findViewById(R.id.register_username);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mEmailView = (EditText) findViewById(R.id.register_email);




        mRegisterAccountButton = (Button) findViewById(R.id.register_button);

        mRegisterStatusView = findViewById(R.id.register_status);

        mRegisterFormView = findViewById(R.id.register_form_view);

        mRegisterAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        mFullnameView = (EditText) findViewById(R.id.register_fullname);
        mPhoneView = (EditText) findViewById(R.id.register_phone);

        mRegisterStatusMessageView = (TextView) findViewById(R.id.register_status_message);

        mRegisterCancelButton = (Button) findViewById(R.id.cancel_button);

        homescreen_intent = new Intent(this, HomescreenActivity.class);


    }

    private void attemptRegister() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the register attempt.
        _SelectedLocation = (Location)mLocationSpinnerView.getSelectedItem();
        mUsername = mUsernameView.getText().toString().trim();
        mEmail = mEmailView.getText().toString().trim();
        mPassword = mPasswordView.getText().toString();
        mPhone = mPhoneView.getText().toString().trim();
        mFullname = mFullnameView.getText().toString().trim();

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
        else if (!mEmail.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        //check to make sure the user selected a localtion
        if(_SelectedLocation.getID().equals("-1")) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_select_location), Toast.LENGTH_LONG).show();
            focusView = mLocationSpinnerView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mRegisterStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            mRegisterTask = new UserRegistrationTask();
            mRegisterTask.execute((Void) null);
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

            mRegisterStatusView.setVisibility(View.VISIBLE);
            mRegisterStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mRegisterStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mRegisterFormView.setVisibility(View.VISIBLE);
            mRegisterFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mRegisterStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private class UserRegistrationTask extends AsyncTask<Void, Void, Boolean> {
        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Boolean doInBackground(Void... params) {

            ParseUser newUser = new ParseUser();

            newUser.setUsername(mUsername);
            newUser.setPassword(mPassword);
            newUser.setEmail(mEmail);
            newUser.put("location", ParseObject.createWithoutData("Location", _SelectedLocation.getID()));
            newUser.put("fullName", mFullname);
            newUser.put("phoneNumber", mPhone);

            SharedPreferences user_info = getSharedPreferences(getString(R.string.USER_PRFERENCES), 0);
            user_info.edit()
                    .putString("username", newUser.getUsername())
                    .putString("locationId", _SelectedLocation.getID())
                    .putString("locationName", _SelectedLocation.getName())
                    .putString("phoneNumber", mPhone)
                    .putString("fullName", mFullname)
                    .putString("email", newUser.getEmail())
                    .commit();

            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    Log.d("Hitch:RegisterAccount", "SignupDone");
                    if(e==null) {

                        homescreen_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        homescreen_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homescreen_intent);
                    }
                    else {
                        Log.d("Hitch:RegisterAccount", "Register Account Error: " + e.getCode() + " - " + e.getMessage());
                        Toast.makeText(getApplicationContext(),
                                e.getMessage() + ", please edit your registration form and resubmit your form.",
                                Toast.LENGTH_LONG).show();
                        mRegisterTask = null;
                        showProgress(false);
                    }
                }
            });
            return true;

        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }

    private class GetAllLocationsTask extends AsyncTask<Void, Void, Void> {

        protected ArrayList<Location> _AllLocations = null;

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Void... params) {

            _AllLocations = new ArrayList<Location>();
            ParseQuery parseQuery = new ParseQuery("Location");

            parseQuery.findInBackground(new FindCallback() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if(e==null) {
                        for(ParseObject object : parseObjects) {
                            Location tempLocation = new Location(object.getObjectId(), object.getString("name"), object.getBoolean("isOffice"));
                            _AllLocations.add(tempLocation);

                        }
                    }
                    locationsLoaded(_AllLocations);
                }
            });

            return null;
        }

        protected void locationsLoaded(ArrayList<Location> locations) {
             if(locations.size() > 0) {
                Collections.sort(locations);
                _AllLocations = locations;
                Log.d("Hitch:RegisterAccount", "Success! Found: " + _AllLocations.size() + " locations");

                _LocationAdapter.addAll(_AllLocations);
                _LocationAdapter.notifyDataSetChanged();
            }
            else {
                Log.w("Hitch:RegisterAccount", "No Locations Returned");
            }
        }
    }
}