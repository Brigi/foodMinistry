package org.food.ministry.data.access.users;

import org.food.ministry.data.access.users.InMemoryUserDAO;
import org.food.ministry.data.access.users.UserDAO;

public class TestInMemoryUserDAO extends TestUserDAO {

    @Override
    protected UserDAO getUserDao() {
        return new InMemoryUserDAO();
    }

}
