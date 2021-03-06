package w15c2.tusk.logic.commands.taskcommands;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.ObservableList;
import w15c2.tusk.commons.collections.UniqueItemCollection.DuplicateItemException;
import w15c2.tusk.commons.collections.UniqueItemCollection.ItemNotFoundException;
import w15c2.tusk.commons.core.Messages;
import w15c2.tusk.commons.exceptions.IllegalValueException;
import w15c2.tusk.commons.util.DateUtil;
import w15c2.tusk.logic.commands.Command;
import w15c2.tusk.logic.commands.CommandResult;
import w15c2.tusk.model.task.DeadlineTask;
import w15c2.tusk.model.task.Description;
import w15c2.tusk.model.task.EventTask;
import w15c2.tusk.model.task.FloatingTask;
import w15c2.tusk.model.task.Task;

//@@author A0139817U
/**
 * Updates a task identified using it's last displayed index from Task Manager.
 */
public class UpdateTaskCommand extends Command {

	public static final String COMMAND_WORD = "update";
    public static final String ALTERNATE_COMMAND_WORD = "edit";
    
    public static final String COMMAND_FORMAT = COMMAND_WORD + "/" + ALTERNATE_COMMAND_WORD + " <INDEX> task <UPDATED VALUE>\n"
            + COMMAND_WORD + "/" + ALTERNATE_COMMAND_WORD + " <INDEX> desc <UPDATED VALUE>\n" 
    		+ COMMAND_WORD + "/" + ALTERNATE_COMMAND_WORD + " <INDEX> date <UPDATED VALUE>";
    public static final String COMMAND_DESCRIPTION = "Updates/Edits the whole task\nUpdate/Edits description\nUpdate/Edits date"; 

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates/Edits the task identified by the index number used in the last task listing.\n"
            + "Depending on whether 'task', 'desc' or 'date' is stated, the task will be updated accordingly.\n"
            + "1) Parameters: INDEX task UPDATED_VALUE\n"
            + "Example: " + COMMAND_WORD + " 1 task Meeting from Oct 31 to Nov 1\n"
            + "2) Parameters: INDEX desc UPDATED_VALUE\n"
            + "Example: " + ALTERNATE_COMMAND_WORD + " 1 description Meeting in town\n"
            + "3) Parameters: INDEX date UPDATED_VALUE\n"
            + "Example: " + ALTERNATE_COMMAND_WORD + " 1 date Oct 31 to Nov 1";

    public static final String MESSAGE_UPDATE_TASK_SUCCESS = "Updated task: %1$s";
    private static final String MESSAGE_CANNOT_UPDATE_TASK = "Selected task's description cannot be updated";
    
    public static final String TASK_DETAILS_UPDATE_TASK = "[Update Task][Task: %s]";
    public static final String TASK_DETAILS_UPDATE_DESCRIPTION = "[Update Task][Description: %s]";
    public static final String TASK_DETAILS_UPDATE_DEADLINE = "[Update Task][Deadline: %s]";
    public static final String TASK_DETAILS_UPDATE_START_END_DATE = "[Update Task][Start date: %s][End date: %s]";
    
    private final int targetIndex;
    private Task updatedTask;
    
    // Values that are to be updated. If it is not supposed to be updated, it will be null
    private Task newTask;
    private Description newDescription;
    private Date newDeadline;
    private Date newStartDate;
    private Date newEndDate;

    /**
     * This constructor is called by the user enters a command to replace the entire task.
     * 
     * Example: update 1 task Homework by 31 Oct 2016
     * (Replaces whatever task at index 1 to be a DeadlineTask with description as "Homework" and deadline by 31 Oct 2016)
     * 
     * @param targetIndex	Target index of the task to be updated.
     * @param newTask		New task to replace the old task.
     * @return 				UpdateTaskCommand object that updates the whole task when executed.
     */
    public UpdateTaskCommand(int targetIndex, Task newTask) {
        this.targetIndex = targetIndex;
        this.newTask = newTask;
    }
    
    /**
     * This constructor is called by the user enters a command to update the description of a task.
     * 
     * Example: update 1 description Meeting
     * (Changes the description of the task at index 1 to be "Meeting")
     * 
     * @param targetIndex	Target index of the task to be updated.
     * @param description	New description to replace the description of the old task.
     * @return 				UpdateTaskCommand object that updates the description when executed.
     */
    public UpdateTaskCommand(int targetIndex, Description newDescription) {
        this.targetIndex = targetIndex;
        this.newDescription = newDescription;
    }
    
    /**
     * This constructor is called by the user enters a command to update the deadline of a task.
     * 
     * Example: update 1 date 31 Oct
     * (Changes the task at index 1 to have a deadline of 31 Oct 2016 (Whether or not it is a deadline task))
     * 
     * @param targetIndex	Target index of the task to be updated.
     * @param newDeadline	New deadline for the old task.
     * @return 				UpdateTaskCommand object that updates the deadline when executed.
     */
    public UpdateTaskCommand(int targetIndex, Date newDeadline) {
        this.targetIndex = targetIndex;
        this.newDeadline = newDeadline;
    }
    
    /**
     * This constructor is called by the user enters a command to update the start date and end date of a task.
     * 
     * Example: update 1 date 31 Oct to 1 Nov
     * (Changes the task at index 1 to have a start date of 31 Oct and end date of 1 Nov (Whether or not it is an event task))
     * 
     * @param targetIndex	Target index of the task to be updated.
     * @param newStartDate	New start date for the old task.
     * @param newEndDate	New end date for the old task.
     * @return 				UpdateTaskCommand object that updates the start date and end date when executed.
     */
    public UpdateTaskCommand(int targetIndex, Date newStartDate, Date newEndDate) {
        this.targetIndex = targetIndex;
        this.newStartDate = newStartDate;
        this.newEndDate = newEndDate;
    }
    
