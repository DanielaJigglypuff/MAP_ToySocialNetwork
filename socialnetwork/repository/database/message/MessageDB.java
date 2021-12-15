package socialnetwork.repository.database.message;

import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.containers.GroupMessage;
import socialnetwork.domain.containers.UserList;
import socialnetwork.domain.exceptions.FileException;
import socialnetwork.repository.database.RepositoryDB;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static socialnetwork.domain.constants.Constants.*;

public class MessageDB extends AbstractMessageDB<Message> {

    public RepositoryDB<User> userRepo;

    public MessageDB(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Message save(Message entity) throws FileException, Exception {
        String sql = "insert into messages (id, text_message, timestamp, id_from, id_group_to ) values (?, ?, ?, ?)";
        String[] attributes = new String[] {entity.getId().toString(),entity.getMessage(), entity.getDate().toString(),
                entity.getFrom().getId().toString(), entity.getTo().toString()};
        return super.save(entity, sql, attributes);
    }


    @Override
    public Message update(Message entity) throws FileException, Exception {
        String sql = "update messages set text_message = ?, timestamp = ?, id_from = ?, id_group_to = ? where id = ?";
        String[] attributes = new String[] {entity.getMessage(), entity.getDate().toString(),
                entity.getFrom().getId().toString(), entity.getTo().toString(),entity.getId().toString()};
        return super.update(entity, sql, attributes);
    }

    @Override
    protected Message getFromDB(ResultSet resultSet) throws FileException, Exception {
        Map<String, String> fromDB = RepositoryDB.getStringDB(resultSet, new String[]{ID, TEXTMSG, TIMESTAMP, FROM , TO});
        User from = userRepo.findOne(UUID.fromString(fromDB.get(FROM)));
        UserList list_users = new UserList();
        List<String> string_users = List.of(fromDB.get(TO).split(","));
        for (String str: string_users) {
            list_users.add(userRepo.findOne(UUID.fromString(str)));
        }
        String text_message = fromDB.get(TEXTMSG);
        LocalDateTime timestamp = LocalDateTime.parse(fromDB.get(TIMESTAMP),DATEFORMATTER);
        Message result = new Message(from, new GroupMessage(list_users),text_message,timestamp);
        result.setId(UUID.fromString(fromDB.get(ID)));
        return result;
    }
}