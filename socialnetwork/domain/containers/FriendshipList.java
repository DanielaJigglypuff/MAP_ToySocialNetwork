package socialnetwork.domain.containers;

import socialnetwork.domain.FriendshipWithStatus;

import java.util.ArrayList;

import static socialnetwork.domain.constants.Constants.DATEFORMATTER;

public class FriendshipList extends ArrayList<FriendshipWithStatus> {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < size(); i++) {
            sb.append("Friendship: ")
                    .append(get(i).getLeft().getEmail())
                    .append(" | ")
                    .append(get(i).getRight().getEmail())
                    .append(" | ")
                    .append(get(i).getDate().toLocalDate().format(DATEFORMATTER));
            if (i + 1 != size())
                sb.append("\n");
        }
        return sb.toString();
    }
}
