package socialnetwork.domain;

import socialnetwork.domain.constants.Constants.Status;

import java.time.LocalDateTime;
import java.util.Objects;

import static socialnetwork.domain.constants.Constants.DATEFORMATTER;

/**
 * Extension of friendship that has a status and a starting date.
 */
public class FriendshipWithStatus extends Friendship {
    private LocalDateTime date;
    private Status status;

    public FriendshipWithStatus(User user1, User user2) {
        super(user1, user2);
        this.status = Status.PENDING;
        this.date = LocalDateTime.now();
    }

    public FriendshipWithStatus(User user1, User user2, Status status, LocalDateTime date) {
        super(user1, user2);
        this.status = status;
        this.date = date;
    }

    /**
     * Returns the date of the friendship;
     * @return the date of the friendship.
     */
    public LocalDateTime getDate() { return date; }

    /**
     * Sets the date of the friendship from a given date;
     * @param date the given date.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Returns the status of the friendship;
     * @return the status of the friendship.
     */
    public Status status() { return status; }

    /**
     * Accepts a friendship.
     */
    public void accept() {
        this.status = Status.ACCEPTED;
        this.date = LocalDateTime.now();
    }

    /**
     * Declines a friendship.
     */
    public void decline() {
        this.status = Status.DECLINED;
    }

    @Override
    public String toString() {
        return super.toString() + " , " + status.getValue() + " , " + date.format(DATEFORMATTER);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass())
            return false;
        FriendshipWithStatus aux = new FriendshipWithStatus(((FriendshipWithStatus) obj).getRight(), ((FriendshipWithStatus) obj).getLeft());
        return super.equals(obj) || super.equals(aux);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date, status);
    }
}
