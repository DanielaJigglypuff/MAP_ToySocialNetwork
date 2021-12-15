package socialnetwork.domain;

import socialnetwork.domain.primary.Entity;

import java.util.Objects;
import java.util.UUID;

/**
 * Object user.
 */
public class User extends Entity<UUID> {
    protected String email;
    protected String firstName;
    protected String lastName;

    public User(String email, String firstName, String lastName) {
        if (this.getId() == null) this.setId(UUID.randomUUID());
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Returns the email;
     * @return the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the first name;
     * @return the first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name;
     * @return the last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the email with a given one;
     * @param email the given email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the first name with a given one;
     * @param firstName the given first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name with a given one;
     * @param lastName the given last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.firstName  + " " + this.lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return email.equals(user.email) && firstName.equals(user.firstName) && lastName.equals(user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, firstName, lastName);
    }
}
