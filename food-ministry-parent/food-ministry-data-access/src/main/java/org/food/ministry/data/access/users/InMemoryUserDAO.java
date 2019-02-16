package org.food.ministry.data.access.users;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.Household;
import org.food.ministry.model.User;

public class InMemoryUserDAO implements UserDAO {

    private Map<Long, User> users = new HashMap<>();    
    
    @Override
    public User get(long id) {
        return users.get(id);
    }
    
    @Override
    public User getUser(String emailAddress) throws DataAccessException {
        List<User> correctUsers = users.values().parallelStream()
                .filter(element -> element.getEmailAddress().equals(emailAddress))
                .collect(Collectors.toList());
        if(correctUsers.size() == 0) {
            throw new DataAccessException(MessageFormat.format(NO_USER_WITH_EMAIL_ADDRESS_FOUND_MESSAGE, emailAddress));
        }
        
        return correctUsers.get(0);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public void save(User user) {
        users.put(Long.valueOf(users.hashCode()), user);
    }

    @Override
    public void update(User user) throws DataAccessException {
        List<User> foundUsers = users.values().parallelStream().filter(element -> element.getId() == user.getId()).collect(Collectors.toList());
        if(foundUsers.size() != 1) {
            throw new DataAccessException(MessageFormat.format(INSUFFICIENT_AMOUNT_MESSAGE, foundUsers.size()));
        }
        User householdToUpdate = foundUsers.get(0);
        householdToUpdate.setName(user.getName());
        householdToUpdate.setEmailAddress(user.getEmailAddress());
    }

    @Override
    public void delete(User user) throws DataAccessException {
        List<Long> correctUsers = users.entrySet().parallelStream()
                .filter(element -> element.getValue().equals(user))
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
        if(correctUsers.size() == 0) {
            throw new DataAccessException(MessageFormat.format(NO_USER_FOUND_MESSAGE, user.getName()));
        }
        
        users.remove(correctUsers.get(0));
    }
}
