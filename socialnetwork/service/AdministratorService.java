package socialnetwork.service;

import socialnetwork.domain.FriendshipWithStatus;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.RepositoryException;

import java.util.ArrayList;

/**
 * Administrator service.
 */
public class AdministratorService extends AbstractService {
    public AdministratorService(AbstractService service) {
        super(service);
    }

    @Override
    public void addUser(String email, String firstName, String lastName) throws Exception {
        User user = users.save(new User(email, firstName, lastName));
        if (user == null) throw new RepositoryException("the email already exists");
    }

    @Override
    public void deleteUser(String email) throws Exception {
        User user = users.findByEmail(email);
        if (user == null)
            throw new RepositoryException("the email does not exits");
        ArrayList<FriendshipWithStatus> toDel = new ArrayList<>();
        for (FriendshipWithStatus friendship : friendships.findAll())
            if (friendship.isFriend(user))
                toDel.add(friendship);
        for(FriendshipWithStatus friendship : toDel)
            friendships.delete(friendship.getId());
        user = users.delete(user.getId());
        if (user == null) throw new RepositoryException("the email does not exist");
    }

    @Override
    public void updateUser(String email, String firstName, String lastName) throws Exception {
        User user = friendships.getUsers().findByEmail(email);
        if (user == null)
            throw new RepositoryException("the email does not exist");
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user = users.update(user);
        if (user == null) throw new RepositoryException("the email does not exist");
    }

    @Override
    public void addFriendship(String email1, String email2) throws Exception {
        if (users.findByEmail(email1) == null)
            throw new RepositoryException("none of the users has one of the emails");
        if (users.findByEmail(email2) == null)
            throw new RepositoryException("none of the users has one of the emails");
        FriendshipWithStatus friendship = friendships.save(new FriendshipWithStatus(users.findByEmail(email1), users.findByEmail(email2)));
        if (friendship == null) throw new RepositoryException("the friendship already exists");
    }

    @Override
    public void deleteFriendship(String email1, String email2) throws Exception {
        if (users.findByEmail(email1) == null || users.findByEmail(email2) == null)
            throw new RepositoryException("none of the users has one of the emails");
        FriendshipWithStatus friendship = new FriendshipWithStatus(users.findByEmail(email1), users.findByEmail(email2));
        for (FriendshipWithStatus i : friendships.findAll())
            if (i.equals(friendship)) {
                friendships.delete(i.getId());
                return;
            }
    }

    @Override
    public void updateFriendship(String email1, String email2, String email3, String email4) throws Exception {
        if (users.findByEmail(email1) == null)
            throw new RepositoryException("none of the users has one of the emails");
        if (users.findByEmail(email2) == null)
            throw new RepositoryException("none of the users has one of the emails");
        FriendshipWithStatus friendship = new FriendshipWithStatus(users.findByEmail(email1), users.findByEmail(email2));
        for (FriendshipWithStatus i : friendships.findAll())
            if (i.equals(friendship)) {
                friendship.setId(i.getId());
            }
        friendship.setLeft(friendships.getUsers().findByEmail(email3));
        friendship.setRight(friendships.getUsers().findByEmail(email4));
        friendships.update(friendship);
    }
}
