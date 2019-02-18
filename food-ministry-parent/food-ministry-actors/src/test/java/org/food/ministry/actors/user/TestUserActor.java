package org.food.ministry.actors.user;

import java.text.MessageFormat;
import java.util.Map;

import org.food.ministry.actors.messages.DelegateMessage;
import org.food.ministry.actors.messages.IMessage;
import org.food.ministry.actors.user.messages.AddHouseholdMessage;
import org.food.ministry.actors.user.messages.AddHouseholdResultMessage;
import org.food.ministry.actors.user.messages.GetHouseholdsMessage;
import org.food.ministry.actors.user.messages.GetHouseholdsResultMessage;
import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.LoginResultMessage;
import org.food.ministry.actors.user.messages.RegisterMessage;
import org.food.ministry.actors.user.messages.RegisterResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.data.access.shoppinglist.ShoppingListDAO;
import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestProbe;

@RunWith(MockitoJUnitRunner.class)
public class TestUserActor {

    private static final String CORRUPTED_DATA_SOURCE_MESSAGE = "Underlying data source not responding correctly";
    private static final long USER_ID = 0;
    private static final String EMAIL_ADDRESS = "email@address.com";
    private static final String USER_NAME = "MyName";
    private static final String PASSWORD = "1234";
    private static final String HOUSEHOLD_NAME = "MyHousehold";

    @Mock
    private UserDAO userDao;
    @Mock
    private HouseholdDAO householdDao;
    @Mock
    private FoodInventoryDAO foodInventoryDao;
    @Mock
    private ShoppingListDAO shoppingListDao;
    @Mock
    private IngredientsPoolDAO ingredientsPoolDao;
    
    private ActorSystem system;
    private ActorRef userActor;
    private TestProbe probe;

    @Before
    public void startUp() throws DataAccessException {
        system = ActorSystem.create("user-system");
        probe = new TestProbe(system);
        IDGenerator.initializeGeneratorActor(system);
        userActor = system.actorOf(UserActor.props(userDao, householdDao, foodInventoryDao, shoppingListDao, ingredientsPoolDao), "user-actor");
    }

    @After
    public void teardown() {
        system = null;
    }

    // --------------- User Login section ---------------