    /**
     * Given the task that is to be updated, create a new updatedTask to replace it
     * by retrieving the values to be updated.
     * 
     * @param taskToUpdate				Task that is supposed to be updated.
     * @throws IllegalValueException 	If the parameters to update are illegal.
     */
    private void prepareUpdatedTask(Task taskToUpdate) throws IllegalValueException {
    	if (newTask != null) {
    		// User wants to change the entire task
    		updatedTask = newTask;
    		
    	} else if (newDescription != null) {
    		// User wants to change just the description
    		updatedTask = prepareUpdatedDescriptionForTask(taskToUpdate);
    		
    	} else if (newDeadline != null) {
    		// User wants to change the deadline of a Task
    		updatedTask = prepareUpdatedDeadlineForTask(taskToUpdate);

    	} else if ((newStartDate != null && newEndDate != null)) {
    		// User wants to change the start date and end date of a Task
    		updatedTask = prepareUpdatedStartEndDateForTask(taskToUpdate);
    		
    	} else {
    		assert false : "At least task, description or date should have new values";
    	}
    	
    	// Retain pin status
		if (taskToUpdate.isPinned()) {
			updatedTask.setAsPin();
		}
		// Retain edited status
		if (taskToUpdate.isCompleted()) {
			updatedTask.setAsComplete();
		}
    }
    
    /**
     * Create a new task with a different description to replace taskToUpdate.
     * 
     * @param taskToUpdate				Task that is supposed to be updated.
     * @return							New task to replace the old task.
     * @throws IllegalValueException	If the parameters to update are illegal.
     */
    private Task prepareUpdatedDescriptionForTask(Task taskToUpdate) throws IllegalValueException {
    	// Return a new Task based on the type of the task to be updated
		if (taskToUpdate instanceof FloatingTask) {
			return new FloatingTask(newDescription.getContent());
			
		} else if (taskToUpdate instanceof DeadlineTask) {
			DeadlineTask task = (DeadlineTask) taskToUpdate;
			return new DeadlineTask(newDescription.getContent(), task.getDeadline());
			
		} else if (taskToUpdate instanceof EventTask) {
			EventTask task = (EventTask) taskToUpdate;
			return new EventTask(newDescription.getContent(), task.getStartDate(), task.getEndDate());	
		
		} else {
			throw new IllegalValueException(MESSAGE_CANNOT_UPDATE_TASK);
		}	
    }
    
    /**
     * Create a new task with a different deadline to replace taskToUpdate
     * 
     * @param taskToUpdate				Task that is supposed to be updated.
     * @return							New task to replace the old task.
     * @throws IllegalValueException	If the parameters to update are illegal.
     */
    private Task prepareUpdatedDeadlineForTask(Task taskToUpdate) throws IllegalValueException {
    	// Create a deadline task to replace the original task
    	String description = taskToUpdate.getDescription().getContent();
		return new DeadlineTask(description, newDeadline);
    }
    
    /**
     * Create a new task with a different start and end date to replace taskToUpdate
     * 
     * @param taskToUpdate				Task that is supposed to be updated.
     * @return							New task to replace the old task.
     * @throws IllegalValueException	If the parameters to update are illegal.
     */
    private Task prepareUpdatedStartEndDateForTask(Task taskToUpdate) throws IllegalValueException {
    	// Create an event task to replace the original task
    	String description = taskToUpdate.getDescription().getContent();
		return new EventTask(description, newStartDate, newEndDate);
    }
    
    /**
     * Retrieve the details of the values (with or without time) to be updated for testing purposes
     */
    public String getTaskDetails(boolean withTime) {
    	SimpleDateFormat dateFormat;
    	// Decide which date format to use
    	if (withTime) {
    		dateFormat = DateUtil.dateFormatWithTime;
    	} else {
    		dateFormat = DateUtil.dateFormat;
    	}
    	
    	if (newTask != null) {
    		return String.format(TASK_DETAILS_UPDATE_TASK, newTask.getTaskDetails(withTime));
    	} else if (newDescription != null) {
    		return String.format(TASK_DETAILS_UPDATE_DESCRIPTION, newDescription);
    	} else if (newDeadline != null) {
    		return String.format(TASK_DETAILS_UPDATE_DEADLINE,
    				dateFormat.format(newDeadline));
    	} else if (newStartDate != null && newEndDate != null) {
    		return String.format(TASK_DETAILS_UPDATE_START_END_DATE, 
    				dateFormat.format(newStartDate), dateFormat.format(newEndDate));
    	} else {
    		return "Error";
    	}
    }


    /**
     * Updates the targeted task in the Model.
     * 
     * @return CommandResult Result of the execution of the update command.
     */
    @Override
    public CommandResult execute() {

	    ObservableList<Task> lastShownList = model.getCurrentFilteredTasks();

        if (lastShownList.size() < targetIndex || targetIndex <= 0) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToUpdate = lastShownList.get(targetIndex - 1);

        try {
        	prepareUpdatedTask(taskToUpdate);
            model.updateTask(taskToUpdate, updatedTask);
        } catch (DuplicateItemException die) {
        	assert false : "Deletion of the original task (before addition of an updated task) has failed";
        } catch (IllegalValueException ive) {
        	return new CommandResult(ive.getMessage());
        } catch (ItemNotFoundException tnfe) {
            assert false : "The target item cannot be missing";
        } 

        closeHelpWindow();
        return new CommandResult(String.format(MESSAGE_UPDATE_TASK_SUCCESS, updatedTask.getDescription().getContent()));
    }

}
