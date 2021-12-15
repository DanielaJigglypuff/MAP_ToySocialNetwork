package socialnetwork.repository.database;

import socialnetwork.domain.exceptions.FileException;
import socialnetwork.domain.exceptions.IdException;
import socialnetwork.domain.primary.Entity;
import socialnetwork.domain.validator.Validator;
import socialnetwork.repository.Repository;

import java.sql.*;
import java.util.*;

/**
 * Abstract repository of objects;
 * @param <E> the class of the object.
 */
public abstract class RepositoryDB<E extends Entity<UUID>> implements Repository<UUID, E> {
    private final String url;
    private final String username;
    private final String password;
    protected Validator<E> validator;

    public RepositoryDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Finds an entity by a value;
     * @param sql the sequence that gives the entity;
     * @param value the value;
     * @return the entity;
     * @throws IdException if the value is null;
     * @throws FileException if the file is not valid.
     */
    protected E findOne(String sql, String value) throws IdException, FileException, Exception {
        if (value == null) throw new IdException("value must not be null");
        try {
            ResultSet resultSet = this.executeQuery(sql, new String[] {value});
            if (resultSet.next()) {
                E entity = getFromDB(resultSet);
                validator.validate(entity);
                return entity;
            }
        } catch (SQLException e) {
            throw e;}
            //System.out.println(e); }
        return null;
    }

    /**
     * Finds all entities with specific values;
     * @param sql the sequence that gives the entities;
     * @param attributes the values;
     * @return the entities;
     * @throws FileException if the file is not valid.
     */
    protected Iterable<E> findAll(String sql, String[] attributes) throws FileException, Exception {
        Set<E> entities = new HashSet<>();
        try {
            ResultSet resultSet = this.executeQuery(sql, attributes);
            while (resultSet.next()) {
                E entity = getFromDB(resultSet);
                validator.validate(entity);
                entities.add(entity);
            }
        } catch (SQLException e) {
            throw e;}
            // System.out.println(e); }
        return entities;
    }

    /**
     * Saves a given entity in repository;
     * @param entity the given entity;
     * @param sql the sequence to save it;
     * @param attributes the values of the entity to delete;
     * @return the entity if it is saved, null otherwise;
     * @throws IdException if the entity is null;
     * @throws FileException if the file is not valid.
     */
    protected E save(E entity, String sql, String[] attributes) throws IdException, FileException, Exception {
        if (entity == null) throw new IdException("entity must not be null");
        if(this.contains(entity)) return null;
        validator.validate(entity);
        this.modifyDB(sql, attributes);
        return entity;
    }

    /**
     * Deletes an entity with a given id;
     * @param uuid the given id;
     * @param sql the sequence to delete the entity;
     * @param attributes the value of the id;
     * @return the entity if it is deleted, null otherwise;
     * @throws IdException if id is null;
     * @throws FileException if the file is not valid.
     */
    protected E delete(UUID uuid, String sql, String[] attributes) throws IdException, FileException, Exception {
        if (uuid == null) throw new IdException("id must not be null");
        E entity = findOne(uuid);
        if (entity == null) return null;
        this.modifyDB(sql, attributes);
        return entity;
    }

    /**
     * Updates the entity in the repository;
     * @param entity the updated entity;
     * @param sql the sequence to update the entity;
     * @param attributes the values of the entity;
     * @return the entity if it updated, null otherwise;
     * @throws IdException if id is null;
     * @throws FileException if the file is not valid.
     */
    protected E update(E entity, String sql, String[] attributes) throws IdException, FileException, Exception {
        if (entity == null) throw new IdException("entity must not be null");
        if (findOne(entity.getId()) == null) return null;
        validator.validate(entity);
        this.modifyDB(sql, attributes);
        return entity;
    }

    /**
     * Returns the entity from the db;
     * @param resultSet the resultSet;
     * @return the entity;
     * @throws FileException if the file is not valid.
     */
    protected abstract E getFromDB(ResultSet resultSet) throws FileException, Exception;

    /**
     * Checks if en entity is in the given repository;
     * @param entity the given entity;
     * @return true if the entity is in repository, false otherwise.
     * @throws FileException if the file is not valid.
     */
    public boolean contains(E entity) throws FileException, Exception {
        for (E e : this.findAll()) if (e.equals(entity)) return true;
        return false;
    }

    public boolean contains(List<E> entities) throws FileException, Exception {
        for (E entity: entities) {
            if(!contains(entity)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates the prepeared statement in a sequence;
     * @param sql the sequence;
     * @param attributes the values to be executed on;
     * @return the prepare statement;
     * @throws SQLException if the file is not valid;
     */
    private PreparedStatement execute(String sql, String[] attributes) throws SQLException, Exception {
        Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < attributes.length; i++) {
            statement.setString(i + 1, attributes[i]);
        }
        return statement;
    }

    /**
     * Executes a prepared statement created with a given statement, on specific values;
     * @param sql the given statement;
     * @param attributes the specific values;
     * @return the resultSet resulted;
     * @throws SQLException if the file is not valid.
     */
    private ResultSet executeQuery(String sql, String[] attributes) throws SQLException, Exception {
        return this.execute(sql, attributes).executeQuery();
    }

    /**
     * Modifies the db with a specific statement and specific attributes;
     * @param sql the specific statement;
     * @param attributes the specific attributes;
     * @throws FileException if the file is not valid.
     */
    private void modifyDB(String sql, String[] attributes) throws Exception, FileException {
        try { this.execute(sql, attributes).executeUpdate(); }
        catch (SQLException e) {
            throw new FileException("corrupted file");
//throw e;
        }
    }

    /**
     * Returns the dictionary that contains the values in the db having column name as key;
     * @param resultSet the resultSet for the statement;
     * @param columnsArray the names of the columns;
     * @return the dictionary that contains the values in the db having column name as key;
     * @throws FileException if the file is not valid.
     */
    protected static Map<String, String> getStringDB(ResultSet resultSet, String[] columnsArray) throws FileException, Exception {
        Map<String, String> result = new HashMap<>();
        try {
            for (String column : columnsArray)
                result.put(column, resultSet.getString(column));
        } catch (SQLException e) {
            throw e; }
            //System.out.println(e);}
        return result;
    }


}
