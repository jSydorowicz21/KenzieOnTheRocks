package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.Drink;
import com.kenzie.appserver.service.model.InsufficientPrivilegeException;
import com.kenzie.appserver.service.model.InvalidUserException;
import com.kenzie.appserver.service.model.User;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private LambdaServiceClient lambdaServiceClient;

    public UserService(UserRepository userRepository, LambdaServiceClient lambdaServiceClient) {
        this.userRepository = userRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public User getUserById(String userId){

        if (userId == null || userRepository.findById(userId).isEmpty()){
            throw new InvalidUserException("User " + userId + " does not exist.");
        }

        return createUserFromRecord(userRepository.findById(userId).get());
    }

    public User updateUser(User user){

        if (user == null ||  user.getUserId() == null || user.getUserId().isEmpty() ||
                user.getDrinks() == null || user.getDrinks().isEmpty()){
            throw new InvalidUserException("User passed into updateUser is invalid");
        }

        checkUserIsInDB(user);

        userRepository.save(createRecordFromUser(user));

        return user;

    }

    public User updateUserDrinks(User user, List<Drink> drinks){
        if (user == null || user.getUserId().isEmpty() || user.getUserId() == null) {
            throw new InvalidUserException("User passed into updateUser is invalid");
        }

        if (drinks == null || drinks.isEmpty()){
            throw new IllegalArgumentException("Drinks is null or empty");
        }

        checkUserIsInDB(user);


        UserRecord record = new UserRecord();
        record.setUserId(user.getUserId());
        record.setDrinks(drinks);

        userRepository.save(record);

        return createUserFromRecord(record);

    }

    public List<Drink> getUsersDrinks(User user){

        if (user == null || user.getUserId().isEmpty() || user.getUserId() == null ){
            throw new InvalidUserException("User passed into getUsersDrinks is invalid");
        }

        checkUserIsInDB(user);


        return userRepository.findById(user.getUserId()).get().getDrinks();

    }

    private User createUserFromRecord(UserRecord record){

        return new User(record.getUserId(), record.getDrinks());

    }

    private UserRecord createRecordFromUser(User user){

        UserRecord record = new UserRecord();
        record.setUserId(user.getUserId());
        record.setDrinks(user.getDrinks());

        return record;
    }

    private void checkUserIsInDB(User user){
        try{
            userRepository.findById(user.getUserId()).get();
        }
        catch (NullPointerException e) {
            throw new InvalidUserException("User is not in database");
        }
    }
}
