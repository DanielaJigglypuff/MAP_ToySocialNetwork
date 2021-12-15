package socialnetwork.domain.validator;

import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.ValidationException;

import static socialnetwork.domain.constants.Constants.ALPHABET_VALIDATOR;
import static socialnetwork.domain.constants.Constants.EMAIL_VALIDATOR;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        if (!entity.getEmail().matches(EMAIL_VALIDATOR))
            throw new ValidationException("Email is invalid");
        if (!entity.getFirstName().matches(ALPHABET_VALIDATOR))
            throw new ValidationException("First name must contain only alphabets");
        if (!entity.getLastName().matches(ALPHABET_VALIDATOR))
            throw new ValidationException("Last name must contain only alphabets");
    }
}
