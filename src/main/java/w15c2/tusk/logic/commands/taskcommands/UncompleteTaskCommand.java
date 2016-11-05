package w15c2.tusk.logic.commands.taskcommands;

import javafx.collections.ObservableList;
import w15c2.tusk.commons.core.EventsCenter;
import w15c2.tusk.commons.core.Messages;
import w15c2.tusk.commons.events.ui.HideHelpRequestEvent;
import w15c2.tusk.logic.commands.CommandResult;
import w15c2.tusk.model.task.Task;

/**
 * Unpins a task identified using it's last displayed index from TaskManager.
 */
//@@author A0143107U
public class UncompleteTaskCommand extends TaskCommand {

	public static final String COMMAND_WORD = "uncomplete";
    public static final String ALTERNATE_COMMAND_WORD = null;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Uncompletes the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNCOMPLETE_TASK_SUCCESS = "Uncompleted task: %1$s";
    public static final String MESSAGE_TASK_ALR_UNCOMPLETED = "Task is already uncompleted";

    public static final String HELP_MESSAGE_USAGE = "Uncomplete a task: \t" + COMMAND_WORD +" <index>";


    public final int targetIndex;

    public UncompleteTaskCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult execute() {

	    ObservableList<Task> lastShownList = model.getCurrentFilteredTasks();

        if (lastShownList.size() < targetIndex || targetIndex <= 0) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToUncomplete = lastShownList.get(targetIndex - 1);
        if(taskToUncomplete.isCompleted()){
            EventsCenter.getInstance().post(new HideHelpRequestEvent());
        	model.uncompleteTask(taskToUncomplete);
        	model.refreshTasksFilter();
        	return new CommandResult(String.format(MESSAGE_UNCOMPLETE_TASK_SUCCESS, taskToUncomplete));
        }
        else{
        	return new CommandResult(MESSAGE_TASK_ALR_UNCOMPLETED);

        }
    }

}