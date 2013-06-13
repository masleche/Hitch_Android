package com.example.Hitch.AsyncTasks;

/**
 * Created with IntelliJ IDEA.
 * User: Reece Engle
 * Date: 6/12/13
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */

/*
import Domain.Location;
import android.os.AsyncTask;
import android.util.Log;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetAllLocationsTask extends AsyncTask<Void, Void, Void> {

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
/*
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

    protected ArrayList<Location> locationsLoaded(ArrayList<Location> locations) {
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
*/