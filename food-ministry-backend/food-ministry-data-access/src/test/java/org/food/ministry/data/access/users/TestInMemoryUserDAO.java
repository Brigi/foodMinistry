package org.food.ministry.data.access.users;

public class TestInMemoryUserDAO extends TestUserDAO {

    @Override
    protected UserDAO getUserDao() {
        return new InMemoryUserDAO();
    }
}
