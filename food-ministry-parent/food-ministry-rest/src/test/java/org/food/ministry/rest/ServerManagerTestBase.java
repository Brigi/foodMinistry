package org.food.ministry.rest;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import org.food.ministry.rest.util.ServerUtil;
import org.junit.After;
import org.junit.Before;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ServerManagerTestBase {

    @Before
    public void setup() {
        try {
            ServerUtil.startServer();
        } catch(InterruptedException e) {
            fail("Failed to start server due to interruption");
        }
    }
    
    @After
    public void teardown() {
        try {
            ServerUtil.stopServer();
        } catch(UnsupportedEncodingException | JsonProcessingException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            fail(MessageFormat.format("Failed to stop server: {}", e.getMessage()));
        }
    }
}
