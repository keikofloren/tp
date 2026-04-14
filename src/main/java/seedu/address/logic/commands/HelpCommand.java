package seedu.address.logic.commands;

import seedu.address.model.Model;
import seedu.address.ui.HelpWindow;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE =
            "Commands summary:\n"
                    + "------------------------------------------------------\n"
                    + "add           n/NAME a/AGE p/PHONE e/EMAIL ad/ADDRESS d/START_DATE "
                    + "ec/EMERGENCY_CONTACT [t/TAG]... [av/AVAILABLE_DAY]...\n"
                    + "addtime       INDEX dist/DISTANCE min/MINUTES sec/SECONDS\n"
                    + "del           INDEX\n"
                    + "deltime       ATHLETE_INDEX TIMING_INDEX\n"
                    + "edit          INDEX [n/NAME] [a/AGE] [p/PHONE] [e/EMAIL] [ad/ADDRESS] "
                    + "[ec/EMERGENCY_CONTACT] [d/START_DATE] [ta/TAG]... [td/TAG]... [av/AVAILABLE_DAY]...\n"
                    + "find          [n/NAME]... [p/PHONE]... [t/TAG]... [av/AVAILABLE_DAY]...\n"
                    + "sort          by/FIELD [ord/ORDER]   (fields: name, pb  |  orders: asc, desc)\n"
                    + "view          INDEX\n"
                    + "list\n"
                    + "clear\n"
                    + "exit\n"
                    + "------------------------------------------------------\n"
                    + "For full details: " + HelpWindow.USERGUIDE_URL;

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
