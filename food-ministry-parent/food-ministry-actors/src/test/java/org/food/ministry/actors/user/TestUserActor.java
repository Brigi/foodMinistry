package org.food.ministry.actors.user;

import org.food.ministry.actors.messages.IMessage;
import org.food.ministry.actors.user.messages.DelegateMessage;
import org.food.ministry.actors.user.messages.LoginMessage;
import org.food.ministry.actors.user.messages.LoginResultMessage;
import org.food.ministry.actors.util.Constants;
import org.food.ministry.actors.util.IDGenerator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestProbe;

public class TestUserActor {

    private static ActorSystem system;
    
    @BeforeClass
    public static void before() {
        system = ActorSystem.create("user-system");
        IDGenerator.initializeGeneratorActor(system);
    }
    
    @AfterClass
    public static void teardown() {
        system = null;
    }
    
    @Test
    public void testUserLoginActor() {
      TestProbe probe = new TestProbe(system);
      ActorRef userActor = system.actorOf(UserActor.props(), "user-actor");
      
      userActor.tell(new LoginMessage(IDGenerator.getUniqueID(), "MyName", "email@address.com", "1234"), probe.ref());
      IMessage firstResultMessage = probe.expectMsgClass(IMessage.class);
      IMessage secondResultMessage = probe.expectMsgClass(IMessage.class);
      
      LoginResultMessage loginResultMessage = getLoginResultMessage(firstResultMessage, secondResultMessage);
      DelegateMessage delegateResultMessage = getDelegateMessage(firstResultMessage, secondResultMessage);
      
      Assert.assertEquals(Constants.NO_ERROR_MESSAGE, loginResultMessage.getErrorMessage());
      Assert.assertEquals(loginResultMessage.getOriginId(), delegateResultMessage.getOriginId());
      Assert.assertTrue(delegateResultMessage.getOriginId() > 0);
    }
    
    private LoginResultMessage getLoginResultMessage(IMessage firstResultMessage, IMessage secondResultMessage) {
        if(firstResultMessage instanceof LoginResultMessage) {
            return (LoginResultMessage) firstResultMessage;
        } else if(secondResultMessage instanceof LoginResultMessage) {
            return (LoginResultMessage) secondResultMessage;
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
