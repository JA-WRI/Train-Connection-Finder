package com.trainapp.model;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private int age;
    private boolean isBooker;
    private Trip trip;

    public User(String lastName, String firstName, String userId, int age, boolean isBooker) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.userId = userId;
        this.age = age;
        this.isBooker = isBooker;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Trip getTrip() {
        return trip;
    }
    public void setTrip(Trip trip) {this.trip = trip;}
    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}
    public boolean isBooker() {return isBooker;}
    public void setBooker(boolean booker) {isBooker = booker;}


}
