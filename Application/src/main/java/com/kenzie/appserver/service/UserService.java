package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.Drink;
import com.kenzie.appserver.service.model.InvalidUserException;
import com.kenzie.appserver.service.model.User;
import com.kenzie.ata.ExcludeFromJacocoGeneratedReport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addNewUser(String userId){
        if (userId == null){
            throw new IllegalArgumentException("No user Id entered");
        }

        final UserRecord record = new UserRecord();
        record.setUserId(userId);
        record.setDrinks(Collections.emptyList());

        userRepository.save(record);

        return createUserFromRecord(record);
    }

    public User getUserById(String userId){
        if (userId == null || userId.isEmpty()){
            throw new InvalidUserException("User " + userId + " does not exist.");
        }

        try {
            userRepository.findById(userId).get();
        } catch (NullPointerException e){
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

        final UserRecord record = new UserRecord();
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

        return getUserById(user.getUserId()).getDrinks();
    }
    @ExcludeFromJacocoGeneratedReport
    public List<Drink> addDrinkToList(User user, Drink drink) {
        if (user == null || user.getUserId().isEmpty() || user.getUserId() == null ){
            throw new InvalidUserException("User passed into getUsersDrinks is invalid");
        }

        checkUserIsInDB(user);

        final List<Drink> drinks = new ArrayList<>(user.getDrinks());

        drinks.add(drink);

        updateUserDrinks(user, drinks);

        return user.getDrinks();
    }

    private User createUserFromRecord(UserRecord record){
        return new User(record.getUserId(), record.getDrinks());
    }

    private UserRecord createRecordFromUser(User user){
        final UserRecord record = new UserRecord();
        record.setUserId(user.getUserId());
        record.setDrinks(user.getDrinks());

        return record;
    }

    private void checkUserIsInDB(User user){
        try{
            userRepository.findById(user.getUserId()).get();
        } catch (NullPointerException e) {
            throw new InvalidUserException("User is not in database");
        }
    }
}
