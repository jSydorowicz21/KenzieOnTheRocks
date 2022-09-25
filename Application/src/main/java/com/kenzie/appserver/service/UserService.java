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

        if (userRepository.findById(userId).isEmpty()){
            throw new InvalidUserException("User " + userId + " does not exist.");
        }

        return createUserFromRecord(userRepository.findById(userId).get());
    }

    public User updateUser(User user){

        if (user == null || user.getUserId().isEmpty() || user.getUserId() == null || userRepository.findById(user.getUserId()).isEmpty()){
            throw new InvalidUserException("User passed into updateUser is invalid");
        }

        userRepository.save(createRecordFromUser(user));

        return user;

    }

    public User updateUserDrinks(User user, List<Drink> drinks){

        UserRecord record = new UserRecord();
        record.setUserId(user.getUserId());
        record.setDrinks(drinks);

        userRepository.save(record);

        return createUserFromRecord(record);

    }

    public User deleteUser(User user){

        if (user == null || user.getUserId().isEmpty() || user.getUserId() == null || userRepository.findById(user.getUserId()).isEmpty()){
            throw new InvalidUserException("User passed into deleteUser is invalid");
        }

        if (!user.equals(createUserFromRecord(userRepository.findById(user.getUserId()).get()))){
            throw new InsufficientPrivilegeException("You may not delete a user that you are not.");
        }

        userRepository.delete(createRecordFromUser(user));

        return user;

    }

    public List<Drink> getUsersDrinks(User user){

        if (user == null || user.getUserId().isEmpty() || user.getUserId() == null || userRepository.findById(user.getUserId()).isEmpty()){
            throw new InvalidUserException("User passed into getUsersDrinks is invalid");
        }

        if (!user.equals(createUserFromRecord(userRepository.findById(user.getUserId()).get()))){
            throw new InsufficientPrivilegeException("You may not get the drinks for a user that you are not.");
        }

        return userRepository.findById(user.getUserId()).get().getDrinks();

    }

    public User createUserFromRecord(UserRecord record){

        return new User(record.getUserId(), record.getDrinks());

    }

    public UserRecord createRecordFromUser(User user){

        UserRecord record = new UserRecord();
        record.setUserId(user.getUserId());
        record.setDrinks(user.getDrinks());

        return record;
    }
}
