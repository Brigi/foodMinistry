package org.food.ministry.data.access.users;

import java.text.MessageFormat;

import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public abstract class TestUserDAO {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final String USER_NAME = "MyName";
    private static final String EMAIL_ADDRESS = "my@mail.com";
    private static final String PASSWORD = "password";
    private User testUser;
    private UserDAO testUserDao;

    protected abstract UserDAO getUserDao();

    @Before
    public void startUp() {
        testUser = new User(0, EMAIL_ADDRESS, USER_NAME, PASSWORD);
        testUserDao = getUserDao();
    }

    @Test
    public void testSaveUser() throws DataAccessException {
        testUserDao.save(testUser);
    }
    
    @Test
    public void testGet() throws DataAccessException {
        testUserDao.save(testUser);
        Assert.assertEquals(testUser, testUserDao.get(testUser.getId()));
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
        testUser.setEmailAddress(newEmailAddress);
        testUserDao.update(testUser);
        Assert.assertEquals(testUser, testUserDao.getUser(newEmailAddress));
    }

    @Test
    public void testUpdateWithMultipleUserContained() throws DataAccessException {
        final String newEmailAddress = "new@mail.com";
        testUserDao.save(testUser);
        testUserDao.save(new User(1, "other@mail.com", "OtherName", "otherPassword"));
        testUser.setEmailAddress(newEmailAddress);
        testUserDao.update(testUser);
        Assert.assertEquals(testUser, testUserDao.getUser(newEmailAddress));
    }
    
    @Test
    public void testDeleteUser() throws DataAccessException {
        testUserDao.save(testUser);
        testUserDao.delete(testUser);
        Assert.assertTrue(testUserDao.getAll().isEmpty());
    }

    @Test
    public void testDoesIdExistsPositive() throws DataAccessException {
        testUserDao.save(testUser);
        Assert.assertTrue(testUserDao.doesIdExist(0));
    }

    @Test
    public void testDoesIdExistsNegative() throws DataAccessException {
        testUserDao.save(testUser);
        Assert.assertFalse(testUserDao.doesIdExist(1));
    }

    @Test
    public void testDoesEmailAddressExistPositive() throws DataAccessException {
        testUserDao.save(testUser);
        Assert.assertTrue(testUserDao.doesEmailAddressExist(EMAIL_ADDRESS));
    }

    @Test
    public void testDoesEmailAddressExistNegative() throws DataAccessException {
        testUserDao.save(testUser);
        Assert.assertFalse(testUserDao.doesEmailAddressExist("other@email.com"));
    }

    @Test
    public void testGetNonExistingID() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(UserDAO.NO_ID_FOUND_MESSAGE, 0));
        testUserDao.get(0);
    }
    
    @Test
    public void testGetNonExistingUser() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(UserDAO.NO_USER_WITH_EMAIL_ADDRESS_FOUND_MESSAGE, EMAIL_ADDRESS));
        testUserDao.getUser(EMAIL_ADDRESS);
    }
    
    @Test
    public void testUpdateNonExistingUser() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(UserDAO.INSUFFICIENT_AMOUNT_MESSAGE, 0));
        testUserDao.update(testUser);
    }

    @Test
    public void testDeleteNonExistingUser() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(UserDAO.NO_USER_FOUND_MESSAGE, USER_NAME));
        testUserDao.delete(testUser);
    }
}
