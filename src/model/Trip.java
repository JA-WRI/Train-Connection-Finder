package model;

public class Trip {

    private String tripId;
    private Connection connection;
    private User user;

    public Trip(String tripId, Connection connection, User user) {
        this.tripId = tripId;
        this.connection = connection;
        this.user = user;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
