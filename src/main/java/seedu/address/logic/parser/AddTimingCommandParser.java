package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DISTANCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEC;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddTimingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.RunTiming;

/**
 * Parses user input arguments and creates an {@link AddTimingCommand}.
 *
 * <p>This parser validates the athlete index, run distance, and timing fields
 * separately so that users receive more specific and helpful error messages.</p>
 */
public class AddTimingCommandParser implements Parser<AddTimingCommand> {

    private static final String COMMAND_FORMAT =
            "Correct command format: addtime INDEX dist/DISTANCE min/MINUTES sec/SECONDS";

    private static final String MESSAGE_MISSING_DISTANCE =
            "Missing required field: dist/DISTANCE\n" + AddTimingCommandParser.COMMAND_FORMAT;

    private static final String MESSAGE_MISSING_MINUTES =
            "Missing required field: min/MINUTES\n" + AddTimingCommandParser.COMMAND_FORMAT;

    private static final String MESSAGE_MISSING_SECONDS =
            "Missing required field: sec/SECONDS\n" + AddTimingCommandParser.COMMAND_FORMAT;

    private static final String MESSAGE_INVALID_INDEX =
            "Invalid index: please provide a positive integer athlete index\n" + AddTimingCommandParser.COMMAND_FORMAT;

    private static final String MESSAGE_INVALID_MINUTES =
            "Invalid minutes: must be a non-negative integer\n" + AddTimingCommandParser.COMMAND_FORMAT;

    private static final String MESSAGE_INVALID_SECONDS =
            "Invalid seconds: must be a number from 0 to 59.99\n" + AddTimingCommandParser.COMMAND_FORMAT;

    private static final String MESSAGE_INVALID_ZERO_TIME =
            "Invalid timing: total time must be greater than 0\n" + AddTimingCommandParser.COMMAND_FORMAT;

    private static final String MESSAGE_DUPLICATE_FIELDS =
            "Invalid command: each field (dist/, min/, sec/) can only be provided once\n"
                    + AddTimingCommandParser.COMMAND_FORMAT;

    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@code AddTimingCommand} and returns an {@code AddTimingCommand}.
     *
     * @param args Full user input after the command word.
     * @return A new {@code AddTimingCommand} containing the parsed values.
     * @throws ParseException If the input format or values are invalid.
     */
    @Override
    public AddTimingCommand parse(String args) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_DISTANCE, PREFIX_MIN, PREFIX_SEC);

        validateNoDuplicateFields(map);
        validateRequiredFields(map);

        Index index = parseIndex(map.getPreamble());
        String distance = parseDistance(map.getValue(PREFIX_DISTANCE).get());
        int minutes = parseMinutes(map.getValue(PREFIX_MIN).get());
        double seconds = parseSeconds(map.getValue(PREFIX_SEC).get());

        validateTotalTime(minutes, seconds);

        RunTiming timing = new RunTiming(distance, minutes, seconds);
        return new AddTimingCommand(index, timing);
    }

    /**
     * Ensures each supported prefix appears at most once.
     *
     * @param map Tokenized user input.
     * @throws ParseException If any prefix appears more than once.
     */
    private void validateNoDuplicateFields(ArgumentMultimap map) throws ParseException {
        try {
            map.verifyNoDuplicatePrefixesFor(PREFIX_DISTANCE, PREFIX_MIN, PREFIX_SEC);
        } catch (ParseException e) {
            throw new ParseException(MESSAGE_DUPLICATE_FIELDS);
        }
    }

    /**
     * Ensures all required timing fields are present.
     *
     * @param map Tokenized user input.
     * @throws ParseException If any required field is missing.
     */
    private void validateRequiredFields(ArgumentMultimap map) throws ParseException {
        if (map.getValue(PREFIX_DISTANCE).isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_DISTANCE);
        }

        if (map.getValue(PREFIX_MIN).isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_MINUTES);
        }

        if (map.getValue(PREFIX_SEC).isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_SECONDS);
        }
    }

    /**
     * Parses and validates the athlete index.
     *
     * @param preamble The unprefixed portion of the input.
     * @return Parsed athlete index.
     * @throws ParseException If the index is missing or invalid.
     */
    private Index parseIndex(String preamble) throws ParseException {
        try {
            return ParserUtil.parseIndex(preamble);
        } catch (ParseException e) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
    }

    /**
     * Parses and validates the race distance.
     *
     * @param rawDistance Raw distance string from user input.
     * @return Validated distance string.
     * @throws ParseException If the distance is unsupported.
     */
    private String parseDistance(String rawDistance) throws ParseException {
        if (!RunTiming.isValidDistance(rawDistance)) {
            throw new ParseException(
                    "Invalid distance: supported distances are 400m, 2.4km, 10km, and 42km.");
        }
        return rawDistance;
    }

    /**
     * Parses and validates the minutes component.
     *
     * @param rawMinutes Raw minutes string from user input.
     * @return Parsed minutes value.
     * @throws ParseException If minutes are not a valid non-negative integer.
     */
    private int parseMinutes(String rawMinutes) throws ParseException {
        try {
            int minutes = Integer.parseInt(rawMinutes);
            if (minutes < 0) {
                throw new ParseException(MESSAGE_INVALID_MINUTES);
            }
            return minutes;
        } catch (NumberFormatException e) {
            throw new ParseException(MESSAGE_INVALID_MINUTES);
        }
    }

    /**
     * Parses and validates the seconds component.
     *
     * @param rawSeconds Raw seconds string from user input.
     * @return Parsed seconds value.
     * @throws ParseException If seconds are not within the accepted range.
     */
    private double parseSeconds(String rawSeconds) throws ParseException {
        try {
            double seconds = Double.parseDouble(rawSeconds);
            if (seconds < 0 || seconds > 59.99) {
                throw new ParseException(MESSAGE_INVALID_SECONDS);
            }
            return seconds;
        } catch (NumberFormatException e) {
            throw new ParseException(MESSAGE_INVALID_SECONDS);
        }
    }

    /**
     * Validates that the total timing is greater than zero.
     *
     * @param minutes Minutes component.
     * @param seconds Seconds component.
     * @throws ParseException If the total time is zero.
     */
    private void validateTotalTime(int minutes, double seconds) throws ParseException {
        if (minutes == 0 && seconds == 0) {
            throw new ParseException(MESSAGE_INVALID_ZERO_TIME);
        }
    }
}
