package com.example.Hitch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: Reece Engle
 * Date: 6/8/13
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class HomescreenActivity extends FragmentActivity {



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_homescreen);
        //get my fragments

        if (findViewById(R.id.fragment_container_1) != null) {
            SearchOptionsFragment searchOptionsFragment = new SearchOptionsFragment();
            UserFragment userFragment = new UserFragment();
            //get fragmentmngr and begin a transaction
            if(savedInstanceState!= null) {
                return;
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.fragment_container_1, userFragment, "User Info").commit();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container_2, searchOptionsFragment, "UserSearchOptions").commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_EditProfile:
                handled = true;
                break;
            case R.id.menu_Homescreen:
                handled = true;
                break;
            case R.id.menu_Logout:
                handled = true;
                getSharedPreferences(getString(R.string.USER_PRFERENCES), 0)
                        .edit()
                        .clear()
                        .commit();
                finish();
                break;
            case R.id.menuExit:
                handled = true;
                onExitClick(item);
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }

        return handled;
    }

    private void onExitClick(MenuItem item) {
        finish();
    }


}