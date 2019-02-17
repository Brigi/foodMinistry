package org.food.ministry.data.access;

import org.food.ministry.data.access.users.InMemoryUserDAO;
import org.food.ministry.data.access.users.UserDAO;

public class TestInMemoryUserDAO extends TestBaseUserDAO {

    @Override
    protected UserDAO getUserDao() {
        return new InMemoryUserDAO();
    }

}
