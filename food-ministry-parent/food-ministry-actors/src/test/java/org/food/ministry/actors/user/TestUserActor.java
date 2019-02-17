package org.food.ministry.actors.user;

import java.text.MessageFormat;

import org.food.ministry.actors.messages.IMessage;
import org.food.ministry.actors.user.messages.DelegateMessage;
import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.LoginResultMessage;
import org.food.ministry.actors.user.messages.RegisterMessage;
import org.food.ministry.actors.user.messages.RegisterResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.food.ministry.data.access.exceptions.DataAccessException;
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

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestProbe;

@RunWith(MockitoJUnitRunner.class)
public class TestUserActor {

    private static final String CORRUPTED_DATA_SOURCE_MESSAGE = "Underlying data source not responding correctly";
    private static final String EMAIL_ADDRESS = "email@address.com";
    private static final String USER_NAME = "MyName";
    private static final String PASSWORD = "1234";

    @Mock
    private UserDAO userDao;
    private ActorSystem system;
    private ActorRef userActor;
    private TestProbe probe;

    @Before
    public void startUp() throws DataAccessException {
        system = ActorSystem.create("user-system");
        probe = new TestProbe(system);
        IDGenerator.initializeGeneratorActor(system);
        userActor = system.actorOf(UserActor.props(userDao), "user-actor");
    }

    @After
    public void teardown() {
        system = null;
    }

    // --------------- User Login section ---------------

    @Test
    public void testSuccessfulUserLogin() throws DataAccessException {
        Mockito.when(userDao.getUser(EMAIL_ADDRESS)).thenReturn(new User(0, EMAIL_ADDRESS, USER_NAME, PASSWORD));
        userActor.tell(new LoginMessage(IDGenerator.getRandomID(), USER_NAME, EMAIL_ADDRESS, PASSWORD), probe.ref());
        IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
        IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);

        LoginResultMessage loginResultMessage = getLoginResultMessage(firstResultMessage, secondResultMessage);
        DelegateMessage delegateResultMessage = getDelegateMessage(firstResultMessage, secondResultMessage);

        Assert.assertTrue(loginResultMessage.isSuccessful());
        Assert.assertEquals(Constants.NO_ERROR_MESSAGE, loginResultMessage.getErrorMessage());
        Assert.assertEquals(loginResultMessage.getOriginId(), delegateResultMessage.getOriginId());
        Assert.assertTrue(delegateResultMessage.getOriginId() != 0);
    }

    @Test
    public void testUserLoginWithWrongPassword() throws DataAccessException {
        Mockito.when(userDao.getUser(EMAIL_ADDRESS)).thenReturn(new User(0, EMAIL_ADDRESS, USER_NAME, PASSWORD));
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

    private DelegateMessage getDelegateMessage(IMessage firstResultMessage, IMessage secondResultMessage) {
        if(firstResultMessage instanceof DelegateMessage) {
            return (DelegateMessage) firstResultMessage;
        } else if(secondResultMessage instanceof DelegateMessage) {
            return (DelegateMessage) secondResultMessage;
        }
        return null;
    }
}
