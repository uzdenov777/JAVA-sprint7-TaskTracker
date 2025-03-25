package manager.interfaces;

import model.Epic;
import model.Subtask;
import model.Task;


import java.util.List;
import java.util.Map;

public interface PrioritizedManager {

    boolean addTaskWithoutIntersection(Task task);

    void removeTaskFromPrioritizedAndNullLists(Task task);

    void removeAllTasksFromPrioritizedAndNullLists(Map<Integer, Task> inputTasksMap);

    void removeAllEpicsFromPrioritizedAndNullLists(Map<Integer, Epic> inputTasksMap);

    void removeAllSubtasksFromPrioritizedAndNullLists(Map<Integer, Subtask> inputTasksMap);

    List<Task> getPrioritizedTasks();
}
