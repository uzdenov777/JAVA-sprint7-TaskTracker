package managerTest.abstractManagersTest;

import manager.enums.StatusTask;
import manager.enums.TypeTask;
import manager.interfaces.PrioritizedManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class PrioritizedManagerTest<T extends PrioritizedManager> {

    PrioritizedManager manager;

    public abstract T createPrioritizedManager();

    @BeforeEach
    public void setupManager() {
        manager = createPrioritizedManager();
    }

    @Test
    public void returnTrueAddTaskWithoutIntersection_WhenTasksByPriorityEmpty() {
        Task task1 = new Task("task", "task1task1", 1, StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        boolean isAdd = manager.addTaskWithoutIntersection(task1);
        assertTrue(isAdd);

        List<Task> getPrioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(getPrioritizedTasks.contains(task1));
    }

    @Test
    public void returnTrueAddWithoutIntersection_WhenNotIntersection() {
        Task task1 = new Task("task", "task1task1", 1, StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", 3, StatusTask.NEW, 22, TypeTask.SUBTASK, "24.03.2025 12:00", 1);
        boolean isAdd = manager.addTaskWithoutIntersection(task1);
        boolean isSubtaskAdded = manager.addTaskWithoutIntersection(subtask1);

        assertTrue(isAdd);
        assertTrue(isSubtaskAdded);

        List<Task> getPrioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(getPrioritizedTasks.contains(task1));
        assertTrue(getPrioritizedTasks.contains(subtask1));
    }

    @Test
    public void returnTrueAddWithoutIntersection_WhenPossibleBetweenTwoTasksWithoutIntersection() {
        Task task1 = new Task("task", "task1task1", 1, StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", 3, StatusTask.NEW, 22, TypeTask.SUBTASK, "24.03.2025 12:00", 1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", 3, StatusTask.NEW, 23, TypeTask.SUBTASK, "22.03.2025 12:00", 1);
        boolean isAdd = manager.addTaskWithoutIntersection(task1);
        boolean isSubtaskAdded = manager.addTaskWithoutIntersection(subtask1);
        boolean isSubtaskAdded2 = manager.addTaskWithoutIntersection(subtask2);

        assertTrue(isAdd);
        assertTrue(isSubtaskAdded);
        assertTrue(isSubtaskAdded2);

        List<Task> getPrioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(getPrioritizedTasks.contains(task1));
        assertTrue(getPrioritizedTasks.contains(subtask1));
        assertTrue(getPrioritizedTasks.contains(subtask2));
    }

    @Test
    public void returnTrueAddWithoutIntersection_WhenAddNotEmptyEpicButWillNotAddToThePriorityList() {//Удалит Эпик из списков приоритетов когда в него добавлена подзадача
        Task task1 = new Task("task", "task1task1", 1, StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Epic epic1 = new Epic("epic1", "epic1epic1", 2, StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", 3, StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);
        boolean isAdd = manager.addTaskWithoutIntersection(task1);
        boolean isAddEpic = manager.addTaskWithoutIntersection(epic1);

        epic1.addSubtask(subtask1);//это методы вызываются в другом классе
        manager.removeTaskFromPrioritizedAndNullLists(epic1);// симуляция

        boolean isSubtaskAdded = manager.addTaskWithoutIntersection(subtask1);

        assertTrue(isAdd);
        assertTrue(isAddEpic);
        assertTrue(isSubtaskAdded);

        List<Task> getPrioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(getPrioritizedTasks.contains(task1));
        assertTrue(getPrioritizedTasks.contains(subtask1));

        assertFalse(getPrioritizedTasks.contains(epic1));
    }

    @Test
    public void returnTrueAddWithoutIntersection_WhenAddEmptyEpic() {
        Task task1 = new Task("task", "task1task1", 1, StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Epic epic1 = new Epic("epic1", "epic1epic1", 2, StatusTask.NEW, TypeTask.EPIC);
        boolean isAdd = manager.addTaskWithoutIntersection(task1);
        boolean isAddEpic = manager.addTaskWithoutIntersection(epic1);

        assertTrue(isAdd);
        assertTrue(isAddEpic);

        List<Task> getPrioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(getPrioritizedTasks.contains(task1));
        assertTrue(getPrioritizedTasks.contains(epic1));
    }

    @Test
    public void returnFalseAddWithoutIntersection_NewTaskStartedEarlierThanTheEndOfThePreviousTask() {
        Task task1 = new Task("task", "task1task1", 1, StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 30);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", 3, StatusTask.NEW, 22, TypeTask.SUBTASK, "21.03.2025 12:15", 1);
        boolean isAdd = manager.addTaskWithoutIntersection(task1);
        boolean isSubtaskAdded = manager.addTaskWithoutIntersection(subtask1);

        assertTrue(isAdd);
        assertFalse(isSubtaskAdded);

        List<Task> getPrioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(getPrioritizedTasks.contains(task1));
        assertFalse(getPrioritizedTasks.contains(subtask1));
    }

    @Test
    public void returnFalseAddWithoutIntersection_NewTaskEndsLaterThanNextTaskStart() {
        Task task1 = new Task("task", "task1task1", 1, StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 30);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", 3, StatusTask.NEW, 22, TypeTask.SUBTASK, "21.03.2025 11:55", 10);
        boolean isAdd = manager.addTaskWithoutIntersection(task1);
        boolean isSubtaskAdded = manager.addTaskWithoutIntersection(subtask1);

        assertTrue(isAdd);
        assertFalse(isSubtaskAdded);

        List<Task> getPrioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(getPrioritizedTasks.contains(task1));
        assertFalse(getPrioritizedTasks.contains(subtask1));
    }

    @Test
    public void returnFalseAddWithoutIntersection_WhenPossibleBetweenTwoTasksIntersection_NewTaskEndsLaterThanNextTaskStart() {
        Task task1 = new Task("task", "task1task1", 1, StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 15);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", 3, StatusTask.NEW, 22, TypeTask.SUBTASK, "21.03.2025 12:30", 1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", 3, StatusTask.NEW, 23, TypeTask.SUBTASK, "21.03.2025 12:20", 15);
        boolean isAdd = manager.addTaskWithoutIntersection(task1);
        boolean isSubtaskAdded = manager.addTaskWithoutIntersection(subtask1);
        boolean isSubtaskAdded2 = manager.addTaskWithoutIntersection(subtask2);

        assertTrue(isAdd);
        assertTrue(isSubtaskAdded);
        assertFalse(isSubtaskAdded2);

        List<Task> getPrioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(getPrioritizedTasks.contains(task1));
        assertTrue(getPrioritizedTasks.contains(subtask1));
        assertFalse(getPrioritizedTasks.contains(subtask2));
    }

    @Test
    public void returnFalseAddWithoutIntersection_TheNewTaskStartedEarlierThanTheEndOfThePreviousTask() {
        Task task1 = new Task("task", "task1task1", 1, StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 15);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", 3, StatusTask.NEW, 22, TypeTask.SUBTASK, "21.03.2025 12:30", 1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2", 3, StatusTask.NEW, 23, TypeTask.SUBTASK, "21.03.2025 12:10", 15);
        boolean isAdd = manager.addTaskWithoutIntersection(task1);
        boolean isSubtaskAdded = manager.addTaskWithoutIntersection(subtask1);
        boolean isSubtaskAdded2 = manager.addTaskWithoutIntersection(subtask2);

        assertTrue(isAdd);
        assertTrue(isSubtaskAdded);
        assertFalse(isSubtaskAdded2);

        List<Task> getPrioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(getPrioritizedTasks.contains(task1));
        assertTrue(getPrioritizedTasks.contains(subtask1));
        assertFalse(getPrioritizedTasks.contains(subtask2));
    }


}
