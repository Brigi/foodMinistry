package org.food.ministry.data.access.users;

import org.food.ministry.data.access.DAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Household;
import org.food.ministry.model.User;

public interface UserDAO extends DAO<User> {

    public static final String NO_USER_WITH_EMAIL_ADDRESS_FOUND_MESSAGE = "No user found with email address {0}";

    public static final String USER_WITH_EMAIL_ADDRESS_ALREADY_EXISTS_MESSAGE = "User with email address {0} already exists";
    
    User getUser(String emailAddress) throws DataAccessException;

    boolean isHouseholdUnreferenced(Household household) throws DataAccessException;

    boolean doesEmailAddressExist(String emailAddress) throws DataAccessException;
}
