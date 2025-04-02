package managerTest.abstractManagersTest;

import manager.interfaces.PrioritizedManager;
import org.junit.jupiter.api.BeforeEach;

public abstract class PrioritizedManagerTest<T extends PrioritizedManager> {

    PrioritizedManager manager;

    public abstract T createPrioritizedManager();

    @BeforeEach
    public void setupManager() {
        manager = createPrioritizedManager();
    }

}
