package manager;

import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTasksManager(File saveFile) {
        return new FileBackedTasksManager(saveFile);
    }
}
