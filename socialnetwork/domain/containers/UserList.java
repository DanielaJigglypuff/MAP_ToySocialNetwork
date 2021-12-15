package socialnetwork.domain.containers;

import socialnetwork.domain.User;

import java.util.ArrayList;

/**
 * List of users.
 */
public class UserList extends ArrayList<User> {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < size(); i++) {
            sb.append(get(i).getEmail());
            if (i + 1 != size())
                sb.append("; ");
        }
        return sb.toString();
    }
}
