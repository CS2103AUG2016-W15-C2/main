package w15c2.tusk.logic.parser;


import static w15c2.tusk.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import w15c2.tusk.logic.commands.Command;
import w15c2.tusk.logic.commands.ExitCommand;
import w15c2.tusk.logic.commands.IncorrectCommand;

//@@author A0143107U
/**
 * Parses Exit commands
 */
public class ExitCommandParser extends CommandParser {
    public static final String COMMAND_WORD = ExitCommand.COMMAND_WORD;
    public static final String ALTERNATE_COMMAND_WORD = ExitCommand.ALTERNATE_COMMAND_WORD;
    
    /**
    * Parses arguments in the context of the exit command.
    *
    * @param args full command args string
    * @return the prepared command
    */
    @Override
    public Command prepareCommand(String arguments) {
        if(!arguments.equals("")){
        	return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExitCommand.MESSAGE_USAGE));
        }
        return new ExitCommand();
    }
}
