package Domain;

import android.os.AsyncTask;
import com.parse.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Reece Engle
 * Date: 5/29/13
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Location implements Comparable {

    private String ID;

    private String Name;
    private boolean Office;
    private Date UpdatedAt;
    private Date CreatedAt;
    //private GetAllLocationsTask _GetAllLocationsTask;


    public Location(String ID, String name, boolean office) {
        this.ID = ID;
        Name = name;
        Office = office;
    }

    public Location() {
        //To change body of created methods use File | Settings | File Templates.
    }

       /*
    public ArrayList<Location> GetAllLocations() {

        _GetAllLocationsTask =  new GetAllLocationsTask();
        _GetAllLocationsTask.doInBackground();
        _GetAllLocationsTask.execute((Void)null);




        return null;
    }
    */

    public Location(String ID) {
        this.ID = ID;
        //TODO: Add in Location Lookup
    }



    public boolean isOffice() {
        return Office;

    }

    public String getName() {
        return Name;
    }

    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return this.Name;
    }


    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     *         a positive integer if this instance is greater than
     *         {@code another}; 0 if this instance has the same order as
     *         {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(Object another) {
        if(another.getClass() == this.getClass()) {
            return Name.compareTo(((Location)another).getName());  //To change body of implemented methods use File | Settings | File Templates.
        }

        return 0;
    }
}
