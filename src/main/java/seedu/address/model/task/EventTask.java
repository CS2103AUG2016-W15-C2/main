package seedu.address.model.task;

import java.util.Date;

/*
 * An EventTask is a task that holds a start date and an end date
 */
public class EventTask extends Task implements FavoritableTask, DatedTask {

	private Date startDate;
	private Date endDate;

	public EventTask(String descriptionText, Date startDate, Date endDate) {
		this.description = new Description(descriptionText);
		this.startDate = startDate;
		this.endDate = endDate;
	}
	

	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public EventTask copy() {
		String newDescription = this.description.getContent();
		Date newStartDate = new Date(this.startDate.getTime());
		Date newEndDate = new Date(this.endDate.getTime());
		EventTask newTask = new EventTask(newDescription, newStartDate, newEndDate); 
		if (this.isFavorite()) {
			newTask.setAsFavorite();
		} else {
			newTask.setAsNotFavorite();
		}
		return newTask;
	}
	
	@Override
	public String toString() {
		return String.format("[Event Task][Description: %s][Start date: %s][End date: %s]", 
				description, dateFormat.format(startDate), dateFormat.format(endDate));
	}
}