package w15c2.tusk.logic.commands.taskcommands;

import javafx.collections.ObservableList;
import w15c2.tusk.commons.collections.UniqueItemCollection.ItemNotFoundException;
import w15c2.tusk.commons.core.Messages;
import w15c2.tusk.logic.commands.Command;
import w15c2.tusk.logic.commands.CommandResult;
import w15c2.tusk.model.task.Task;

/**
 * Deletes a task identified using it's last displayed index from TaskManager.
 */
public class DeleteTaskCommand extends Command {

	public static final String COMMAND_WORD = "delete";
    public static final String ALTERNATE_COMMAND_WORD = null;
    
    public static final String COMMAND_FORMAT = COMMAND_WORD + " <INDEX>";
    public static final String COMMAND_DESCRIPTION = "Delete a Task"; 
    
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted task: %1$s";

    public final int targetIndex;

    public DeleteTaskCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Delete the task at the targeted index in the Model.
     * 
     * @return CommandResult Result of the execution of the delete command.
     */
    @Override
    public CommandResult execute() {

	    ObservableList<Task> lastShownList = model.getCurrentFilteredTasks();

        if (lastShownList.size() < targetIndex || targetIndex <= 0) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToDelete = lastShownList.get(targetIndex - 1);

	        try {
	            model.deleteTask(taskToDelete);
	            closeHelpWindow();
	            if(lastShownList.size() == 0) {
	                model.clearTasksFilter();
	            }
	        } catch (ItemNotFoundException tnfe) {
	            assert false : "The target item cannot be missing";
	        }

        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete));
    }

}
