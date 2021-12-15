package socialnetwork.repository.database.friendship;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.exceptions.FileException;
import socialnetwork.domain.exceptions.IdException;
import socialnetwork.domain.validator.FriendshipValidator;
import socialnetwork.repository.database.RepositoryDB;
import socialnetwork.repository.database.UserRepositoryDB;

import java.util.UUID;

/**
 * Abstract repository of friendships.
 * @param <E> the class (kind) of friendship.
 */
public abstract class AbstractFriendshipRepositoryDB<E extends Friendship> extends RepositoryDB<E> {
    protected final UserRepositoryDB users;

    public AbstractFriendshipRepositoryDB(String url, String username, String password) {
        super(url, username, password);
        this.validator = new FriendshipValidator<>();
        users = new UserRepositoryDB(url, username, password);
    }

    @Override
    public E findOne(UUID uuid) throws IdException, FileException, Exception {
        String sql = "select * from friendships where id = ?";
        return super.findOne(sql, uuid.toString());
    }

    /**
     * Finds all the friendships of an user, given by id;
     * @param id the given id;
     * @return an Iterable containing the friendships that user has.
     * @throws FileException if the file is not valid.
     */
    public Iterable<E> findByFriend(UUID id) throws FileException, Exception {
        String sql = "select * from friendships where friend_1 = ? or friend_2 = ?";
        return super.findAll(sql, new String[]{id.toString(), id.toString()});
    }

    @Override
    public Iterable<E> findAll() throws IdException, FileException, Exception {
        String sql = "select * from friendships";
        return super.findAll(sql, new String[]{});
    }

    /**
     * Returns the repository of users;
     * @return the repository of users.
     */
    public UserRepositoryDB getUsers() {
        return users;
    }

    @Override
    protected E save(E friendship, String sql, String[] attributes) throws IdException, FileException, Exception {
        if (!users.contains(friendship.getLeft()) || !users.contains(friendship.getRight())) return null;
        return super.save(friendship, sql, attributes);
    }

    @Override
    public E delete(UUID uuid) throws FileException, Exception {
        String sql = "delete from friendships where id = ?";
        String[] attributes = new String[] {uuid.toString()};
        return super.delete(uuid, sql, attributes);
    }

    @Override
    public E update(E friendship, String sql, String[] attributes) throws IdException, FileException, Exception {
        if (!users.contains(friendship.getLeft()) || !users.contains(friendship.getRight())) return null;
        return super.update(friendship, sql, attributes);
    }
}
