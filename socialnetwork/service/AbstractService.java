package socialnetwork.service;

import socialnetwork.domain.FriendshipWithStatus;
import socialnetwork.domain.ReplyMessage;
import socialnetwork.domain.User;
import socialnetwork.domain.containers.FriendshipList;
import socialnetwork.domain.exceptions.AccessException;
import socialnetwork.domain.exceptions.RepositoryException;
import socialnetwork.domain.util.SpecificList;
import socialnetwork.repository.database.GroupRepositoryDB;
import socialnetwork.repository.database.UserRepositoryDB;
import socialnetwork.repository.database.friendship.FriendshipStatusRepositoryDB;
import socialnetwork.repository.database.message.ReplyMessageDB;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static socialnetwork.domain.constants.Constants.Status.ACCEPTED;

/**
 * Abstract Service.
 */
public class AbstractService {
    protected final FriendshipStatusRepositoryDB friendships;
    protected final UserRepositoryDB users;
    protected final ReplyMessageDB messages;
    protected final GroupRepositoryDB groups;

    public AbstractService(String url, String username, String password) {
        this.friendships = new FriendshipStatusRepositoryDB(url, username, password);
        this.users = new UserRepositoryDB(url, username, password);
        this.messages = new ReplyMessageDB(url, username, password);
        this.groups = new GroupRepositoryDB(url, username, password);
    }

    public AbstractService(AbstractService service) {
        this.friendships = service.friendships;
        this.users = service.users;
        this.messages = service.messages;
        this.groups = service.groups;
    }

    /**
     * @return the friendships in the network.
     */
    public Iterable<FriendshipWithStatus> getFriendships() throws Exception {
        return this.friendships.findAll();
    }

    /**
     * @return the users in the network.
     */
    public Iterable<User> getUsers() throws Exception {
        return users.findAll();
    }

    /**
     * Finds the user with a given email;
     * @param email the given email;
     * @return the user with the given email.
     */
    public User findUserByEmail(String email) throws Exception {
        return users.findByEmail(email);
    }

    /**
     * Finds the user with a given id;
     * @param id the given id;
     * @return the user with the given id.
     */
    public User findUserById(UUID id) throws Exception{
        return users.findOne(id);
    }

    /**
     * Makes a list of people with the same first name
     * @param first_name the first name we are looking for
     * @return a list of people with the same first name
     * @throws Exception if first name doesn't exist
     */
    public List<User> findUsersByFirstName(String first_name) throws Exception {
        Iterable<User> iterableUsers = users.findAll();
        List<User> sameFirstName =  StreamSupport.stream(iterableUsers.spliterator(), false)
                .filter( user -> user.getFirstName().equals(first_name))
                .collect(Collectors.toList());
        return sameFirstName;
    }

    /**
     * Makes a list of people with the same last name
     * @param last_name the last name we are looking for
     * @return a list of people with the same last name
     */
    public List<User> findUsersByLastName(String last_name) throws Exception {
        Iterable<User> iterableUsers = users.findAll();
        List<User> sameLastName =  StreamSupport.stream(iterableUsers.spliterator(), false)
                .filter( user -> user.getLastName().equals(last_name))
                .collect(Collectors.toList());
        return sameLastName;
    }

    /**
     * Makes a string that contains the people with that particular email and month
     * @param user the user that we are looking for
     * @param month the month in which he made friends
     * @return a string with the name and date
     */
    public String getFriendshipByMonth(User user, int month) throws Exception {
        String sameMonthFriends =  StreamSupport.stream(friendships.findAll().spliterator(), false)
                .filter(friends -> friends.status().equals(ACCEPTED))
                .filter(friends -> (friends.getLeft().getEmail().equals(user.getEmail())||
                        friends.getRight().getEmail().equals(user.getEmail())))
                .filter(friends -> friends.getDate().getMonthValue() == month)
                .map( friends -> friends.theOtherFriend(user).getFirstName()+" | "+ friends.theOtherFriend(user).getLastName() +
                        " | "+friends.getDate().toString())
                .collect( Collectors.joining( "\n" ))
                .replace("T", " ");
        return sameMonthFriends;
    }

    public void register(String email, String firstName, String lastName) throws Exception {
        User user = users.save(new User(email, firstName, lastName));
        if (user == null) throw new RepositoryException("the email already exists");
    }

    /**
     * Adds user by the given attributes;
     * @param email given email;
     * @param firstName given first name;
     * @param lastName given last name;
     */
    public void addUser(String email, String firstName, String lastName) throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Deletes user by the given email;
     * @param email the given email;
     */
    public void deleteUser(String email) throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Updates user by the given attributes;
     * @param email given email;
     * @param firstName given first name;
     * @param lastName given last name;
     */
    public void updateUser(String email, String firstName, String lastName) throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Adds friendship by 2 given emails;
     * @param email1 the #1 given email;
     * @param email2 the #2 given email;
     */
    public void addFriendship(String email1, String email2) throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Deletes friendship by 2 given emails;
     * @param email1 the #1 given email;
     * @param email2 the #2 given email;
     */
    public void deleteFriendship(String email1, String email2) throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Updates friendship by 2 given emails;
     * @param email1 the #1 given email;
     * @param email2 the #2 given email;
     * @param email3 the #1 email to update;
     * @param email4 the #2 email to update;
     */
    public void updateFriendship(String email1, String email2, String email3, String email4) throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Sends request to a specific user, given by email;
     * @param email the given email.
     */
    public void sendRequest(String email) throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Accepts a request of a specific user, given by email;
     * @param email the given email.
     */
    public void acceptRequest(String email) throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Declines a request of a specific user, given by email;
     * @param email the given email.
     */
    public void declineRequest(String email) throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Deletes a friendship with a specific user, given by email;
     * @param email the given email.
     */
    public void deleteFriend(String email) throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Returns the list with the accepted friendships;
     * @return the list with the accepted friendships.
     */
    public FriendshipList acceptedFriendships() throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Returns the pendig list;
     * @return the pending list.
     */
    public FriendshipList pendingFriendships() throws Exception {
        throw new AccessException("inaccessible method");
    }


    protected  void showList(SpecificList list) throws Exception{
        throw new AccessException("inaccessible method");
    }

    /**
     * Sends a message to one/more users;
     * @param emails_to the users, separated by comma;
     * @param text the text of the message;
     * @param reply_uuid the uuid for reply message.
     */
    public void sendMessage(String emails_to, String text, String reply_uuid) throws Exception {
        throw new AccessException("inaccessible method");
    }

    /**
     * Creates the list of messages with a given user;
     * @param email the given user.
     */
    public List<ReplyMessage> messagesWith(String email) throws Exception {
        throw new AccessException("inaccessible method");
    }
}
