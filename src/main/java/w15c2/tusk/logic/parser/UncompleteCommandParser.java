package w15c2.tusk.logic.parser;

import static w15c2.tusk.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import w15c2.tusk.commons.util.StringUtil;
import w15c2.tusk.logic.commands.Command;
import w15c2.tusk.logic.commands.IncorrectCommand;
import w15c2.tusk.logic.commands.taskcommands.UncompleteTaskCommand;

//@@author A0143107U
/**
 * Parses Uncomplete commands
 */
public class UncompleteCommandParser extends CommandParser{
	public static final String COMMAND_WORD = UncompleteTaskCommand.COMMAND_WORD;
    public static final String ALTERNATE_COMMAND_WORD = UncompleteTaskCommand.ALTERNATE_COMMAND_WORD;

    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

	/**
     * Parses arguments in the context of the Uncomplete task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
	public Command prepareCommand(String arguments) {
		 Optional<Integer> index = parseIndex(arguments);
	        if(!index.isPresent()){
	            return new IncorrectCommand(
	                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UncompleteTaskCommand.MESSAGE_USAGE));
	        }

	        return new UncompleteTaskCommand(index.get());
	}
	/**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }
}
