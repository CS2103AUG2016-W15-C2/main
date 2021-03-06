package w15c2.tusk.logic.parser;

import static w15c2.tusk.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import w15c2.tusk.logic.commands.Command;
import w15c2.tusk.logic.commands.IncorrectCommand;
import w15c2.tusk.logic.commands.taskcommands.ClearTaskCommand;

//@@author A0139817U
/**
 * Parses Clear commands
 */
public class ClearCommandParser extends CommandParser{
	public static final String COMMAND_WORD = ClearTaskCommand.COMMAND_WORD;
	public static final String ALTERNATE_COMMAND_WORD = ClearTaskCommand.ALTERNATE_COMMAND_WORD;
	
	 /**
     * Parses arguments in the context of the clear task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    public Command prepareCommand(String arguments) {
    	if(!arguments.equals("")){
        	return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTaskCommand.MESSAGE_USAGE));
        }
        return new ClearTaskCommand();
    }
}
