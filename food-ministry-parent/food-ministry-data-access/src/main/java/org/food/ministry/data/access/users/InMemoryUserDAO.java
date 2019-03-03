package org.food.ministry.data.access.users;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.food.ministry.data.access.InMemoryBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Household;
import org.food.ministry.model.User;

/**
 * A data access object class, which handles the access to persisted
 * {@link User} objects via an in-memory data structure. This class is not meant
 * to be used for production, but for testing only.
 * 
 * @author Maximilian Briglmeier
 * @since 18.02.2019
 */
public class InMemoryUserDAO extends InMemoryBaseDAO<User> implements UserDAO {

    @Override
    public void save(User user) throws DataAccessException {
        if(!getItems().values().parallelStream().filter(item -> item.getEmailAddress().equals(user.getEmailAddress())).findAny().isEmpty()) {
            throw new DataAccessException(MessageFormat.format(USER_WITH_EMAIL_ADDRESS_ALREADY_EXISTS_MESSAGE, user.getEmailAddress()));
        }
        super.save(user);
    }
    
    @Override
    public User getUser(String emailAddress) throws DataAccessException {
        List<User> correctUsers = getItems().values().parallelStream().filter(element -> element.getEmailAddress().equals(emailAddress)).collect(Collectors.toList());
        if(correctUsers.size() == 0) {
            throw new DataAccessException(MessageFormat.format(NO_USER_WITH_EMAIL_ADDRESS_FOUND_MESSAGE, emailAddress));
        }

        return correctUsers.get(0);
    }

    @Override
    public void update(User user) throws DataAccessException {
        User userToUpdate = getItemToUpdate(user);
        userToUpdate.setName(user.getName());
        userToUpdate.setEmailAddress(user.getEmailAddress());
    }

    @Override
    public boolean doesEmailAddressExist(String emailAddress) throws DataAccessException {
        long amountOfEmailAddresses = getItems().values().parallelStream().filter(element -> emailAddress.equals(element.getEmailAddress())).collect(Collectors.counting());
        return amountOfEmailAddresses > 0;
    }

    @Override
    public boolean isHouseholdUnreferenced(Household household) throws DataAccessException {
        return getItems().values().parallelStream().anyMatch(user -> user.getHouseholds().parallelStream().anyMatch(householdElement -> householdElement.equals(household)));
    }
}
