package socialnetwork.domain.validator;

import socialnetwork.domain.Message;
import socialnetwork.domain.exceptions.ValidationException;

public class MessageValidator<E extends Message> implements Validator<E> {

    @Override
    public void validate(E entity) throws ValidationException {

    }
}
