package w15c2.tusk.logic.commands.taskcommands;

import w15c2.tusk.commons.collections.UniqueItemCollection;
import w15c2.tusk.commons.exceptions.IllegalValueException;
import w15c2.tusk.logic.commands.CommandResult;
import w15c2.tusk.logic.parser.ParserSelector;
import w15c2.tusk.model.Alias;

//@@author A0143107U
/**
 * Adds a task to TaskManager.
 */
public class AddAliasCommand extends TaskCommand {

    public static final String COMMAND_WORD = "alias";
    public static final String ALTERNATE_COMMAND_WORD = null;
    public static final String COMMAND_FORMAT = COMMAND_WORD + " <SHORTCUT> <SENTENCE>";
    public static final String COMMAND_DESCRIPTION = "Add an Alias"; 
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets a one-word alias for any sentence to be used as a command. "
            + "Parameters: SHORTCUT SENTENCE\n"
            + "Example: " + COMMAND_WORD
            + " am add Meeting";

    public static final String MESSAGE_SUCCESS = "New alias added: %1$s";
    public static final String MESSAGE_DUPLICATE_ALIAS = "This alias already exists in TaskManager";

    private final Alias toAdd;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddAliasCommand(String shortcut, String sentence)
            throws IllegalValueException {
    	if (shortcut == null || shortcut.isEmpty()) {
    		throw new IllegalValueException("Shortcut to AddAliasCommand constructor is empty.\n" + MESSAGE_USAGE);
    	}
    	if (sentence == null || sentence.isEmpty()) {
    		throw new IllegalValueException("Sentence to AliasCommand constructor is empty.\n" + MESSAGE_USAGE);
    	}
        boolean isCommandWord =  ParserSelector.getIsCommandWord(shortcut);
        if(isCommandWord){
    		throw new IllegalValueException("Shortcut " + shortcut + " cannot be an alias as it is a command word.\n" + MESSAGE_USAGE);
        }
        this.toAdd = new Alias(shortcut, sentence);
    }
    
    /**
     * Retrieve the details of the alias to add for testing purposes
     */
    public String getAliasDetails() {
    	return toAdd.toString();
    }
    

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addAlias(toAdd);
            closeHelpWindow();
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueItemCollection.DuplicateItemException e) {
            return new CommandResult(MESSAGE_DUPLICATE_ALIAS);
        }

    }

}
