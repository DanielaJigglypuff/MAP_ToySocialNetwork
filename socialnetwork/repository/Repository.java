package socialnetwork.repository;

import socialnetwork.domain.exceptions.FileException;
import socialnetwork.domain.exceptions.ValidationException;
import socialnetwork.domain.primary.Entity;

/**
 * CRUD operations repository interface;
 *
 * @param <ID> - type E must have an attribute of type ID;
 * @param <E>  - type of entities saved in repository.
 */
public interface Repository<ID, E extends Entity<ID>> {
    /**
     * Finds the entity with the given id;
     *
     * @param id - ID;
     *           - the given id;
     *           - id must not be null;
     * @return an encapsulating the entity with the given id;
     * @throws IllegalArgumentException if id is null.
     */
    E findOne(ID id) throws FileException, Exception;

    /**
     * @return all entities.
     */
    Iterable<E> findAll() throws FileException, Exception;

    /**
     * Saves the entity;
     *
     * @param entity - E;
     *               - the given entity;
     *               - entity must not be null;
     * @return - null if the entity was saved;
     * - the entity (id already exists);
     * @throws IllegalArgumentException if the given entity is null;
     * @throws ValidationException      if the entity is not valid.
     */
    E save(E entity) throws FileException, Exception;

    /**
     * Removes the entity with the given id;
     *
     * @param id - ID;
     *           - the given id;
     *           - id must not be null;
     * @return - null if there is no entity with the given id;
     * - otherwise, the removed entity;
     * @throws IllegalArgumentException if the given id is null.
     */
    E delete(ID id) throws FileException, Exception;

    /**
     * Updates an entity with another;
     *
     * @param entity the entity to update;
     * @return the new entity;
     * @throws IllegalArgumentException if the entity is null;
     * @throws ValidationException      if the entity is not valid;
     */
    E update(E entity) throws FileException, Exception;
}
