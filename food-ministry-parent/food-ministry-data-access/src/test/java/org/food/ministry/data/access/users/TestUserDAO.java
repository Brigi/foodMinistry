package org.food.ministry.data.access.users;

import java.text.MessageFormat;

import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.FoodInventory;
import org.food.ministry.model.Household;
import org.food.ministry.model.IngredientsPool;
import org.food.ministry.model.RecipesPool;
import org.food.ministry.model.ShoppingList;
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
    private Household testHousehold;
    private UserDAO testUserDao;

    protected abstract UserDAO getUserDao();

    @Before
    public void startUp() {
        testUser = new User(0, EMAIL_ADDRESS, USER_NAME, PASSWORD);
        testHousehold = new Household(0, new FoodInventory(0), new ShoppingList(0), new IngredientsPool(0), new RecipesPool(0), "MyHousehold");
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
    public void testIsHouseholdReferenced() throws DataAccessException {
        testUser.addHousehold(testHousehold);
        testUserDao.save(testUser);
        Assert.assertTrue(testUserDao.isHouseholdUnreferenced(testHousehold));
    }

    @Test
    public void testIsHouseholdNotReferencedInitially() throws DataAccessException {
        testUserDao.save(testUser);
        Assert.assertFalse(testUserDao.isHouseholdUnreferenced(testHousehold));
    }

    @Test
    public void testIsHouseholdNotReferencedAfterRemoving() throws DataAccessException {
        testUser.addHousehold(testHousehold);
        testUserDao.save(testUser);
        Assert.assertTrue(testUserDao.isHouseholdUnreferenced(testHousehold));
        testUser.removeHousehold(testHousehold);
        testUserDao.update(testUser);
        Assert.assertFalse(testUserDao.isHouseholdUnreferenced(testHousehold));
    }

    @Test
    public void testIsHouseholdReferencedAfterRemovingWithTwoUsers() throws DataAccessException {
        User secondUser = new User(1, "other@mail.com", "OtherName", "pw");
        testUser.addHousehold(testHousehold);
        secondUser.addHousehold(testHousehold);
        testUserDao.save(testUser);
        testUserDao.save(secondUser);
        Assert.assertTrue(testUserDao.isHouseholdUnreferenced(testHousehold));
        testUser.removeHousehold(testHousehold);
        testUserDao.update(testUser);
        Assert.assertTrue(testUserDao.isHouseholdUnreferenced(testHousehold));
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
