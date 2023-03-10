package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.Drink;
import com.kenzie.appserver.service.model.InvalidUserException;
import com.kenzie.appserver.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;


    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    /** ------------------------------------------------------------------------
     *  UserService.addNewUser
     *  ------------------------------------------------------------------------ **/
    @Test
    void addNewUser_addsUser(){
        // GIVEN
        final String id = randomUUID().toString();

        // WHEN
        final ArgumentCaptor<UserRecord> userRecordCaptor = ArgumentCaptor.forClass(UserRecord.class);
        when(userRepository.save(any(UserRecord.class))).then(i -> i.getArgumentAt(0, UserRecord.class));
        final User returnedUser = userService.addNewUser(id);

        // THEN
        verify(userRepository).save(userRecordCaptor.capture());

        final UserRecord capturedRecord = userRecordCaptor.getValue();

        Assertions.assertNotNull(returnedUser, "The object is returned");
        Assertions.assertEquals(returnedUser.getUserId(), id, "The id matches");
        Assertions.assertTrue(returnedUser.getDrinks().isEmpty(), "List is expected to be empty");

        Assertions.assertNotNull(capturedRecord, "The object is returned");
        Assertions.assertEquals(capturedRecord.getUserId(), id, "The id matches");
        Assertions.assertTrue(capturedRecord.getDrinks().isEmpty(), "List is expected to be empty");
    }
    @Test
    void addNewUser_NullId_ThrowsException() {
        // GIVEN

        // WHEN

        // THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.addNewUser(null), "Exception is expected to be thrown");
    }


    /** ------------------------------------------------------------------------
     *  UserService.getUserById
     *  ------------------------------------------------------------------------ **/

    @Test
    void getUserById_ReturnsCorrectUser() {
        // GIVEN
        final String id = randomUUID().toString();

        final List<Drink> drinks = List.of(new Drink(UUID.randomUUID().toString(), "Drink1", List.of("Ingredient"), id),
                new Drink(UUID.randomUUID().toString(), "Drink2", List.of("Ingredient"), id));

        final UserRecord record = new UserRecord();
        record.setUserId(id);
        record.setDrinks(drinks);

        // WHEN
        when(userRepository.findById(id)).thenReturn(Optional.of(record));
        final User user = userService.getUserById(id);

        // THEN
        Assertions.assertNotNull(user, "The object is returned");
        Assertions.assertEquals(record.getUserId(), id, "The id matches");
        Assertions.assertEquals(record.getDrinks(), drinks, "The drinks match");
    }

    @Test
    void getUserById_NullId_ThrowsException() {
        // GIVEN

        // WHEN

        // THEN
        Assertions.assertThrows(InvalidUserException.class, () -> userService.getUserById(null), "Exception is expected to be thrown");
    }

    /** ------------------------------------------------------------------------
     *  UserService.updateUser
     *  ------------------------------------------------------------------------ **/

    @Test
    void updateUser_UpdatesUser(){
        // GIVEN
        final String id = randomUUID().toString();

        final List<Drink> drinks = List.of(new Drink(UUID.randomUUID().toString(), "Drink1", List.of("Ingredient"), id),
                new Drink(UUID.randomUUID().toString(), "Drink2", List.of("Ingredient"), id));

        final  User user = new User(id, drinks);

        final UserRecord userRecord = new UserRecord();

        userRecord.setUserId(id);
        userRecord.setDrinks(drinks);

        // WHEN
        final ArgumentCaptor<UserRecord> userRecordCaptor = ArgumentCaptor.forClass(UserRecord.class);
        when(userRepository.findById(id)).thenReturn(Optional.of(userRecord));
        when(userRepository.save(any(UserRecord.class))).then(i -> i.getArgumentAt(0, UserRecord.class));
        final User returnedUser = userService.updateUser(user);

        // THEN
        verify(userRepository).save(userRecordCaptor.capture());

        final UserRecord capturedRecord = userRecordCaptor.getValue();

        Assertions.assertNotNull(returnedUser, "The object is returned");
        Assertions.assertEquals(returnedUser.getUserId(), id, "The id matches");
        Assertions.assertEquals(returnedUser.getDrinks(), drinks, "The drinks match");

        Assertions.assertNotNull(capturedRecord, "The object is returned");
        Assertions.assertEquals(capturedRecord.getUserId(), id, "The id matches");
        Assertions.assertEquals(capturedRecord.getDrinks(), drinks, "The drinks match");
    }

    @Test
    void updateUser_UserNotInDB_ThrowsException(){
        // GIVEN
        final String id = randomUUID().toString();
        final List<Drink> drinks = List.of(new Drink(UUID.randomUUID().toString(), "Drink1", List.of("Ingredient"), id),
                new Drink(UUID.randomUUID().toString(), "Drink2", List.of("Ingredient"), id));
        final User user = new User(id, drinks);
        final UserRecord userRecord = new UserRecord();

        userRecord.setUserId(id);
        userRecord.setDrinks(drinks);

        // WHEN

        // THEN
        Assertions.assertThrows(InvalidUserException.class, () -> userService.updateUser(user), "Exception should be thrown when user is not in DB");
    }

    @Test
    void updateUser_NullID_ThrowsException(){
        // GIVEN
        final String id = randomUUID().toString();
        final List<Drink> drinks = List.of(new Drink(UUID.randomUUID().toString(), "Drink1", List.of("Ingredient"), id), new Drink(UUID.randomUUID().toString(),  "Drink2", List.of("Ingredient"), id));
        final User user = new User(id, drinks);

        // WHEN

        // THEN
        Assertions.assertThrows(InvalidUserException.class, () -> userService.updateUser(user), "Exception should be thrown when user is not in DB");
    }

    @Test
    void updateUser_NullDrinks_ThrowsException(){
        // GIVEN
        final String id = randomUUID().toString();
        final User user = new User(id, null);

        // WHEN

        // THEN
        Assertions.assertThrows(InvalidUserException.class, () -> userService.updateUser(user), "Exception should be thrown when user is not in DB");
    }

    @Test
    void updateUser_EmptyDrinks_ThrowsException(){
        // GIVEN
        final String id = randomUUID().toString();
        final List<Drink> drinks = Collections.emptyList();
        final User user = new User(id, drinks);

        // WHEN

        // THEN
        Assertions.assertThrows(InvalidUserException.class, () -> userService.updateUser(user), "Exception should be thrown when no drinks are passed in.");
    }

    /** ------------------------------------------------------------------------
     *  UserService.updateUserDrinks
     *  ------------------------------------------------------------------------ **/

    @Test
    void updateUserDrinks_UpdatesUsersDrinks(){
        // GIVEN
        final String id = randomUUID().toString();
        final List<Drink> drinks = List.of(new Drink(UUID.randomUUID().toString(), "Drink1", List.of("Ingredient"), id),
                new Drink(UUID.randomUUID().toString(), "Drink2", List.of("Ingredient"), id));
        final List<Drink> newDrinks = List.of(new Drink(UUID.randomUUID().toString(), "Drink3", List.of("Ingredient"), id));
        final User user = new User(id, drinks);
        final UserRecord userRecord = new UserRecord();

        userRecord.setUserId(id);
        userRecord.setDrinks(drinks);

        // WHEN
        final ArgumentCaptor<UserRecord> userRecordCaptor = ArgumentCaptor.forClass(UserRecord.class);
        when(userRepository.findById(id)).thenReturn(Optional.of(userRecord));
        when(userRepository.save(any(UserRecord.class))).then(i -> i.getArgumentAt(0, UserRecord.class));
        final User returnedUser = userService.updateUserDrinks(user, newDrinks);

        // THEN
        verify(userRepository).save(userRecordCaptor.capture());
        final UserRecord capturedRecord = userRecordCaptor.getValue();

        Assertions.assertNotNull(returnedUser, "The object is returned");
        Assertions.assertEquals(returnedUser.getUserId(), id, "The id matches");
        Assertions.assertEquals(returnedUser.getDrinks(), newDrinks, "The drinks match");

        Assertions.assertNotNull(capturedRecord, "The object is returned");
        Assertions.assertEquals(capturedRecord.getUserId(), id, "The id matches");
        Assertions.assertEquals(capturedRecord.getDrinks(), newDrinks, "The drinks match");
    }

    @Test
    void updateUserDrinks_NullUser_ThrowsException() {
        // GIVEN
        final List<Drink> drinks = Collections.emptyList();

        // WHEN

        // THEN
        Assertions.assertThrows(InvalidUserException.class, () -> userService.updateUserDrinks(null, drinks), "Exception should be thrown when no drinks are passed in.");
    }

    @Test
    void updateUserDrinks_NullDrinks_ThrowsException(){
        // GIVEN
        final String id = randomUUID().toString();
        final User user = new User(id, null);

        // WHEN

        // THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUserDrinks(user, null), "Exception should be thrown when no drinks are passed in.");
    }


    @Test
    void updateUserDrinks_EmptyDrinks_ThrowsException(){
        // GIVEN
        final String id = randomUUID().toString();
        final List<Drink> drinks = Collections.emptyList();
        final User user = new User(id, drinks);

        // WHEN

        // THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUserDrinks(user, drinks), "Exception should be thrown when no drinks are passed in.");
    }

    @Test
    void updateUserDrinks_UserNotInDB_ThrowsException(){
        // GIVEN
        final  String id = randomUUID().toString();
        final List<Drink> drinks = List.of(new Drink(UUID.randomUUID().toString(), "Drink1", List.of("Ingredient"), id),
                new Drink(UUID.randomUUID().toString(), "Drink2", List.of("Ingredient"), id));
        final User user = new User(id, drinks);
        final UserRecord userRecord = new UserRecord();

        userRecord.setUserId(id);
        userRecord.setDrinks(drinks);

        // WHEN

        // THEN
        Assertions.assertThrows(InvalidUserException.class, () -> userService.updateUserDrinks(user, drinks), "Exception should be thrown when user is not in DB");
    }

    /** ------------------------------------------------------------------------
     *  UserService.getUsersDrinks
     *  ------------------------------------------------------------------------ **/

    @Test
    void getUsersDrinks_ReturnsCorrectDrinks() {
        // GIVEN
        final String id = randomUUID().toString();
        final List<Drink> drinks = List.of(new Drink(UUID.randomUUID().toString(), "Drink1", List.of("Ingredient"), id),
                new Drink(UUID.randomUUID().toString(), "Drink2", List.of("Ingredient"), id));
        final UserRecord record = new UserRecord();

        record.setUserId(id);
        record.setDrinks(drinks);
        final User user = new User(id, drinks);

        // WHEN
        when(userRepository.findById(id)).thenReturn(Optional.of(record));
        final List<Drink> returnedDrinks = userService.getUsersDrinks(user);

        // THEN
        Assertions.assertNotNull(returnedDrinks, "The object is returned");
        Assertions.assertEquals(returnedDrinks, drinks, "The drinks match");
    }

    @Test
    void getUsersDrinks_NullUser_ThrowsException() {
        // GIVEN

        // WHEN

        // THEN
        Assertions.assertThrows(InvalidUserException.class, () -> userService.getUsersDrinks(null), "Exception is expected to be thrown");
    }
}
