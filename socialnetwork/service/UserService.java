package socialnetwork.service;

import socialnetwork.domain.FriendshipWithStatus;
import socialnetwork.domain.ReplyMessage;
import socialnetwork.domain.User;
import socialnetwork.domain.constants.Constants;
import socialnetwork.domain.containers.FriendshipList;
import socialnetwork.domain.containers.GroupMessage;
import socialnetwork.domain.exceptions.RepositoryException;
import socialnetwork.domain.util.SpecificList;
import socialnetwork.domain.util.SpecificOperation;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static socialnetwork.domain.constants.Constants.DATEFORMATTER;

/**
 * User service.
 */
public class UserService extends AbstractService {
    private final User user;

    public UserService(AbstractService service, User user) {
        super(service);
        this.user = user;
    }

    @Override
    public void sendRequest(String email) throws Exception {
        User req_user = users.findByEmail(email);
        if (req_user == null) throw new RepositoryException("no user found");
        FriendshipWithStatus friendship = friendships.request(new FriendshipWithStatus(this.user, req_user));
        if (friendship == null) throw new RepositoryException("the friend request or friendship already exists");
    }

    /**
     * Does an operation related to an user given by email, on a specific list;
     * @param list the specific list;
     * @param operation the specific operation;
     * @param email the given email;
     * @param exception the exception the operation can throw;
     */
    private void doOperation(SpecificList list, SpecificOperation operation, String email,
                             Exception exception) throws Exception {
        User req_user = users.findByEmail(email);
        if (req_user == null) throw new RepositoryException("no user found");
        FriendshipWithStatus friendship = new FriendshipWithStatus(req_user, this.user);
        for (FriendshipWithStatus request : list.process())
            if (request.equals(friendship)) {
                operation.process(request);
                return;
            }
        throw exception;
    }

    @Override
    public void acceptRequest(String email) throws Exception {
        doOperation(this::pendingFriendships,this.friendships::accept,
                email, new RepositoryException("the friend request does not exist"));
    }

    @Override
    public void declineRequest(String email) throws Exception {
        doOperation(this::pendingFriendships,this.friendships::decline,
                email, new RepositoryException("the friend request does not exist"));
    }

    @Override
    public void deleteFriend(String email) throws Exception {
        doOperation(this::acceptedFriendships,this.friendships::decline,
                email, new RepositoryException("the friendship does not exist"));
    }

    @Override
    public FriendshipList acceptedFriendships() throws Exception {
        return StreamSupport.stream(this.friendships.findByFriend(this.user.getId()).spliterator(), false)
                .filter(friendship -> friendship.getRight().equals(this.user) || friendship.getLeft().equals(this.user))
                .filter(friendship -> friendship.status() == Constants.Status.ACCEPTED)
                .collect(Collectors.toCollection(FriendshipList::new));
    }

    @Override
    public FriendshipList pendingFriendships() throws Exception {
        return StreamSupport.stream(this.friendships.findByFriend(this.user.getId()).spliterator(), false)
                .filter(friendship -> friendship.getRight().equals(this.user) || friendship.getLeft().equals(this.user))
                .filter(friendship -> friendship.status() == Constants.Status.PENDING)
                .collect(Collectors.toCollection(FriendshipList::new));
    }

    @Override
    protected void showList(SpecificList list) throws Exception {
        list.process().forEach(friendship -> System.out.println("Friendship: " + friendship.
                theOtherFriend(this.user).getLastName() + " | " + friendship.theOtherFriend(this.user).
                getFirstName() + " | " + friendship.getDate().toLocalDate().format(DATEFORMATTER)));
    }

    @Override
    public void sendMessage(String emails_to, String text, String reply_uuid) throws Exception {
        GroupMessage to_group = new GroupMessage(Stream.concat(groups.parseByEmail(emails_to).stream(),
                Stream.of(user)).collect(Collectors.toList()));
        for (GroupMessage i : groups.findAll())
            if (to_group.equals(i)) {
                to_group.setId(i.getId());
            }
        groups.save(to_group);
        ReplyMessage message;
        if (reply_uuid.equals("-")) message = new ReplyMessage(user, to_group, text, LocalDateTime.now());
        else message = new ReplyMessage(messages.findOne(UUID.fromString(reply_uuid)), user, to_group, text, LocalDateTime.now());
        messages.save(message);
    }

    @Override
    public List<ReplyMessage> messagesWith(String email) throws Exception {
        return StreamSupport.stream(this.messages.findAll().spliterator(), false)
                .filter(message -> {
                    try {
                        return message.getGroup().getMembers().contains(user) &&
                                message.getGroup().getMembers().contains(this.users.findByEmail(email));
                    } catch (Exception ignored) {} return false;})
                .filter(message -> {
                    try {
                        return message.getFrom().equals(user) || message.getFrom().equals(this.users.findByEmail(email));
                    } catch (Exception ignored) {} return false;})
                .sorted(Collections.reverseOrder((m1, m2) -> m2.getDate().compareTo(m1.getDate())))
                .collect(Collectors.toList());
    }
}
