package socialnetwork.domain.containers;

import socialnetwork.domain.User;
import socialnetwork.domain.primary.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The group of a message.
 */
public class GroupMessage extends Entity<UUID> {
    private final List<User> members;

    public GroupMessage(List<User> members) {
        setId(UUID.randomUUID());
        this.members = List.copyOf(members);
    }

    public List<User> everyoneBut(User user) {
        List<User> result = new ArrayList<>(List.copyOf(members));
        result.remove(user);
        return result;
    }

    public List<User> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return members.stream().map(user -> user.getId().toString()).collect(Collectors.joining(","));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMessage that = (GroupMessage) o;
        return that.members.containsAll(this.members) && this.members.containsAll(that.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), members);
    }
}
