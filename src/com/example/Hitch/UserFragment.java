package com.example.Hitch;

import android.app.*;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.parse.*;

import java.util.List;

/**
 * User: Reece Engle
 * Date: 6/8/13
 * Time: 4:57 PM
 */
public class UserFragment extends Fragment {

    private static final String APP_SECRET = "KKyJUd6ulUMafZTe0rXmrguThQVtbRGkbdwsMM8Z";
    private static final String CLIENT_KEY = "ggOz6c5beJFCImMHlBqWqTyHuD0wuOk6sQyEtf8z";

    private ImageView mUserImageView;



    private TextView mUsernameView;
    private TextView mNameView;
    private TextView mPhoneView;
    private TextView mLocationView;
    private TextView mEmailView;

    private View mFragmentView;

    private Button mSearchButton;

    private SharedPreferences _UserInfo;

    private GetLocationTask _LocationTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        Parse.initialize(getActivity().getApplicationContext(), APP_SECRET, CLIENT_KEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.user_info_fragment, container, false);

        mFragmentView = setupUserView(mFragmentView);



        return mFragmentView;
    }

    private View setupUserView(View view) {

        findAllUserViews(view);


        _UserInfo = this.getActivity().getSharedPreferences(getString(R.string.USER_PRFERENCES), 0);

        String locationName = _UserInfo.getString("locationName", "");
        if(locationName.isEmpty()) {
            _LocationTask = new GetLocationTask();
            _LocationTask.execute((Void) null);
        } else {
            mLocationView.setText(_UserInfo.getString("locationName", ""));
        }

        mUserImageView.setImageResource(R.drawable.images);

        mUsernameView.setText(_UserInfo.getString("username", ""));
        mNameView.setText(_UserInfo.getString("fullName", ""));
        mPhoneView.setText(_UserInfo.getString("phoneNumber", ""));
        mEmailView.setText(_UserInfo.getString("email", ""));



        return view;
    }

    private void findAllUserViews(View view) {


        mUserImageView = (ImageView) view.findViewById(R.id.userImageView);

        mUsernameView = (TextView) view.findViewById(R.id.display_username);
        mEmailView = (TextView) view.findViewById(R.id.display_email);
        mPhoneView = (TextView) view.findViewById(R.id.display_phone);
        mLocationView = (TextView) view.findViewById(R.id.display_location);
        mNameView = (TextView) view.findViewById(R.id.display_name);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.main_menu, menu);
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
            mLocationView.setText(name);
        }
    }

}
