package com.example.Hitch;

import Domain.Location;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.parse.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Reece Engle
 * Date: 6/12/13
 * Time: 9:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserFormFragment extends Fragment {
    private TextView mUserFormTitle;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mEmailView;
    private EditText mPhoneView;
    private EditText mFullnameView;

    private Button mSubmitUserFormButton;
    private Button mCancelUserFormButton;

    private View mUserFormStatusView;
    private View mUserFormView;

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

    private TextView mUserFormStatusMessageView;

    private static final String APP_SECRET = "KKyJUd6ulUMafZTe0rXmrguThQVtbRGkbdwsMM8Z";
    private static final String CLIENT_KEY = "ggOz6c5beJFCImMHlBqWqTyHuD0wuOk6sQyEtf8z";

    /**
     * Keep track of the registration task to ensure we can cancel it if requested.
     */
    private SubmitUserFormTask mRegisterTask = null;
    private GetAllLocationsTask _GetAllLocationsTask = null;
    private View mFragmentView;
    private boolean isEditAction;
    private SharedPreferences _UserInfo;
    private GetLocationTask _LocationTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        isEditAction = getActivity().getIntent().getAction() == "EDIT_USER_INFO";
        //setContentView(R.layout.register_account);

        mFragmentView = inflater.inflate(R.layout.edit_user_activity, container, false);
        Parse.initialize(getActivity(), APP_SECRET, CLIENT_KEY);

        _GetAllLocationsTask =  new GetAllLocationsTask();
        _GetAllLocationsTask.execute((Void) null);


        _AllLocations = new ArrayList<Location>();
        mLocationSpinnerView = (Spinner) mFragmentView.findViewById(R.id.location_spinner);
        _LocationAdapter = new ArrayAdapter<Location>(getActivity(), android.R.layout.simple_list_item_1, _AllLocations);
        _LocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocationSpinnerView.setAdapter(_LocationAdapter);

        _LocationAdapter.add(new Location("-1", "Please select a location.", false));

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Condensed.ttf");

        mUserFormTitle = (TextView) mFragmentView.findViewById(R.id.register_account_title);
        mUserFormTitle.setTypeface(tf);

        mUsernameView = (EditText) mFragmentView.findViewById(R.id.register_username);
        mPasswordView = (EditText) mFragmentView.findViewById(R.id.register_password);
        mEmailView = (EditText) mFragmentView.findViewById(R.id.register_email);

        mFullnameView = (EditText) mFragmentView.findViewById(R.id.register_fullname);
        mPhoneView = (EditText) mFragmentView.findViewById(R.id.register_phone);
        mUserFormStatusMessageView = (TextView) mFragmentView.findViewById(R.id.register_status_message);


        if(isEditAction) {
            mPasswordView.setVisibility(View.INVISIBLE);
            setupUserFormView();
        }





        mSubmitUserFormButton = (Button) mFragmentView.findViewById(R.id.register_button);

        mUserFormStatusView = mFragmentView.findViewById(R.id.register_status);

        mUserFormView = mFragmentView.findViewById(R.id.register_form_view);

        mSubmitUserFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptEditInfo();
            }
        });


        mCancelUserFormButton = (Button) mFragmentView.findViewById(R.id.cancel_button);

        homescreen_intent = new Intent(getActivity(), HomescreenActivity.class);

        return mFragmentView;
    }

    private void setupUserFormView() {

        _UserInfo = this.getActivity().getSharedPreferences(getString(R.string.USER_PRFERENCES), 0);

        String locationName = _UserInfo.getString("locationName", "");
        if(locationName.isEmpty()) {
            _LocationTask = new GetLocationTask();
            _LocationTask.execute((Void) null);
        } else {

            //TODO:mLocationView.setText(_UserInfo.getString("locationName", ""));
        }


        //TODO:mUserImageView.setImageResource(R.drawable.images);

        mUsernameView.setText(_UserInfo.getString("username", ""));
        mFullnameView.setText(_UserInfo.getString("fullName", ""));
        mPhoneView.setText(_UserInfo.getString("phoneNumber", ""));
        mEmailView.setText(_UserInfo.getString("email", ""));



    }
    private void attemptEditInfo() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);

        // Store values at the time of the register attempt.
        _SelectedLocation = (Location)mLocationSpinnerView.getSelectedItem();
        mUsername = mUsernameView.getText().toString().trim();
        mEmail = mEmailView.getText().toString().trim();
        mPhone = mPhoneView.getText().toString().trim();
        mFullname = mFullnameView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;


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
            Toast.makeText(getActivity().getApplicationContext(),
                    getString(R.string.error_select_location),
                    Toast.LENGTH_LONG).show();
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
            mUserFormStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            mRegisterTask = new SubmitUserFormTask();
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

            mUserFormStatusView.setVisibility(View.VISIBLE);
            mUserFormStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mUserFormStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mUserFormView.setVisibility(View.VISIBLE);
            mUserFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mUserFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mUserFormStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mUserFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private class SubmitUserFormTask extends AsyncTask<Void, Void, Boolean> {
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

                if(isEditAction) {
                    ParseUser currentUser = ParseUser.getCurrentUser();

                    final ParseObject editUserObject = new ParseObject("User");
                    editUserObject.put("location", ParseObject.createWithoutData("Location", _SelectedLocation.getID()));
                    editUserObject.put("email", mEmail);
                    editUserObject.put("phoneNumber", mPhone);
                    editUserObject.put("fullName", mFullname);
                    editUserObject.saveInBackground();
                }
                else {
                    ParseUser newUser = new ParseUser();

                    newUser.setUsername(mUsername);
                    newUser.setPassword(mPassword);
                    newUser.setEmail(mEmail);
                    newUser.put("location", ParseObject.createWithoutData("Location", _SelectedLocation.getID()));
                    newUser.put("fullName", mFullname);
                    newUser.put("phoneNumber", mPhone);

                    SharedPreferences user_info = getActivity().getSharedPreferences(getString(R.string.USER_PRFERENCES), 0);
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


                                homescreen_intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                homescreen_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                homescreen_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                showProgress(false);
                                startActivity(homescreen_intent);
                            }
                            else {
                                Log.d("Hitch:RegisterAccount", "Register Account Error: " + e.getCode() + " - " + e.getMessage());
                                Toast.makeText(getActivity().getApplicationContext(),
                                        e.getMessage() + ", please edit your registration form and resubmit your form.",
                                        Toast.LENGTH_LONG).show();
                                mRegisterTask = null;
                                showProgress(false);
                            }
                        }
                    });
                }

                return true;

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

    protected class GetLocationTask extends AsyncTask<Void, Void, Void> {

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
        protected Void doInBackground(Void... params) {
            ParseQuery query = new ParseQuery("Location");
            String locationId =  _UserInfo.getString("locationId", "0");

            if(!locationId.equals("0")) {
                query.whereEqualTo("objectId", locationId);
                query.findInBackground(new FindCallback() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (e == null) {
                            setLocationInfo(parseObjects.get(0).getString("name"));

                        } else {
                            Toast.makeText(getActivity(), "Network Error: Cannot find location data.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            return null;
        }

        private void setLocationInfo(String name) {

            _UserInfo.edit().putString("locationName", name).commit();
            //TODO: mLocationView.setText(name);
        }
    }
}
