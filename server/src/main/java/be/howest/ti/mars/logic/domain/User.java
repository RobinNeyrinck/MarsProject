package be.howest.ti.mars.logic.domain;

public class User {
    private int userID;
    private final String firstname;
    private final String lastname;
    private final String avatar;

    private static final int NOT_YET_ASSIGNED = -1;


    public User(int userID, String firstname, String lastname, String avatar) {
        this.userID = userID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.avatar = avatar;
    }

    public User(String firstname, String lastname, String avatar) {
        this(NOT_YET_ASSIGNED, firstname, lastname, avatar);
    }
    public User(String firstname, String lastname) {
        this(NOT_YET_ASSIGNED, firstname, lastname, "");
    }

    public User(User user) {
        this(user.userID, user.firstname, user.lastname, user.avatar);
    }

    public int getUserID() {
        return userID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAvatar() {
        return avatar;
    }
}
