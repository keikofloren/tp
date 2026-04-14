package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names must be 1 to 100 characters long, contain at least one letter, "
                    + "and may only contain letters, spaces, apostrophes, hyphens, and slashes (e.g. s/o, d/o).";

    /*
     * Names must be between 1 and 100 characters long,
     * contain at least one letter, and may only contain
     * letters, spaces, apostrophes, hyphens, and forward slashes (to support s/o and d/o).
     */
    public static final String VALIDATION_REGEX = "^(?=.{1,100}$)(?=.*[A-Za-z])[-A-Za-z'/ ]+$";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        String normalisedName = normaliseName(name);
        checkArgument(isValidName(normalisedName), MESSAGE_CONSTRAINTS);
        fullName = normalisedName;
    }

    public static String normaliseName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
