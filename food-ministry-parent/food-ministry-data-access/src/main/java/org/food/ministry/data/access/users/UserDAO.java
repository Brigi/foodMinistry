package org.food.ministry.data.access.users;

import org.food.ministry.data.access.DAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.User;

public interface UserDAO extends DAO<User> {

    public static final String NO_ID_FOUND_MESSAGE = "User for id {0} not found";
    
    public static final String NO_USER_FOUND_MESSAGE = "User {0} not found";

    public static final String NO_USER_WITH_EMAIL_ADDRESS_FOUND_MESSAGE = "No user found with email address {0}";

    public static final String INSUFFICIENT_AMOUNT_MESSAGE = "Insufficient amount of users found: {0}";

    User getUser(String emailAddress) throws DataAccessException;

    boolean doesEmailAddressExist(String emailAddress) throws DataAccessException;
}
