package w15c2.tusk.logic.parser;

import static w15c2.tusk.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import w15c2.tusk.logic.commands.taskcommands.ClearTaskCommand;
import w15c2.tusk.logic.commands.taskcommands.IncorrectTaskCommand;
import w15c2.tusk.logic.commands.taskcommands.TaskCommand;

/*
 * Parses Clear commands
 */
//@@author A0139817U
public class ClearCommandParser extends CommandParser{
	public static final String COMMAND_WORD = ClearTaskCommand.COMMAND_WORD;
	public static final String ALTERNATE_COMMAND_WORD = ClearTaskCommand.ALTERNATE_COMMAND_WORD;
	
	 /**
     * Parses arguments in the context of the clear task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    public TaskCommand prepareCommand(String arguments) {
    	if(!arguments.equals("")){
        	return new IncorrectTaskCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTaskCommand.MESSAGE_USAGE));
        }
        return new ClearTaskCommand();
    }
}