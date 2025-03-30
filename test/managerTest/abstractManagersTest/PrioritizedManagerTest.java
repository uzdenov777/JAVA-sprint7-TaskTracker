package managerTest.abstractManagersTest;

import manager.interfaces.PrioritizedManager;

public abstract class PrioritizedManagerTest<T extends PrioritizedManager> {

    PrioritizedManager manager;

    public abstract T createPrioritizedManager();
}
