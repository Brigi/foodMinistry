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
import org.food.ministry.actors.user.messages.RemoveHouseholdMessage;
import org.food.ministry.actors.user.messages.RemoveHouseholdResultMessage;
import org.food.ministry.actors.user.util.MessageUtil;
import org.food.ministry.actors.user.util.RedirectAnswer;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.data.access.foodinventory.FoodInventoryDAO;
import org.food.ministry.data.access.household.HouseholdDAO;
import org.food.ministry.data.access.ingredientspool.IngredientsPoolDAO;
import org.food.ministry.data.access.shoppinglist.ShoppingListDAO;
import org.food.ministry.data.access.users.UserDAO;
import org.food.ministry.model.FoodInventory;
import org.food.ministry.model.Household;
import org.food.ministry.model.IngredientsPool;
import org.food.ministry.model.RecipesPool;
import org.food.ministry.model.ShoppingList;
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

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestProbe;

@RunWith(MockitoJUnitRunner.class)
public class TestUserActor {

    private static final long USER_ID = 0;
    private static final long HOUSEHOLD_ID = 1;
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

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new LoginMessage(messageId, USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        LoginResultMessage loginResultMessage = MessageUtil.getMessageByClass(LoginResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, loginResultMessage, delegateResultMessage);
    }

    @Test
    public void testUserLoginWithWrongPassword() throws DataAccessException {
        Mockito.when(userDao.getUser(EMAIL_ADDRESS)).thenReturn(new User(USER_ID, EMAIL_ADDRESS, USER_NAME, PASSWORD));

        userActor.tell(new LoginMessage(IDGenerator.getRandomID(), USER_NAME, EMAIL_ADDRESS, "wrongPassword"), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        LoginResultMessage loginResultMessage = MessageUtil.getMessageByClass(LoginResultMessage.class, firstResultMessage, secondResultMessage);

        Assert.assertFalse(loginResultMessage.isSuccessful());
        Assert.assertEquals(Constants.WRONG_CREDENTIALS_MESSAGE, loginResultMessage.getErrorMessage());
    }

    @Test
    public void testUserLoginWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(userDao).getUser(ArgumentMatchers.any());

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new LoginMessage(messageId, USER_NAME, EMAIL_ADDRESS, "wrongPassword"), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        LoginResultMessage loginResultMessage = MessageUtil.getMessageByClass(LoginResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Login for user {0} failed: {1}", USER_NAME, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, loginResultMessage, delegateMessage);
    }

    // --------------- User Registration section ---------------

    @Test
    public void testSuccessfulUserRegistration() throws DataAccessException {
        Mockito.when(userDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(userDao.doesEmailAddressExist(EMAIL_ADDRESS)).thenReturn(false);

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new RegisterMessage(messageId, USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RegisterResultMessage registrationResultMessage = MessageUtil.getMessageByClass(RegisterResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, registrationResultMessage, delegateResultMessage);
    }

    @Test
    public void testSuccessfulUserRegistrationIncludingDuplicatedIdGeneration() throws DataAccessException {
        Mockito.when(userDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(true).thenReturn(false);
        Mockito.when(userDao.doesEmailAddressExist(EMAIL_ADDRESS)).thenReturn(false);

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new RegisterMessage(messageId, USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RegisterResultMessage registrationResultMessage = MessageUtil.getMessageByClass(RegisterResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, registrationResultMessage, delegateResultMessage);
    }

    @Test
    public void testUserRegistrationWithExistingEmailAddress() throws DataAccessException {
        Mockito.when(userDao.doesEmailAddressExist(EMAIL_ADDRESS)).thenReturn(true);

        userActor.tell(new RegisterMessage(IDGenerator.getRandomID(), USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RegisterResultMessage registrationResultMessage = MessageUtil.getMessageByClass(RegisterResultMessage.class, firstResultMessage, secondResultMessage);

        Assert.assertFalse(registrationResultMessage.isSuccessful());
        Assert.assertEquals(Constants.EMAIL_ADDRESS_ALREADY_EXISTS_MESSAGE, registrationResultMessage.getErrorMessage());
    }

    @Test
    public void testUserRegistrationWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(userDao).save(ArgumentMatchers.any());

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new RegisterMessage(messageId, USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RegisterResultMessage registrationResultMessage = MessageUtil.getMessageByClass(RegisterResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Registration of user {0} failed: {1}", USER_NAME, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, registrationResultMessage, delegateMessage);
    }

    // --------------- User Registration and Login section ---------------

    @Test
    public void testSuccessfulUserRegistrationAndLogin() throws DataAccessException {
        Mockito.when(userDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(userDao.doesEmailAddressExist(EMAIL_ADDRESS)).thenReturn(false);
        RedirectAnswer<User> redirectAnwser = new RedirectAnswer<>();
        Mockito.doAnswer(redirectAnwser.setRedirectedUserAnswer()).when(userDao).save(ArgumentMatchers.any());
        Mockito.when(userDao.getUser(EMAIL_ADDRESS)).then(redirectAnwser.getRedirectedUserAnswer());

        // Register User
        long messageId = IDGenerator.getRandomID();
        userActor.tell(new RegisterMessage(messageId, USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RegisterResultMessage registrationResultMessage = MessageUtil.getMessageByClass(RegisterResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, registrationResultMessage, delegateResultMessage);

        // Login User
        messageId = IDGenerator.getRandomID();
        userActor.tell(new LoginMessage(messageId, USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        firstResultMessage = probe.expectMsgClass(IMessage.class);
        secondResultMessage = probe.expectMsgClass(IMessage.class);

        LoginResultMessage loginResultMessage = MessageUtil.getMessageByClass(LoginResultMessage.class, firstResultMessage, secondResultMessage);
        delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, loginResultMessage, delegateResultMessage);
    }

    // --------------- Household section ---------------

    @Test
    public void testSuccessfulGetEmptyHouseholds() throws DataAccessException {
        Mockito.when(userDao.get(ArgumentMatchers.anyLong())).thenReturn(new User(USER_ID, EMAIL_ADDRESS, USER_NAME, PASSWORD));

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new GetHouseholdsMessage(messageId, USER_ID), probe.ref());

        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetHouseholdsResultMessage getHouseholdsResultMessage = MessageUtil.getMessageByClass(GetHouseholdsResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getHouseholdsResultMessage, delegateResultMessage);

        Assert.assertTrue(getHouseholdsResultMessage.getHouseholdIdsWithName().isEmpty());
    }

    @Test
    public void testGetHouseholdsWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(userDao).get(ArgumentMatchers.anyLong());

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new GetHouseholdsMessage(messageId, USER_ID), probe.ref());

        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetHouseholdsResultMessage getHouseholdsResultMessage = MessageUtil.getMessageByClass(GetHouseholdsResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Getting households for user id {0} failed: {1}", USER_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, getHouseholdsResultMessage, delegateMessage);
        Assert.assertTrue(getHouseholdsResultMessage.getHouseholdIdsWithName().isEmpty());
    }

    @Test
    public void testSuccessfulAddHousehold() throws DataAccessException {
        Mockito.when(userDao.get(ArgumentMatchers.anyLong())).thenReturn(new User(USER_ID, EMAIL_ADDRESS, USER_NAME, PASSWORD));
        Mockito.when(householdDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(foodInventoryDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(shoppingListDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(ingredientsPoolDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new AddHouseholdMessage(messageId, USER_ID, HOUSEHOLD_NAME), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddHouseholdResultMessage addHouseholdsResultMessage = MessageUtil.getMessageByClass(AddHouseholdResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, addHouseholdsResultMessage, delegateResultMessage);
    }

    @Test
    public void testAddHouseholdWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(userDao).get(ArgumentMatchers.anyLong());

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new AddHouseholdMessage(messageId, USER_ID, HOUSEHOLD_NAME), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddHouseholdResultMessage addHouseholdsResultMessage = MessageUtil.getMessageByClass(AddHouseholdResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Getting households for user with id {0} failed: {1}", USER_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, addHouseholdsResultMessage, delegateMessage);
    }

    @Test
    public void testSuccessfulRemoveHousehold() throws DataAccessException {
        User user = new User(USER_ID, EMAIL_ADDRESS, USER_NAME, PASSWORD);
        Household household = new Household(HOUSEHOLD_ID, new FoodInventory(0), new ShoppingList(0), new IngredientsPool(0), new RecipesPool(0), HOUSEHOLD_NAME);
        user.addHousehold(household);
        Mockito.when(userDao.get(USER_ID)).thenReturn(user);
        Mockito.when(householdDao.get(HOUSEHOLD_ID)).thenReturn(household);
        Mockito.when(userDao.isHouseholdUnreferenced(household)).thenReturn(true);

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new RemoveHouseholdMessage(messageId, USER_ID, HOUSEHOLD_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RemoveHouseholdResultMessage removeHouseholdsResultMessage = MessageUtil.getMessageByClass(RemoveHouseholdResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, removeHouseholdsResultMessage, delegateResultMessage);
    }

    @Test
    public void testSuccessfulRemoveReferencedHousehold() throws DataAccessException {
        User user = new User(USER_ID, EMAIL_ADDRESS, USER_NAME, PASSWORD);
        Household household = new Household(HOUSEHOLD_ID, new FoodInventory(0), new ShoppingList(0), new IngredientsPool(0), new RecipesPool(0), HOUSEHOLD_NAME);
        user.addHousehold(household);
        Mockito.when(userDao.get(USER_ID)).thenReturn(user);
        Mockito.when(householdDao.get(HOUSEHOLD_ID)).thenReturn(household);
        Mockito.when(userDao.isHouseholdUnreferenced(household)).thenReturn(false);

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new RemoveHouseholdMessage(messageId, USER_ID, HOUSEHOLD_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RemoveHouseholdResultMessage removeHouseholdsResultMessage = MessageUtil.getMessageByClass(RemoveHouseholdResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, removeHouseholdsResultMessage, delegateResultMessage);
    }

    @Test
    public void testRemoveHouseholdWithDataAccessException() throws DataAccessException {
        Mockito.doThrow(new DataAccessException(MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE)).when(userDao).get(ArgumentMatchers.anyLong());

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new RemoveHouseholdMessage(messageId, USER_ID, HOUSEHOLD_ID), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        RemoveHouseholdResultMessage addHouseholdsResultMessage = MessageUtil.getMessageByClass(RemoveHouseholdResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        String expectedErrorMessage = MessageFormat.format("Deleting household with id {0} for user with id {1} failed: {2}", HOUSEHOLD_ID, USER_ID, MessageUtil.CORRUPTED_DATA_SOURCE_MESSAGE);
        MessageUtil.checkForErrorMessage(messageId, expectedErrorMessage, addHouseholdsResultMessage, delegateMessage);
    }

    @Test
    public void testSuccessfulGetHouseholdsAfterAddHousehold() throws DataAccessException {
        User user = new User(USER_ID, EMAIL_ADDRESS, USER_NAME, PASSWORD);
        Mockito.when(userDao.get(ArgumentMatchers.anyLong())).thenReturn(user);
        Mockito.when(householdDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(foodInventoryDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(shoppingListDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(ingredientsPoolDao.doesIdExist(ArgumentMatchers.anyLong())).thenReturn(false);

        long messageId = IDGenerator.getRandomID();
        userActor.tell(new AddHouseholdMessage(messageId, USER_ID, HOUSEHOLD_NAME), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        AddHouseholdResultMessage addHouseholdsResultMessage = MessageUtil.getMessageByClass(AddHouseholdResultMessage.class, firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, addHouseholdsResultMessage, delegateResultMessage);

        messageId = IDGenerator.getRandomID();
        userActor.tell(new GetHouseholdsMessage(messageId, USER_ID), probe.ref());
        firstResultMessage = probe.expectMsgClass(IMessage.class);
        secondResultMessage = probe.expectMsgClass(IMessage.class);

        GetHouseholdsResultMessage getHouseholdsResultMessage = MessageUtil.getMessageByClass(GetHouseholdsResultMessage.class, firstResultMessage, secondResultMessage);
        delegateResultMessage = MessageUtil.getMessageByClass(DelegateMessage.class, firstResultMessage, secondResultMessage);

        MessageUtil.checkNoErrorMessage(messageId, getHouseholdsResultMessage, delegateResultMessage);

        Map<Long, String> households = getHouseholdsResultMessage.getHouseholdIdsWithName();
        Assert.assertEquals(1, households.size());
        Assert.assertTrue(households.containsValue(HOUSEHOLD_NAME));
    }
}
