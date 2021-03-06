package w15c2.tusk.logic.autocomplete;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import w15c2.tusk.commons.core.LogsCenter;
import w15c2.tusk.logic.parser.CommandParserList;

//@@author A0138978E
/**
 * Returns a set of words for autocomplete purposes based on the requested
 * source
 */
public class AutocompleteSource {
    
    /**
     * Iterate through all parsers and get the command words, fail with assertion errors on any problems
       because we don't expect any non-programming related issues in this method
     * @return
     */
	public static Set<String> getCommands() {

		
		Class<?>[] parserTypes = CommandParserList.getList();
		Logger logger = LogsCenter.getLogger(AutocompleteSource.class);
		Set<String> commandWords = new HashSet<String>();
		
		for (Class<?> parser : parserTypes) {
			try {
				String commandWord = (String) parser.getField("COMMAND_WORD").get(null);
				String altCommandWord = (String) parser.getField("ALTERNATE_COMMAND_WORD").get(null);
				
				commandWords.add(commandWord);
				if (altCommandWord != null) {
				    commandWords.add(altCommandWord);
				}
				
			} catch (NoSuchFieldException ex) {
				logger.severe("No such field - check parser list, exception: " + ex.getMessage());
				assert false;
			} catch (Exception e) {
				logger.severe("Generic exception: " + e.getMessage());
				assert false;
			}
		}
		
		return commandWords;
	}
}
