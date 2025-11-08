package com.trainapp.DTO;

public class UserDTO {
    private String fname;
    private String lname;
    private int age;
    private boolean isBooker;

    public UserDTO(String fname, String lname, int age, boolean isBooker) {
        this.fname = fname;
        this.lname = lname;
        this.age = age;
        this.isBooker = isBooker;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isBooker() {
        return isBooker;
    }

    public void setBooker(boolean booker) {
        isBooker = booker;
    }
}
