package socialnetwork.domain.util;

import socialnetwork.domain.FriendshipWithStatus;

public interface SpecificOperation {
    FriendshipWithStatus process(FriendshipWithStatus friendship) throws Exception;
}
