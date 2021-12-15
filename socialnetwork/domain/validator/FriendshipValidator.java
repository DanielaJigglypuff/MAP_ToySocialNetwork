package socialnetwork.domain.validator;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.exceptions.ValidationException;

public class FriendshipValidator<E extends Friendship> implements Validator<E> {
    @Override
    public void validate(E entity) throws ValidationException {
        if (entity.getLeft().equals(entity.getRight()))
            throw new ValidationException("Users must be different");
    }
}
