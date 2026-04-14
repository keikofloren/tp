package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag name
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName("-leading")); // leading hyphen
        assertFalse(Tag.isValidTagName("trailing-")); // trailing hyphen
        assertFalse(Tag.isValidTagName("double--hyphen")); // consecutive hyphens
        assertFalse(Tag.isValidTagName("@symbol")); // non-alphanumeric character

        // valid tag name
        assertTrue(Tag.isValidTagName("teamA")); // alphanumeric only
        assertTrue(Tag.isValidTagName("cross-country")); // hyphenated tag
        assertTrue(Tag.isValidTagName("u-20")); // alphanumeric with hyphen
        assertTrue(Tag.isValidTagName("long-distance runner")); // hyphenated word with space
    }

}
