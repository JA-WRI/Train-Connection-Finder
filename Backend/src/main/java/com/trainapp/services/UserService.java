package com.trainapp.services;

import com.trainapp.DTO.UserDTO;
import com.trainapp.model.User;

public class UserService {

    public UserService(){}

    public User createUser(UserDTO userDTO){
        User newUser = new User(userDTO.getLname(), userDTO.getFname(), userDTO.getAge(), userDTO.isBooker());

        saveUser(newUser);
        return newUser;

    }
    public void saveUser(User user){
        //save the user in the database
    }


}
