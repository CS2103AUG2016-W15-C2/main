package w15c2.tusk.storage.task;

import com.google.common.eventbus.Subscribe;

import w15c2.tusk.commons.collections.UniqueItemCollection;
import w15c2.tusk.commons.core.ComponentManager;
import w15c2.tusk.commons.core.LogsCenter;
import w15c2.tusk.commons.events.model.AliasChangedEvent;
import w15c2.tusk.commons.events.model.NewTaskListEvent;
import w15c2.tusk.commons.events.model.TaskManagerChangedEvent;
import w15c2.tusk.commons.events.storage.DataSavingExceptionEvent;
import w15c2.tusk.commons.exceptions.DataConversionException;
import w15c2.tusk.model.Alias;
import w15c2.tusk.model.UserPrefs;
import w15c2.tusk.model.task.Task;
import w15c2.tusk.storage.JsonUserPrefsStorage;
import w15c2.tusk.storage.UserPrefsStorage;
import w15c2.tusk.storage.alias.AliasStorage;
import w15c2.tusk.storage.alias.XmlAliasStorage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages storage of TaskManager data in local storage.
 */
//@@author A0143107U
public class TaskStorageManager extends ComponentManager implements TaskStorage {

    private static final Logger logger = LogsCenter.getLogger(TaskStorageManager.class);
    private TaskManagerStorage taskManagerStorage;
    private AliasStorage aliasStorage;
    private UserPrefsStorage userPrefsStorage;


    public TaskStorageManager(TaskManagerStorage taskManagerStorage, AliasStorage aliasStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.taskManagerStorage = taskManagerStorage;
        this.aliasStorage = aliasStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public TaskStorageManager(String taskManagerFilePath, String aliasFilePath, String userPrefsFilePath) {
        this(new XmlTaskManagerStorage(taskManagerFilePath), new XmlAliasStorage(aliasFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ TaskManager methods ==============================

    @Override
    public String getTaskManagerFilePath() {
        return taskManagerStorage.getTaskManagerFilePath();
    }

    @Override
    public Optional<UniqueItemCollection<Task>> readTaskManager() throws DataConversionException, IOException {
        return readTaskManager(taskManagerStorage.getTaskManagerFilePath());
    }

    @Override
    public Optional<UniqueItemCollection<Task>> readTaskManager(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskManagerStorage.readTaskManager(filePath);
    }

    @Override
    public void saveTaskManager(UniqueItemCollection<Task> taskManager) throws IOException {
        saveTaskManager(taskManager, taskManagerStorage.getTaskManagerFilePath());
    }

    @Override
    public void saveTaskManager(UniqueItemCollection<Task> taskManager, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskManagerStorage.saveTaskManager(taskManager, filePath);
    }


    @Override
    @Subscribe
    public void handleTaskManagerChangedEvent(TaskManagerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskManager(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
    
    @Subscribe
    public void handleNewTaskListEvent(NewTaskListEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskManager(event.newTasks);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    
 // ================ Alias methods ==============================

    @Override
    public String getAliasFilePath() {
        return aliasStorage.getAliasFilePath();
    }

    @Override
    public Optional<UniqueItemCollection<Alias>> readAlias() throws DataConversionException, IOException {
        return readAlias(aliasStorage.getAliasFilePath());
    }

    @Override
    public Optional<UniqueItemCollection<Alias>> readAlias(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return aliasStorage.readAlias(filePath);
    }

    @Override
    public void saveAlias(UniqueItemCollection<Alias> alias) throws IOException {
        saveAlias(alias, aliasStorage.getAliasFilePath());
    }

    @Override
    public void saveAlias(UniqueItemCollection<Alias> alias, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        aliasStorage.saveAlias(alias, filePath);
    }


    @Override
    @Subscribe
    public void handleAliasChangedEvent(AliasChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveAlias(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
}