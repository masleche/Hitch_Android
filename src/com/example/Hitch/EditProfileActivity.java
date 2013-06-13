package com.example.Hitch;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: Reece Engle
 * Date: 6/12/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditProfileActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_activity);

        if(findViewById(R.id.edit_user_form_fragment) != null) {
            UserFormFragment userFormFragment = new UserFormFragment();

        }

    }
}