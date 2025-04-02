package managerTest;

import manager.FileBackedTasksManager;
import managerTest.abstractManagersTest.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    public FileBackedTasksManager createTaskManager() {
        File file1 = new File("saveFile1.txt");
        return new FileBackedTasksManager(file1);
    }
}
