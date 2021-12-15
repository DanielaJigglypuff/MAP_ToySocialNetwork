package socialnetwork.domain.primary;

import java.util.Objects;

/**
 * Entity class for every future instance that needs an id;
 *
 * @param <ID> - the type of ID that Entity has.
 */
public abstract class Entity<ID> {
    private ID id;

    /**
     * Returns the id of the entity;
     * @return the id of the entity.
     */
    public ID getId() {
        return this.id;
    }

    /**
     * Sets the id of the entity using a given id;
     * @param id the given id.
     */
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
