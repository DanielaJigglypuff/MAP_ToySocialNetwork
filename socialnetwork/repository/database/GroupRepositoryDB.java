package socialnetwork.repository.database;

import socialnetwork.domain.User;
import socialnetwork.domain.containers.GroupMessage;
import socialnetwork.domain.exceptions.FileException;
import socialnetwork.domain.validator.GroupMessageValidator;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static socialnetwork.domain.constants.Constants.GROUPS;
import static socialnetwork.domain.constants.Constants.ID;

public class GroupRepositoryDB extends RepositoryDB<GroupMessage> {
    private final UserRepositoryDB users;

    public GroupRepositoryDB(String url, String username, String password) {
        super(url, username, password);
        this.validator = new GroupMessageValidator();
        users = new UserRepositoryDB(url, username, password);
    }

    @Override
    public GroupMessage findOne(UUID uuid) throws FileException, Exception {
        String sql = "select * from groups where id = ?";
        return super.findOne(sql, uuid.toString());
    }

    @Override
    public Iterable<GroupMessage> findAll() throws FileException, Exception {
        String sql = "select * from groups";
        return super.findAll(sql, new String[]{});
    }

    @Override
    public GroupMessage save(GroupMessage group) throws FileException, Exception {
        if (!users.contains(group.getMembers())) return null;
        String sql = "insert into groups (id, users_group) values (?, ?)";
        String[] attributes = new String[] {group.getId().toString(), group.toString()};
        return super.save(group, sql, attributes);
    }

    @Override
    public GroupMessage delete(UUID uuid) throws FileException, Exception {
        String sql = "delete from groups where id = ?";
        String[] attributes = new String[] {uuid.toString()};
        return super.delete(uuid, sql, attributes);
    }

    @Override
    public GroupMessage update(GroupMessage entity) throws FileException, Exception {
        if (!users.contains(entity.getMembers())) return null;
        String sql = "update groups set users = ? where id = ?";
        String[] attributes = new String[]{entity.toString(), entity.getId().toString()};
        return super.update(entity, sql, attributes);
    }

    public List<User> parse(String string, Function<String, User> func) {
        return Arrays.stream(string.split(",")).map(func).collect(Collectors.toList());
    }

    public List<User> parseByUUID(String string) throws Exception {
        return parse(string, v -> {
            try {
                return users.findOne(UUID.fromString(v));
            } catch (Exception e) {
                return null;
            }
        });
    }

    public List<User> parseByEmail(String string) throws Exception {
        return parse(string, v -> {
            try {
                return users.findByEmail(v);
            } catch (Exception e) {
                return null;
            }
        });
    }

    @Override
    protected GroupMessage getFromDB(ResultSet resultSet) throws FileException, Exception {
        Map<String, String> fromDB = RepositoryDB.getStringDB(resultSet, new String[]{ID, GROUPS});
        GroupMessage result = new GroupMessage(parseByUUID(fromDB.get(GROUPS)));
        result.setId(UUID.fromString(fromDB.get(ID)));
        return result;
    }
}
