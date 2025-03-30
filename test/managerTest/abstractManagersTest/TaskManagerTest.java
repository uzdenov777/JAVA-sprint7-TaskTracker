package managerTest.abstractManagersTest;

import manager.enums.StatusTask;
import manager.enums.TypeTask;
import manager.interfaces.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected TaskManager manager;

    @BeforeEach
    public abstract void createTaskManager();

    @Test
    public void get1And2And3Int() {
        int taskId = manager.getNewId();
        Assertions.assertEquals(1, taskId);

        int subtaskId = manager.getNewId();
        Assertions.assertEquals(2, subtaskId);

        int epicId = manager.getNewId();
        Assertions.assertEquals(3, epicId);
    }

    @Test
    public void getListEmptyHistory() {
        Task task1 = new Task("task", "task1task1", manager.getNewId(), StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        manager.addTask(task1);

        List<Task> history = manager.getHistory();

        Assertions.assertNotNull(history);
        Assertions.assertTrue(history.isEmpty());
    }

    @Test
    public void get3ListSizeHistory() {
        Task task1 = new Task("task", "task1task1", manager.getNewId(), StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Epic epic1 = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);


        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());
        List<Task> history = manager.getHistory();

        Assertions.assertNotNull(history);
        Assertions.assertEquals(3, history.size());

        Assertions.assertTrue(history.contains(task1));
        Assertions.assertTrue(history.contains(epic1));
        Assertions.assertTrue(history.contains(subtask1));
    }

    @Test
    public void notGetTasksThatWereNotRequestedHistory() {
        Task task1 = new Task("task", "task1task1", manager.getNewId(), StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Epic epic1 = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);


        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        manager.getTask(task1.getId());
        List<Task> history = manager.getHistory();

        Assertions.assertNotNull(history);
        Assertions.assertEquals(1, history.size());
        Assertions.assertTrue(history.contains(task1));
        Assertions.assertFalse(history.contains(epic1));
        Assertions.assertFalse(history.contains(subtask1));
    }

    @Test
    public void getListEmptyPrioritizedTasks() {
        List<Task> prioritizedTasks = manager.getPrioritizedTasks();

        Assertions.assertNotNull(prioritizedTasks);
        Assertions.assertTrue(prioritizedTasks.isEmpty());
    }

    @Test
    public void getPrioritizedTasksSize3() {
        Task task1 = new Task("task", "task1task1", manager.getNewId(), StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Epic epic1 = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);


        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        Assertions.assertNotNull(prioritizedTasks);
        Assertions.assertEquals(2, prioritizedTasks.size());
        Assertions.assertTrue(prioritizedTasks.contains(task1));
        Assertions.assertTrue(prioritizedTasks.contains(subtask1));

        Assertions.assertFalse(prioritizedTasks.contains(epic1));
    }

    @Test
    public void returnTwoTaskInPrioritiesThatWasAdded() {
        Task task1 = new Task("task", "task1task1", manager.getNewId(), StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Epic epic1 = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);

        manager.addTask(task1);
        manager.addEpic(epic1);

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        Assertions.assertNotNull(prioritizedTasks);
        Assertions.assertEquals(2, prioritizedTasks.size());
        Assertions.assertTrue(prioritizedTasks.contains(task1));
        Assertions.assertTrue(prioritizedTasks.contains(epic1));//возвращает Эпик потому что нет подзадач в нем

        Assertions.assertFalse(prioritizedTasks.contains(subtask1));
    }

    @Test
    public void getListTasksListEpicsListSubtasksSizeEach0() {
        Task task1 = new Task("task", "task1task1", manager.getNewId(), StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Epic epic1 = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);


        List<Task> listTasks = manager.getListTasks();
        List<Epic> listEpics = manager.getListEpics();
        List<Subtask> listSubtasks = manager.getListSubtasks();

        Assertions.assertTrue(listTasks.isEmpty());
        Assertions.assertTrue(listEpics.isEmpty());
        Assertions.assertTrue(listSubtasks.isEmpty());
    }

    @Test
    public void getListTasksListEpicsListSubtasksSizeEach1() {
        Task task1 = new Task("task", "task1task1", manager.getNewId(), StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Epic epic1 = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);

        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        List<Task> listTasks = manager.getListTasks();
        List<Epic> listEpics = manager.getListEpics();
        List<Subtask> listSubtasks = manager.getListSubtasks();

        Assertions.assertTrue(listTasks.contains(task1));
        Assertions.assertTrue(listEpics.contains(epic1));
        Assertions.assertTrue(listSubtasks.contains(subtask1));

        Assertions.assertEquals(1, listTasks.size());
        Assertions.assertEquals(1, listEpics.size());
        Assertions.assertEquals(1, listSubtasks.size());
    }

    @Test
    public void getMapTasksMapEpicsMapSubtasksSizeEach0() {
        Task task1 = new Task("task", "task1task1", manager.getNewId(), StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Epic epic1 = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);

        Map<Integer ,Task> taskMap = manager.getMapTasks();
        Map<Integer, Epic> epicMap = manager.getMapEpics();
        Map<Integer, Subtask> subtaskMap = manager.getMapSubtasks();

        Assertions.assertTrue(taskMap.isEmpty());
        Assertions.assertTrue(epicMap.isEmpty());
        Assertions.assertTrue(subtaskMap.isEmpty());
    }

    @Test
    public void getMapTasksMapEpicsMapSubtasksSizeEach1() {
        Task task1 = new Task("task", "task1task1", manager.getNewId(), StatusTask.NEW, TypeTask.TASK, "21.03.2025 12:00", 1);
        Epic epic1 = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);

        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        Map<Integer ,Task> taskMap = manager.getMapTasks();
        Map<Integer, Epic> epicMap = manager.getMapEpics();
        Map<Integer, Subtask> subtaskMap = manager.getMapSubtasks();

        Task taskExtracted = taskMap.get(task1.getId());
        Epic epicExtracted = epicMap.get(epic1.getId());
        Subtask extractedSubtask = subtaskMap.get(subtask1.getId());

        Assertions.assertEquals(task1, taskExtracted);
        Assertions.assertEquals(epic1, epicExtracted);
        Assertions.assertEquals(subtask1, extractedSubtask);

        Assertions.assertTrue(taskMap.containsKey(task1.getId()));
        Assertions.assertTrue(epicMap.containsKey(epic1.getId()));
        Assertions.assertTrue(subtaskMap.containsKey(subtask1.getId()));

        Assertions.assertEquals(1, taskMap.size());
        Assertions.assertEquals(1, epicMap.size());
        Assertions.assertEquals(1, subtaskMap.size());
    }

    //остоновился на провекре методов  clearTasks clearEpics clearSubtasks
}
