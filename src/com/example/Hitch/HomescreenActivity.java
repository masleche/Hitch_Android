package com.example.Hitch;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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
public class HomescreenActivity extends Activity {



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_homescreen);
        //get my fragments
        SearchOptionsFragment searchOptionsFragment = new SearchOptionsFragment();
        UserFragment userFragment = new UserFragment();
        //get fragmentmngr and begin a transaction
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, userFragment);
        fragmentTransaction.addToBackStack("User");
        fragmentTransaction.add(searchOptionsFragment, "UserSearchOptions");
        fragmentTransaction.commit();

    }

    @Override
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