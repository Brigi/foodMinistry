package org.food.ministry.data.access;

import java.text.MessageFormat;

import org.food.ministry.data.access.exceptions.DataAccessException;
import org.food.ministry.model.PersistenceObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public abstract class TestBaseDAO<T extends PersistenceObject> {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private T testPersistenceObject;
    private DAO<T> testDao;
    
    protected abstract T getPersistenceObject();
    protected abstract DAO<T> getDao();

    @Before
    public void startUp() {
        testPersistenceObject = getPersistenceObject();
        testDao = getDao();
    }
    
    @Test
    public void testSaveItem() throws DataAccessException {
        testDao.save(testPersistenceObject);
    }
    
    @Test
    public void testSaveItemTwice() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(DAO.ITEM_ALREADY_EXISTS_MESSAGE, getPersistenceObject().getId()));
        testDao.save(testPersistenceObject);
        testDao.save(testPersistenceObject);
    }

    @Test
    public void testGet() throws DataAccessException {
        testDao.save(testPersistenceObject);
        Assert.assertEquals(testPersistenceObject, testDao.get(testPersistenceObject.getId()));
    }
    
    @Test
    public void testDeleteItem() throws DataAccessException {
        testDao.save(testPersistenceObject);
        testDao.delete(testPersistenceObject);
        Assert.assertTrue(testDao.getAll().isEmpty());
    }

    @Test
    public void testDoesIdExistsPositive() throws DataAccessException {
        testDao.save(testPersistenceObject);
        Assert.assertTrue(testDao.doesIdExist(0));
    }

    @Test
    public void testDoesIdExistsNegative() throws DataAccessException {
        testDao.save(testPersistenceObject);
        Assert.assertFalse(testDao.doesIdExist(1));
    }

    @Test
    public void testGetNonExistingID() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(DAO.NO_ID_FOUND_MESSAGE, 0));
        testDao.get(0);
    }

    @Test
    public void testUpdateNonExistingItem() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(DAO.INSUFFICIENT_AMOUNT_MESSAGE, 0));
        testDao.update(testPersistenceObject);
    }

    @Test
    public void testDeleteNonExistingItem() throws DataAccessException {
        expectedException.expect(DataAccessException.class);
        expectedException.expectMessage(MessageFormat.format(DAO.NO_ID_FOUND_MESSAGE, testPersistenceObject.getId()));
        testDao.delete(testPersistenceObject);
    }
}
