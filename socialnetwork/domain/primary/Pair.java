package socialnetwork.domain.primary;

import java.util.Objects;
import java.util.UUID;

/**
 * Pair of different objects, entity with id;
 * @param <E1> class of first object;
 * @param <E2> class of the second object.
 */
public abstract class Pair<E1, E2> extends Entity<UUID> {
    private E1 e1;
    private E2 e2;

    /**
     * Constructor with parameters;
     * @param e1 first entity of the pair;
     * @param e2 second entity of the pair.
     */
    public Pair(E1 e1, E2 e2) {
        this.setId(UUID.randomUUID());
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * Returns the first entity;
     * @return the first entity of the pair.
     */
    public E1 getLeft() {
        return e1;
    }

    /**
     * Sets the first entity using a given entity;
     * @param e1 the given entity.
     */
    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    /**
     * Returns the second entity;
     * @return the second entity of the pair.
     */
    public E2 getRight() {
        return e2;
    }

    /**
     * Sets the second entity using a given entity;
     * @param e2 the given entity.
     */
    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    @Override
    public String toString() {
        return e1 + "," + e2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pair<?,?> entity = (Pair<?,?>) obj;
        return this.e1.equals(entity.e1) && this.e2.equals(entity.e2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}
