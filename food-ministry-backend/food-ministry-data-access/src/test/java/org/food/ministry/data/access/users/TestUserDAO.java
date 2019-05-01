package org.food.ministry.data.access.users;

import java.text.MessageFormat;

import org.food.ministry.data.access.DAO;
import org.food.ministry.data.access.TestBaseDAO;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.FoodInventory;
import org.food.ministry.model.Household;
import org.food.ministry.model.IngredientsPool;
import org.food.ministry.model.RecipesPool;
import org.food.ministry.model.ShoppingList;
import org.food.ministry.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class TestUserDAO extends TestBaseDAO<User> {

    private static final String USER_NAME = "MyName";
    private static final String EMAIL_ADDRESS = "my@mail.com";
    private static final String PASSWORD = "password";
    private Household testHousehold;

    protected abstract UserDAO getUserDao();

    @Before
    public void startUp() {
        super.startUp();
        testHousehold = new Household(0, new FoodInventory(0), new ShoppingList(0), new IngredientsPool(0), new RecipesPool(0), "MyHousehold");
    }

    @Override
    protected User getPersistenceObject() {
        return new User(0, EMAIL_ADDRESS, USER_NAME, PASSWORD);
    }

    @Override
    protected DAO<User> getDao() {
        return getUserDao();
    }

    @Test
    public void testGetUser() throws DataAccessException {
        final User testUser = getPersistenceObject();
        final UserDAO userDao = getUserDao();
        
        userDao.save(testUser);
        Assert.assertEquals(testUser, userDao.getUser(EMAIL_ADDRESS));
    }

    @Test
    public void testSaveItemTwice() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(UserDAO.USER_WITH_EMAIL_ADDRESS_ALREADY_EXISTS_MESSAGE, EMAIL_ADDRESS));
        final User testUser = getPersistenceObject();
        final User existingEmailAddressUser = new User(1, EMAIL_ADDRESS, "OtherName", "OtherPassword");
        final UserDAO userDao = getUserDao();
        
        userDao.save(testUser);
        userDao.save(existingEmailAddressUser);
    }
    
    @Test
    public void testUpdateUser() throws DataAccessException {
        final User testUser = getPersistenceObject();
        final String newEmailAddress = "new@mail.com";
        final UserDAO userDao = getUserDao();
        
        userDao.save(testUser);
        testUser.setEmailAddress(newEmailAddress);
        userDao.update(testUser);
        Assert.assertEquals(testUser, userDao.getUser(newEmailAddress));
    }

    @Test
    public void testUpdateWithMultipleUserContained() throws DataAccessException {
        final User testUser = getPersistenceObject();
        final String newEmailAddress = "new@mail.com";
        final UserDAO userDao = getUserDao();
        
        userDao.save(testUser);
        userDao.save(new User(1, "other@mail.com", "OtherName", "otherPassword"));
        testUser.setEmailAddress(newEmailAddress);
        userDao.update(testUser);
        Assert.assertEquals(testUser, userDao.getUser(newEmailAddress));
    }

    @Test
    public void testDoesEmailAddressExistPositive() throws DataAccessException {
        final UserDAO userDao = getUserDao();
        
        userDao.save(getPersistenceObject());
        Assert.assertTrue(userDao.doesEmailAddressExist(EMAIL_ADDRESS));
    }

    @Test
    public void testDoesEmailAddressExistNegative() throws DataAccessException {
        final UserDAO userDao = getUserDao();
        
        userDao.save(getPersistenceObject());
        Assert.assertFalse(userDao.doesEmailAddressExist("other@email.com"));
    }

    @Test
    public void testIsHouseholdReferenced() throws DataAccessException {
        final User testUser = getPersistenceObject();
        final UserDAO userDao = getUserDao();
        
        testUser.addHousehold(testHousehold);
        userDao.save(testUser);
        Assert.assertTrue(userDao.isHouseholdUnreferenced(testHousehold));
    }

    @Test
    public void testIsHouseholdNotReferencedInitially() throws DataAccessException {
        final UserDAO userDao = getUserDao();
        
        userDao.save(getPersistenceObject());
        Assert.assertFalse(userDao.isHouseholdUnreferenced(testHousehold));
    }

    @Test
    public void testIsHouseholdNotReferencedAfterRemoving() throws DataAccessException {
        final User testUser = getPersistenceObject();
        final UserDAO userDao = getUserDao();
        
        testUser.addHousehold(testHousehold);
        userDao.save(testUser);
        Assert.assertTrue(userDao.isHouseholdUnreferenced(testHousehold));
        testUser.removeHousehold(testHousehold);
        userDao.update(testUser);
        Assert.assertFalse(userDao.isHouseholdUnreferenced(testHousehold));
    }

    @Test
    public void testIsHouseholdReferencedAfterRemovingWithTwoUsers() throws DataAccessException {
        final User testUser = getPersistenceObject();        
        final User secondUser = new User(1, "other@mail.com", "OtherName", "pw");
        final UserDAO userDao = getUserDao();
        
        testUser.addHousehold(testHousehold);
        secondUser.addHousehold(testHousehold);
        userDao.save(testUser);
        userDao.save(secondUser);
        Assert.assertTrue(userDao.isHouseholdUnreferenced(testHousehold));
        testUser.removeHousehold(testHousehold);
        userDao.update(testUser);
        Assert.assertTrue(userDao.isHouseholdUnreferenced(testHousehold));
    }

    @Test
    public void testGetNonExistingUser() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(UserDAO.NO_USER_WITH_EMAIL_ADDRESS_FOUND_MESSAGE, EMAIL_ADDRESS));
        getUserDao().getUser(EMAIL_ADDRESS);
    }
}
