package com.trainapp.services;

import com.trainapp.model.Reservation;
import com.trainapp.model.Ticket;
import com.trainapp.model.User;

public class ReservationService {

    public ReservationService(){}

    public Reservation createReservation(User traveller){
        Reservation reservation = new Reservation(traveller.getUserId(), traveller.getFirstName(), traveller.getLastName(), traveller.getAge());
        return  reservation;
    }

    public void saveReservation(){
        //save reservation to database
    }
}