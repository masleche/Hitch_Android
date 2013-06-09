package com.example.Hitch;

import Domain.User;
import android.app.Activity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Reece Engle
 * Date: 5/29/13
 * Time: 9:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationStateManager {

    private User CurrentUser;
    private Date LastAction;

    private Activity CurrentActivity;

    public ApplicationStateManager(User currentUser) {
        CurrentUser = currentUser;
        LastAction =  new Date();
    }

    public void setLastAction(Date lastAction) {
        LastAction = lastAction;
    }

    public void setCurrentActivity(Activity currentActivity) {
        CurrentActivity = currentActivity;
    }

    public Activity getCurrentActivity() {

        return CurrentActivity;
    }

    public Date getLastAction() {

        return LastAction;
    }

    public User getCurrentUser() {

        return CurrentUser;
    }
}