    @Test
    public void testSuccessfulUserLogin() throws DataAccessException {
        Mockito.when(userDao.getUser(EMAIL_ADDRESS)).thenReturn(new User(USER_ID, EMAIL_ADDRESS, USER_NAME, PASSWORD));
        
        long loginMessageId = IDGenerator.getRandomID();
        userActor.tell(new LoginMessage(loginMessageId, USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        LoginResultMessage loginResultMessage = getLoginResultMessage(firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = getDelegateMessage(firstResultMessage, secondResultMessage);

        Assert.assertTrue(loginResultMessage.isSuccessful());
        Assert.assertEquals(Constants.NO_ERROR_MESSAGE, loginResultMessage.getErrorMessage());
        Assert.assertEquals(loginMessageId, loginResultMessage.getOriginId());
        Assert.assertEquals(loginResultMessage.getOriginId(), delegateResultMessage.getOriginId());
    }

    @Test
    public void testUserLoginWithWrongPassword() throws DataAccessException {
        Mockito.when(userDao.getUser(EMAIL_ADDRESS)).thenReturn(new User(USER_ID, EMAIL_ADDRESS, USER_NAME, PASSWORD));
        
        userActor.tell(new LoginMessage(IDGenerator.getRandomID(), USER_NAME, EMAIL_ADDRESS, "wrongPassword"), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        LoginResultMessage loginResultMessage = getLoginResultMessage(firstResultMessage, secondResultMessage);

        Assert.assertFalse(loginResultMessage.isSuccessful());
        Assert.assertEquals(Constants.WRONG_CREDENTIALS_MESSAGE, loginResultMessage.getErrorMessage());
    }

    @Test
    public void testUserLoginWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(CORRUPTED_DATA_SOURCE_MESSAGE)).when(userDao).getUser(ArgumentMatchers.any());
        
        userActor.tell(new LoginMessage(IDGenerator.getRandomID(), USER_NAME, EMAIL_ADDRESS, "wrongPassword"), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        LoginResultMessage loginResultMessage = getLoginResultMessage(firstResultMessage, secondResultMessage);

        Assert.assertFalse(loginResultMessage.isSuccessful());
        Assert.assertEquals(MessageFormat.format("Login for user {0} failed: {1}", USER_NAME, CORRUPTED_DATA_SOURCE_MESSAGE), loginResultMessage.getErrorMessage());
    }

    // --------------- User Registration section ---------------

    @Test
    public void testSuccessfulUserRegistration() throws DataAccessException {
        Mockito.when(userDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(userDao.doesEmailAddressExist(EMAIL_ADDRESS)).thenReturn(false);
        
        userActor.tell(new RegisterMessage(IDGenerator.getRandomID(), USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RegisterResultMessage registrationResultMessage = getRegistrationResultMessage(firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = getDelegateMessage(firstResultMessage, secondResultMessage);

        Assert.assertTrue(registrationResultMessage.isSuccessful());
        Assert.assertEquals(Constants.NO_ERROR_MESSAGE, registrationResultMessage.getErrorMessage());
        Assert.assertEquals(registrationResultMessage.getOriginId(), delegateResultMessage.getOriginId());
        Assert.assertTrue(delegateResultMessage.getOriginId() != 0);
    }

    @Test
    public void testSuccessfulUserRegistrationIncludingDuplicatedIdGeneration() throws DataAccessException {
        Mockito.when(userDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(true).thenReturn(false);
        Mockito.when(userDao.doesEmailAddressExist(EMAIL_ADDRESS)).thenReturn(false);
        
        userActor.tell(new RegisterMessage(IDGenerator.getRandomID(), USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RegisterResultMessage registrationResultMessage = getRegistrationResultMessage(firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = getDelegateMessage(firstResultMessage, secondResultMessage);

        Assert.assertTrue(registrationResultMessage.isSuccessful());
        Assert.assertEquals(Constants.NO_ERROR_MESSAGE, registrationResultMessage.getErrorMessage());
        Assert.assertEquals(registrationResultMessage.getOriginId(), delegateResultMessage.getOriginId());
        Assert.assertTrue(delegateResultMessage.getOriginId() != 0);
    }

    @Test
    public void testUserRegistrationWithExistingEmailAddress() throws DataAccessException {
        Mockito.when(userDao.doesEmailAddressExist(EMAIL_ADDRESS)).thenReturn(true);
        
        userActor.tell(new RegisterMessage(IDGenerator.getRandomID(), USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RegisterResultMessage registrationResultMessage = getRegistrationResultMessage(firstResultMessage, secondResultMessage);

        Assert.assertFalse(registrationResultMessage.isSuccessful());
        Assert.assertEquals(Constants.EMAIL_ADDRESS_ALREADY_EXISTS_MESSAGE, registrationResultMessage.getErrorMessage());
    }

    @Test
    public void testUserRegistrationWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(CORRUPTED_DATA_SOURCE_MESSAGE)).when(userDao).save(ArgumentMatchers.any());
        
        userActor.tell(new RegisterMessage(IDGenerator.getRandomID(), USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RegisterResultMessage registrationResultMessage = getRegistrationResultMessage(firstResultMessage, secondResultMessage);

        Assert.assertFalse(registrationResultMessage.isSuccessful());
        Assert.assertEquals(MessageFormat.format("Registration of user {0} failed: {1}", USER_NAME, CORRUPTED_DATA_SOURCE_MESSAGE), registrationResultMessage.getErrorMessage());
    }
    
    // --------------- User Registration and Login section ---------------
    
    @Test
    public void testSuccessfulUserRegistrationAndLogin() throws DataAccessException {
        Mockito.when(userDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(userDao.doesEmailAddressExist(EMAIL_ADDRESS)).thenReturn(false);
        UserContainer userContainer = new UserContainer();
        Mockito.doAnswer(setRedirectedUserAnswer(userContainer)).when(userDao).save(ArgumentMatchers.any());
        Mockito.when(userDao.getUser(EMAIL_ADDRESS)).then(getRedirectedUserAnswer(userContainer));        
        
        // Register User
        userActor.tell(new RegisterMessage(IDGenerator.getRandomID(), USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RegisterResultMessage registrationResultMessage = getRegistrationResultMessage(firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = getDelegateMessage(firstResultMessage, secondResultMessage);

        Assert.assertTrue(registrationResultMessage.isSuccessful());
        Assert.assertEquals(Constants.NO_ERROR_MESSAGE, registrationResultMessage.getErrorMessage());
        Assert.assertEquals(registrationResultMessage.getOriginId(), delegateResultMessage.getOriginId());
        Assert.assertTrue(delegateResultMessage.getOriginId() != 0);
        
        // Login User
        userActor.tell(new LoginMessage(IDGenerator.getRandomID(), USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        firstResultMessage = probe.expectMsgClass(IMessage.class);
        secondResultMessage = probe.expectMsgClass(IMessage.class);

        LoginResultMessage loginResultMessage = getLoginResultMessage(firstResultMessage, secondResultMessage);
        delegateResultMessage = getDelegateMessage(firstResultMessage, secondResultMessage);

        Assert.assertTrue(loginResultMessage.isSuccessful());
        Assert.assertEquals(Constants.NO_ERROR_MESSAGE, loginResultMessage.getErrorMessage());
        Assert.assertEquals(loginResultMessage.getOriginId(), delegateResultMessage.getOriginId());
        Assert.assertTrue(delegateResultMessage.getOriginId() != 0);
    }
    
    // --------------- Household section ---------------
    
    @Test
    public void testSuccessfulGetEmptyHouseholds() throws DataAccessException {
        Mockito.when(userDao.get(ArgumentMatchers.anyLong())).thenReturn(new User(USER_ID, EMAIL_ADDRESS, USER_NAME, PASSWORD));
        
        userActor.tell(new GetHouseholdsMessage(IDGenerator.getRandomID(), USER_ID), probe.ref());
        
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetHouseholdsResultMessage getHouseholdsResultMessage = getGetHouseholdsResultMessage(firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = getDelegateMessage(firstResultMessage, secondResultMessage);

        Assert.assertTrue(getHouseholdsResultMessage.isSuccessful());
        Assert.assertEquals(Constants.NO_ERROR_MESSAGE, getHouseholdsResultMessage.getErrorMessage());
        Assert.assertEquals(getHouseholdsResultMessage.getOriginId(), delegateResultMessage.getOriginId());
        Assert.assertTrue(delegateResultMessage.getOriginId() != 0);
        
        Assert.assertTrue(getHouseholdsResultMessage.getHouseholdIdsWithName().isEmpty());
    }
    
    @Test
    public void testGetHouseholdsWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(CORRUPTED_DATA_SOURCE_MESSAGE)).when(userDao).get(ArgumentMatchers.anyLong());
        
        long requestId = IDGenerator.getRandomID();
        userActor.tell(new GetHouseholdsMessage(requestId, USER_ID), probe.ref());
        
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetHouseholdsResultMessage getHouseholdsResultMessage = getGetHouseholdsResultMessage(firstResultMessage, secondResultMessage);

        Assert.assertFalse(getHouseholdsResultMessage.isSuccessful());
        Assert.assertEquals(requestId, getHouseholdsResultMessage.getOriginId());
        Assert.assertEquals(MessageFormat.format("Getting households for user id {0} failed: {1}", USER_ID, CORRUPTED_DATA_SOURCE_MESSAGE), getHouseholdsResultMessage.getErrorMessage());
        
        Assert.assertTrue(getHouseholdsResultMessage.getHouseholdIdsWithName().isEmpty());
    }
    
    @Test
    public void testSuccessfulAddHousehold() throws DataAccessException {
        Mockito.when(userDao.get(ArgumentMatchers.anyLong())).thenReturn(new User(USER_ID, EMAIL_ADDRESS, USER_NAME, PASSWORD));
        Mockito.when(householdDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(foodInventoryDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(shoppingListDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(ingredientsPoolDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        
        userActor.tell(new AddHouseholdMessage(IDGenerator.getRandomID(), USER_ID, HOUSEHOLD_NAME), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddHouseholdResultMessage addHouseholdsResultMessage = getAddHouseholdResultMessage(firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = getDelegateMessage(firstResultMessage, secondResultMessage);

        Assert.assertTrue(addHouseholdsResultMessage.isSuccessful());
        Assert.assertEquals(Constants.NO_ERROR_MESSAGE, addHouseholdsResultMessage.getErrorMessage());
        Assert.assertEquals(addHouseholdsResultMessage.getOriginId(), delegateResultMessage.getOriginId());
        Assert.assertTrue(delegateResultMessage.getOriginId() != 0);
    }
    
    @Test
    public void testAddHouseholdWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(CORRUPTED_DATA_SOURCE_MESSAGE)).when(userDao).get(ArgumentMatchers.anyLong());
        
        long requestId = IDGenerator.getRandomID();
        userActor.tell(new AddHouseholdMessage(requestId, USER_ID, HOUSEHOLD_NAME), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddHouseholdResultMessage addHouseholdsResultMessage = getAddHouseholdResultMessage(firstResultMessage, secondResultMessage);

        Assert.assertFalse(addHouseholdsResultMessage.isSuccessful());
        Assert.assertEquals(requestId, addHouseholdsResultMessage.getOriginId());
        Assert.assertEquals(MessageFormat.format("Getting households for user id {0} failed: {1}", USER_ID, CORRUPTED_DATA_SOURCE_MESSAGE), addHouseholdsResultMessage.getErrorMessage());
    }
    
    @Test
    public void testSuccessfulGetHouseholdsAfterAddHousehold() throws DataAccessException {
        User user = new User(USER_ID, EMAIL_ADDRESS, USER_NAME, PASSWORD);
        Mockito.when(userDao.get(ArgumentMatchers.anyLong())).thenReturn(user);
        Mockito.when(householdDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(foodInventoryDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(shoppingListDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(ingredientsPoolDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        
        userActor.tell(new AddHouseholdMessage(IDGenerator.getRandomID(), USER_ID, HOUSEHOLD_NAME), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddHouseholdResultMessage addHouseholdsResultMessage = getAddHouseholdResultMessage(firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = getDelegateMessage(firstResultMessage, secondResultMessage);

        Assert.assertTrue(addHouseholdsResultMessage.isSuccessful());
        Assert.assertEquals(Constants.NO_ERROR_MESSAGE, addHouseholdsResultMessage.getErrorMessage());
        Assert.assertEquals(addHouseholdsResultMessage.getOriginId(), delegateResultMessage.getOriginId());
        Assert.assertTrue(delegateResultMessage.getOriginId() != 0);
        
        userActor.tell(new GetHouseholdsMessage(IDGenerator.getRandomID(), USER_ID), probe.ref());
        firstResultMessage = probe.expectMsgClass(IMessage.class);
        secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetHouseholdsResultMessage getHouseholdsResultMessage = getGetHouseholdsResultMessage(firstResultMessage, secondResultMessage);
        delegateResultMessage = getDelegateMessage(firstResultMessage, secondResultMessage);

        Assert.assertTrue(getHouseholdsResultMessage.isSuccessful());
        Assert.assertEquals(Constants.NO_ERROR_MESSAGE, getHouseholdsResultMessage.getErrorMessage());
        Assert.assertEquals(getHouseholdsResultMessage.getOriginId(), delegateResultMessage.getOriginId());
        Assert.assertTrue(delegateResultMessage.getOriginId() != 0);
        
        Map<Long, String> households = getHouseholdsResultMessage.getHouseholdIdsWithName();
        Assert.assertEquals(1, households.size());
        Assert.assertTrue(households.containsValue(HOUSEHOLD_NAME));
    }
    
    // --------------- Helper functions section ---------------

    private LoginResultMessage getLoginResultMessage(IMessage firstResultMessage, IMessage secondResultMessage) {
        if(firstResultMessage instanceof LoginResultMessage) {
            return (LoginResultMessage) firstResultMessage;
        } else if(secondResultMessage instanceof LoginResultMessage) {
            return (LoginResultMessage) secondResultMessage;
        }
        return null;
    }

    private RegisterResultMessage getRegistrationResultMessage(IMessage firstResultMessage, IMessage secondResultMessage) {
        if(firstResultMessage instanceof RegisterResultMessage) {
            return (RegisterResultMessage) firstResultMessage;
        } else if(secondResultMessage instanceof RegisterResultMessage) {
            return (RegisterResultMessage) secondResultMessage;
        }
        return null;
    }
    
    private GetHouseholdsResultMessage getGetHouseholdsResultMessage(IMessage firstResultMessage, IMessage secondResultMessage) {
        if(firstResultMessage instanceof GetHouseholdsResultMessage) {
            return (GetHouseholdsResultMessage) firstResultMessage;
        } else if(secondResultMessage instanceof GetHouseholdsResultMessage) {
            return (GetHouseholdsResultMessage) secondResultMessage;
        }
        return null;
    }
    
    private AddHouseholdResultMessage getAddHouseholdResultMessage(IMessage firstResultMessage, IMessage secondResultMessage) {
        if(firstResultMessage instanceof AddHouseholdResultMessage) {
            return (AddHouseholdResultMessage) firstResultMessage;
        } else if(secondResultMessage instanceof AddHouseholdResultMessage) {
            return (AddHouseholdResultMessage) secondResultMessage;
        }
        return null;
    }

    private DelegateMessage getDelegateMessage(IMessage firstResultMessage, IMessage secondResultMessage) {
        if(firstResultMessage instanceof DelegateMessage) {
            return (DelegateMessage) firstResultMessage;
        } else if(secondResultMessage instanceof DelegateMessage) {
            return (DelegateMessage) secondResultMessage;
        }
        return null;
    }
    
    private Answer<Object> setRedirectedUserAnswer(UserContainer userContainer) {
        return (invocation) -> {
            User user = (User) invocation.getArgument(0);
            userContainer.setStoredUser(user);
            return null;
        };
    }
    
    private Answer<User> getRedirectedUserAnswer(UserContainer userContainer) {
        return (invocation) -> {
            return userContainer.getStoredUser();
        };
    }
    
    private class UserContainer {
        private User storedUser;

        public User getStoredUser() {
            return storedUser;
        }

        public void setStoredUser(User storedUser) {
            this.storedUser = storedUser;
        }
    }
}
