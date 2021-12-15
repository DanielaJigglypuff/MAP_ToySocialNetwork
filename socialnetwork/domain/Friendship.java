package socialnetwork.domain;

import socialnetwork.domain.primary.Pair;

public class Friendship extends Pair<User, User> {
    public Friendship(User user1, User user2) {
        super(user1, user2);
    }

    /**
     * Verifies if an user is in a friendship;
     * @param user the user;
     * @return the value of truth.
     */
    public boolean isFriend(User user) {
        return ((getLeft().equals(user)) || (getRight().equals(user)));
    }

    /**
     * Return the friend of an user in a friendship;
     * @param user the user
     * @return the other friend; null if the given user is not in friendship.
     */
    public User theOtherFriend(User user) {
        if (getLeft().equals(user))
            return getRight();
        if (getRight().equals(user))
            return getLeft();
        return null;
    }

    @Override
    public String toString() {
        return "Friendship: " + getLeft().getEmail() + " , " + getRight().getEmail();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass())
            return false;
        Friendship aux = new Friendship(((Friendship) obj).getRight(), ((Friendship) obj).getLeft());
        return super.equals(obj) || super.equals(aux);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
