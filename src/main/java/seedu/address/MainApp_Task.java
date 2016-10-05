package seedu.address;

import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.address.commons.collections.UniqueItemCollection;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.TaskConfig;
import seedu.address.commons.core.Version;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.TaskConfigUtil;
import seedu.address.logic.Logic_Task;
import seedu.address.logic.LogicManager_Task;
import seedu.address.model.*;
import seedu.address.model.task.InMemoryTaskList;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskManager;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.task.TaskStorage;
import seedu.address.storage.task.TaskStorageManager;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager_Task;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * The main entry point to the application.
 */
public class MainApp_Task extends Application {
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    public static final Version VERSION = new Version(1, 0, 0, true);

    protected Ui ui;
    protected Logic_Task logic;
    protected TaskStorage storage;
    protected InMemoryTaskList model;
    protected TaskConfig config;
    protected UserPrefs userPrefs;

    public MainApp_Task() {}

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing AddressBook ]===========================");
        super.init();

        config = initConfig(getApplicationParameter("config"));
        storage = new TaskStorageManager(config.getTasksFilePath(), config.getUserPrefsFilePath());

        userPrefs = initPrefs(config);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager_Task(model, storage);

        ui = new UiManager_Task(logic, config, userPrefs);

        initEventsCenter();
    }

    private String getApplicationParameter(String parameterName){
        Map<String, String> applicationParameters = getParameters().getNamed();
        return applicationParameters.get(parameterName);
    }

    private InMemoryTaskList initModelManager(TaskStorage storage, UserPrefs userPrefs) {
        Optional<UniqueItemCollection<Task>> tasks;
        UniqueItemCollection<Task> initialData;
        try {
            tasks = storage.readTaskManager();
            if(!tasks.isPresent()){
                logger.info("Data file not found. Will be starting with an empty TaskManager");
                initialData = new UniqueItemCollection<Task>();
            } else {
            	initialData = tasks.get();
            } 
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty AddressBook");
            initialData = new UniqueItemCollection<Task>();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. . Will be starting with an empty AddressBook");
            initialData = new UniqueItemCollection<Task>();
        }

        // TODO: Actually pass in data to use
        return new TaskManager(initialData, userPrefs);
    }

    private void initLogging(TaskConfig config) {
        LogsCenter.init(config);
    }

    protected TaskConfig initConfig(String configFilePath) {
        TaskConfig initializedConfig;
        String configFilePathUsed;

        configFilePathUsed = TaskConfig.DEFAULT_CONFIG_FILE;

        if(configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<TaskConfig> configOptional = TaskConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new TaskConfig());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. " +
                    "Using default config properties");
            initializedConfig = new TaskConfig();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            TaskConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    protected UserPrefs initPrefs(TaskConfig config) {
        assert config != null;

        String prefsFilePath = config.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. " +
                    "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. . Will be starting with an empty AddressBook");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting AddressBook " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Address Book ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}