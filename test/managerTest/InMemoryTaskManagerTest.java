package managerTest;


import manager.InMemoryTaskManager;
import managerTest.abstractManagersTest.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void createTaskManager() {
        this.manager = new InMemoryTaskManager();
    }
}
