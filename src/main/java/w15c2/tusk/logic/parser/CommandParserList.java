package w15c2.tusk.logic.parser;

/*
 * List of all available parsers to respond to command input
 */
public class CommandParserList {
	public static Class<?>[] getList(){
	    
		return new Class[]{
				AddCommandParser.class, 
				UpdateCommandParser.class,
				DeleteCommandParser.class, 
				ClearCommandParser.class,
				FindCommandParser.class,
				PinCommandParser.class,
				UnpinCommandParser.class,
				CompleteCommandParser.class,
				UncompleteCommandParser.class,
				UndoCommandParser.class,
				RedoCommandParser.class,
				ListCommandParser.class,
				AddAliasCommandParser.class, 
				DeleteAliasCommandParser.class,
				HelpCommandParser.class,
				SetStorageCommandParser.class,
				ExitCommandParser.class};
	}
}
