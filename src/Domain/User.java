package Domain;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Reece Engle
 * Date: 5/29/13
 * Time: 9:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class User {

    private String ID;
    private String Username;
    private String Email;
    private String FullName;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getLocationID() {
        return LocationID;
    }

    public void setLocationID(String locationID) {
        LocationID = locationID;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        UpdatedAt = updatedAt;
    }

    public Location getLocation() {
        return Location;
    }

    public void setLocation(Location location) {
        Location = location;
    }

    private String LocationID;
    private String PhoneNumber;
    private Date CreatedAt;

    public User(String ID, String username, String email, String fullName,
                String location, String phoneNumber) {
        this.ID = ID;
        Username = username;
        Email = email;
        FullName = fullName;
        LocationID = location;
        PhoneNumber = phoneNumber;
    }

    private Date UpdatedAt;


    private Location Location;

    public User(String id) {
        ID = id;
    }

    //TODO: Create Image Field in User Object

}
