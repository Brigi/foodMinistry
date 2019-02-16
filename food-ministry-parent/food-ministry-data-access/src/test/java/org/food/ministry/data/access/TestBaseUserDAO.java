package org.food.ministry.data.access;

import java.text.MessageFormat;

import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public abstract class TestBaseUserDAO {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none(); 
    
    private static final String USER_NAME = "MyName";
    private static final String EMAIL_ADDRESS = "my@mail.com";
    private User testUser;
    private UserDAO testUserDao;
    
    protected abstract UserDAO getUserDao();
    
    @Before
    public void startUp() {
        testUser = new User(0, EMAIL_ADDRESS, USER_NAME);
        testUserDao = getUserDao();
    }
    
    @Test
    public void testSaveUser() throws DataAccessException {
        testUserDao.save(testUser);
    }
    
    @Test
    public void testGetUser() throws DataAccessException {
        testUserDao.save(testUser);
        Assert.assertEquals(testUser, testUserDao.getUser(EMAIL_ADDRESS));
    }
    
    @Test
    public void testUpdateUser() throws DataAccessException {
        final String newEmailAddress = "new@mail.com";
        testUserDao.save(testUser);
        testUserDao.update(testUser);
        Assert.assertEquals(testUser, testUserDao.getUser(newEmailAddress));
    }
    
    @Test
    public void testDeleteUser() throws DataAccessException {
        testUserDao.save(testUser);
        testUserDao.delete(testUser);
        Assert.assertTrue(testUserDao.getAll().isEmpty());
    }
    
    /*
    @Test
    public void testUpdateUserWithWrongParameterCount() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(UserDAO.INVALID_PARAMETER_COUNT_MESSAGE, USER_NAME, 3));
        final String newEmailAddress = "new@mail.com";
        final String newUserName = "MyNewName";
        testUserDao.save(testUser);
        testUserDao.update(testUser, new String[] {newEmailAddress, newUserName, "Unneeded Parameter"});
    }*/
    
    @Test
    public void testGetNonExistingUser() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(UserDAO.NO_USER_WITH_EMAIL_ADDRESS_FOUND_MESSAGE, EMAIL_ADDRESS));
        testUserDao.getUser(EMAIL_ADDRESS);
    }
    
    @Test
    public void testDeleteNonExistingUser() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(UserDAO.NO_USER_FOUND_MESSAGE, USER_NAME));
        testUserDao.delete(testUser);
    }
}
