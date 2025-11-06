package com.trainapp.services;

import com.trainapp.model.Connection;
import com.trainapp.model.Trip;
import com.trainapp.model.User;

public class TripService {

    public TripService(){
    }

    public Trip createTrip(Connection connection, User user){
        //save new trip to database
        return new Trip(connection, user);
    }
}
