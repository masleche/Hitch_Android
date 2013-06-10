package com.example.Hitch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.parse.Parse;

/**
 * Created with IntelliJ IDEA.
 * User: Reece Engle
 * Date: 6/9/13
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */

public class SearchOptionsFragment extends Fragment {

    private SharedPreferences _UserInfo;
    private View mFragmentView;

    private Button mSearchLocation;
    private Button mSearchUser;
    private Button mSearchNearMe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.user_info_fragment, container, false);




        return mFragmentView;
    }


}
