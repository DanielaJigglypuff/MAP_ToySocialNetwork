package socialnetwork.repository.database.friendship;

import socialnetwork.domain.FriendshipWithStatus;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.FileException;
import socialnetwork.repository.database.RepositoryDB;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static socialnetwork.domain.constants.Constants.*;

/**
 * Repository of friendship with status and date.
 */
public class FriendshipStatusRepositoryDB extends AbstractFriendshipRepositoryDB<FriendshipWithStatus> {
    public FriendshipStatusRepositoryDB(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public FriendshipWithStatus save(FriendshipWithStatus friendship) throws FileException, Exception {
        String sql = "insert into friendships (id, friend_1, friend_2, status, date) values (?, ?, ?, ?, ?)";
        String[] attributes = new String[] {friendship.getId().toString(), friendship.getLeft().getId().toString(),
                friendship.getRight().getId().toString(), friendship.status().getValue(), friendship.getDate().format(DATEFORMATTER)};
        return super.save(friendship, sql, attributes);
    }

    @Override
    public FriendshipWithStatus update(FriendshipWithStatus friendship) throws FileException, Exception {
        String sql = "update friendships set friend_1 = ?, friend_2 = ?, status = ?, date = ? where id = ?";
        String[] attributes = new String[] {friendship.getLeft().getId().toString(), friendship.getRight().getId().toString(),
                friendship.status().getValue(), friendship.getDate().format(DATEFORMATTER), friendship.getId().toString()};
        return super.update(friendship, sql, attributes);
    }

    @Override
    protected FriendshipWithStatus getFromDB(ResultSet resultSet) throws FileException, Exception {
        Map<String, String> fromDB = RepositoryDB.getStringDB(resultSet, new String[]{ID, FRIEND1, FRIEND2, STATUS, FRIENDDATE});
        User user1 = users.findOne(UUID.fromString(fromDB.get(FRIEND1)));
        User user2 = users.findOne(UUID.fromString(fromDB.get(FRIEND2)));
        Status status = Status.fromString(fromDB.get(STATUS));
        LocalDateTime friendDate = LocalDateTime.parse(fromDB.get(FRIENDDATE),DATEFORMATTER);
        FriendshipWithStatus result = new FriendshipWithStatus(user1, user2, status, friendDate);
        result.setId(UUID.fromString(fromDB.get(ID)));
        return result;
    }

    /**
     * Accepts a friendship given by parameter;
     * @param friendship the given friendship;
     * @return the new friendship (updated);
     * @throws FileException if the file is not valid.
     */
    public FriendshipWithStatus accept(FriendshipWithStatus friendship) throws FileException, Exception {
        friendship.accept();
        return update(friendship);
    }

    /**
     * Creates a friendship given by parameter;
     * @param friendship the given friendship;
     * @return the new friendship;
     * @throws FileException if the file is not valid.
     */
    public FriendshipWithStatus request(FriendshipWithStatus friendship) throws FileException, Exception {
        return save(friendship);
    }

    /**
     * Declines a friendship given by parameter;
     * @param friendship the given friendship;
     * @return the deleted friendship;
     * @throws FileException if the file is not valid.
     */
    public FriendshipWithStatus decline(FriendshipWithStatus friendship) throws FileException, Exception {
        return delete(friendship.getId());
    }
}
