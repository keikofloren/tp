package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddTimingCommand;

/**
 * Contains parser tests for {@code AddTimingCommandParser}.
 */
public class AddTimingCommandParserTest {

    private final AddTimingCommandParser parser = new AddTimingCommandParser();

    /**
     * Tests parse success for a valid 400m timing command.
     */
    @Test
    public void parse_valid400m_success() {
        assertDoesNotThrow(() -> {
            AddTimingCommand command = parser.parse("1 dist/400m min/0 sec/55");
            assertTrue(command instanceof AddTimingCommand);
        });
    }

    /**
     * Tests parse success for a valid 2.4km timing command.
     */
    @Test
    public void parse_valid24km_success() {
        assertDoesNotThrow(() -> {
            AddTimingCommand command = parser.parse("1 dist/2.4km min/10 sec/30");
            assertTrue(command instanceof AddTimingCommand);
        });
    }

    /**
     * Tests parse success for a valid 10km timing command.
     */
    @Test
    public void parse_valid10km_success() {
        assertDoesNotThrow(() -> {
            AddTimingCommand command = parser.parse("1 dist/10km min/47 sec/30");
            assertTrue(command instanceof AddTimingCommand);
        });
    }

    /**
     * Tests parse success for a valid 42km timing command.
     */
    @Test
    public void parse_valid42km_success() {
        assertDoesNotThrow(() -> {
            AddTimingCommand command = parser.parse("1 dist/42km min/240 sec/30");
            assertTrue(command instanceof AddTimingCommand);
        });
    }

    /**
     * Tests parse failures when required fields are missing.
     */
    @Test
    public void parse_missingFields_failure() {
        // missing all prefixed fields
        assertParseFailure(parser, "1",
                "Missing required field: dist/DISTANCE\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");

        // missing dist/
        assertParseFailure(parser, "1 min/10 sec/30",
                "Missing required field: dist/DISTANCE\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");

        // missing min/
        assertParseFailure(parser, "1 dist/2.4km sec/30",
                "Missing required field: min/MINUTES\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");

        // missing sec/
        assertParseFailure(parser, "1 dist/2.4km min/10",
                "Missing required field: sec/SECONDS\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");
    }

    /**
     * Tests parse failures when duplicate prefixes are provided.
     */
    @Test
    public void parse_duplicateFields_failure() {
        // duplicate dist/
        assertParseFailure(parser, "1 dist/2.4km dist/10km min/10 sec/30",
                "Invalid command: each field (dist/, min/, sec/) can only be provided once\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");

        // duplicate min/
        assertParseFailure(parser, "1 dist/2.4km min/10 min/11 sec/30",
                "Invalid command: each field (dist/, min/, sec/) can only be provided once\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");

        // duplicate sec/
        assertParseFailure(parser, "1 dist/2.4km min/10 sec/30 sec/31",
                "Invalid command: each field (dist/, min/, sec/) can only be provided once\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");
    }

    /**
     * Tests parse failures for invalid distance values.
     */
    @Test
    public void parse_invalidDistance_failure() {
        assertParseFailure(parser, "1 dist/5km min/10 sec/30",
                "Invalid distance: supported distances are 400m, 2.4km, 10km, and 42km.");
    }

    /**
     * Tests parse failures for invalid minutes values.
     */
    @Test
    public void parse_invalidMinutes_failure() {
        // negative minutes
        assertParseFailure(parser, "1 dist/2.4km min/-1 sec/30",
                "Invalid minutes: must be a non-negative integer\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");

        // non-numeric minutes
        assertParseFailure(parser, "1 dist/2.4km min/abc sec/30",
                "Invalid minutes: must be a non-negative integer\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");
    }

    /**
     * Tests parse failures for invalid seconds values.
     */
    @Test
    public void parse_invalidSeconds_failure() {
        // negative seconds
        assertParseFailure(parser, "1 dist/2.4km min/10 sec/-1",
                "Invalid seconds: must be a number from 0 to 59.99\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");

        // seconds out of range
        assertParseFailure(parser, "1 dist/2.4km min/10 sec/60",
                "Invalid seconds: must be a number from 0 to 59.99\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");

        // non-numeric seconds
        assertParseFailure(parser, "1 dist/2.4km min/10 sec/abc",
                "Invalid seconds: must be a number from 0 to 59.99\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");

        // seconds exceed 59.99 (e.g. 59.999)
        assertParseFailure(parser, "1 dist/2.4km min/10 sec/59.999",
                "Invalid seconds: must be a number from 0 to 59.99\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");
    }

    /**
     * Tests that 59.99 seconds is accepted as the boundary value.
     */
    @Test
    public void parse_secondsBoundary_success() {
        assertDoesNotThrow(() -> {
            AddTimingCommand command = parser.parse("1 dist/2.4km min/10 sec/59.99");
            assertTrue(command instanceof AddTimingCommand);
        });
    }

    /**
     * Tests parse failures when the total timing is zero.
     */
    @Test
    public void parse_zeroTotalTime_failure() {
        assertParseFailure(parser, "1 dist/2.4km min/0 sec/0",
                "Invalid timing: total time must be greater than 0\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");
    }

    /**
     * Tests parse failures when the athlete index is invalid.
     */
    @Test
    public void parse_invalidIndex_failure() {
        // non-numeric index
        assertParseFailure(parser, "abc dist/2.4km min/10 sec/30",
                "Invalid index: please provide a positive integer athlete index\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");

        // negative index
        assertParseFailure(parser, "-1 dist/2.4km min/10 sec/30",
                "Invalid index: please provide a positive integer athlete index\n"
                        + "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS");
    }
}
