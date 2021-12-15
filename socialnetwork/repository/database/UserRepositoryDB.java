package socialnetwork.repository.database;

import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.FileException;
import socialnetwork.domain.exceptions.IdException;
import socialnetwork.domain.validator.UserValidator;

import java.sql.ResultSet;
import java.util.Map;
import java.util.UUID;

import static socialnetwork.domain.constants.Constants.*;

/**
 * Repository of users.
 */
public class UserRepositoryDB extends RepositoryDB<User> {

    public UserRepositoryDB(String url, String username, String password) {
        super(url, username, password);
        this.validator = new UserValidator();
    }

    @Override
    public User findOne(UUID uuid) throws IdException, FileException, Exception {
        String sql = "select * from users where id = ?";
        return super.findOne(sql, uuid.toString());
    }

    /**
     * Finds user by email;
     * @param email the given email;
     * @return the user;
     * @throws IdException if the email is null;
     * @throws FileException if the file is not valid.
     */
    public User findByEmail(String email) throws IdException, FileException, Exception {
        String sql = "select * from users where email = ?";
        return super.findOne(sql, email);
    }

    @Override
    public Iterable<User> findAll() throws IdException, FileException, Exception {
        String sql = "select * from users";
        return super.findAll(sql, new String[]{});
    }

    @Override
    public User save(User user) throws IdException, FileException, Exception {
        String sql = "insert into users (id, email, first_name, last_name) values (?, ?, ?, ?)";
        String[] attributes = new String[] {user.getId().toString(), user.getEmail(),
                user.getFirstName(), user.getLastName()};
        return super.save(user, sql, attributes);
    }

    @Override
    public User delete(UUID uuid) throws IdException, FileException, Exception {
        String sql = "delete from users where id = ?";
        String[] attributes = new String[] {uuid.toString()};
        return super.delete(uuid, sql, attributes);
    }

    @Override
    public User update(User user) throws IdException, FileException, Exception {
        String sql = "update users set email = ?, first_name = ?, last_name = ? where id = ?";
        String[] attributes = new String[] {user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getId().toString()};
        return super.update(user, sql, attributes);
    }

    @Override
    protected User getFromDB(ResultSet resultSet) throws FileException, Exception {
        Map<String, String> fromDB = RepositoryDB.getStringDB(resultSet, new String[]{ID, EMAIL, FNAME, LNAME});
        User result = new User(fromDB.get(EMAIL), fromDB.get(FNAME), fromDB.get(LNAME));
        result.setId(UUID.fromString(fromDB.get(ID)));
        return result;
    }
}
