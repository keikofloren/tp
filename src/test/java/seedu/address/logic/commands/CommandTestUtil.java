package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVAILABLE_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_AGE_AMY = "25";
    public static final String VALID_AGE_BOB = "31";
    public static final String VALID_PHONE_AMY = "91234567";
    public static final String VALID_PHONE_BOB = "98765432";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_START_DATE_AMY = "01/01/2001";
    public static final String VALID_START_DATE_BOB = "02/02/2002";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_EMERGENCY_CONTACT_AMY = "Mother 91112222";
    public static final String VALID_EMERGENCY_CONTACT_BOB = "Father 93334444";
    public static final String VALID_AVAILABLE_DAY_AMY = "Mon";
    public static final String VALID_AVAILABLE_DAY_BOB = "Wed";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String AGE_DESC_AMY = " " + PREFIX_AGE + VALID_AGE_AMY;
    public static final String AGE_DESC_BOB = " " + PREFIX_AGE + VALID_AGE_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String START_DATE_DESC_AMY = " " + PREFIX_START_DATE + VALID_START_DATE_AMY;
    public static final String START_DATE_DESC_BOB = " " + PREFIX_START_DATE + VALID_START_DATE_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;
    public static final String ADD_TAG_DESC_FRIEND = " " + PREFIX_ADD_TAG + VALID_TAG_FRIEND;
    public static final String ADD_TAG_DESC_HUSBAND = " " + PREFIX_ADD_TAG + VALID_TAG_HUSBAND;
    public static final String DELETE_TAG_DESC_FRIEND = " " + PREFIX_DELETE_TAG + VALID_TAG_FRIEND;
    public static final String DELETE_TAG_DESC_HUSBAND = " " + PREFIX_DELETE_TAG + VALID_TAG_HUSBAND;
    public static final String EMERGENCY_CONTACT_DESC_AMY =
        " " + PREFIX_EMERGENCY_CONTACT + VALID_EMERGENCY_CONTACT_AMY;
    public static final String EMERGENCY_CONTACT_DESC_BOB =
        " " + PREFIX_EMERGENCY_CONTACT + VALID_EMERGENCY_CONTACT_BOB;
    public static final String AVAILABLE_DAY_DESC_AMY = " " + PREFIX_AVAILABLE_DAY + VALID_AVAILABLE_DAY_AMY;
    public static final String AVAILABLE_DAY_DESC_BOB = " " + PREFIX_AVAILABLE_DAY + VALID_AVAILABLE_DAY_BOB;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_AGE_DESC = " " + PREFIX_AGE + "0"; // must be an integer between 10 and 99
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "12345678"; // must start with 8 or 9
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_START_DATE_DESC = " " + PREFIX_START_DATE + "30/02/2025"; // must be a real date
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_EMERGENCY_CONTACT_DESC =
            " " + PREFIX_EMERGENCY_CONTACT + "Mother 12345678"; // invalid phone number
    public static final String INVALID_AVAILABLE_DAY_DESC = " "
        + PREFIX_AVAILABLE_DAY + "Day"; // must be a valid day of the week

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
            .withAge(VALID_AGE_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
            .withEmergencyContact(VALID_EMERGENCY_CONTACT_AMY)
            .withStartDate(VALID_START_DATE_AMY).withTags(VALID_TAG_FRIEND)
            .withAvailableDays(VALID_AVAILABLE_DAY_AMY).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
            .withAge(VALID_AGE_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
            .withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB)
            .withStartDate(VALID_START_DATE_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .withAvailableDays(VALID_AVAILABLE_DAY_BOB).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

}
