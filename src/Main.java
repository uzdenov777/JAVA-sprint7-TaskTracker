import manager.FileBackedTasksManager;
import manager.Managers;
import manager.enums.TypeTask;
import manager.interfaces.TaskManager;
import model.Epic;
import manager.enums.StatusTask;
import model.Subtask;
import model.Task;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file1 = new File("saveFile1.txt");
        TaskManager manager = Managers.getFileBackedTasksManager(file1);
        Task task1 = new Task("task1", "task1task1", manager.getNewId(), StatusTask.NEW, TypeTask.TASK, "20.03.2025 12:00", 30);
        Task task2 = new Task("task2", "task2task2", manager.getNewId(), StatusTask.NEW, TypeTask.TASK, "20.03.2025 12:00", 30);
        Epic epic1 = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Epic epic2 = new Epic("epic2", "epic2epic2", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "20.03.2025 12:00", 30);
        Subtask subtask2 = new Subtask("subtask2", "subtask2subtask2", manager.getNewId(), StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "20.03.2025 12:00", 30);
        Subtask subtask3 = new Subtask("subtask3", "subtask3subtask3", manager.getNewId(), StatusTask.NEW, epic1.getId(), TypeTask.SUBTASK, "20.03.2025 12:00", 30);
        Subtask subtask4 = new Subtask("subtask4", "subtask4subtask4", manager.getNewId(), StatusTask.NEW, epic2.getId(), TypeTask.SUBTASK, "20.03.2025 12:00", 30);
        manager.addTask(task1);
        manager.addTask(task2);

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);


        manager.getEpic(epic1.getId());//
        manager.getEpic(epic2.getId());//1
        manager.getSubtask(subtask1.getId());//
        manager.getSubtask(subtask2.getId());//2
        manager.getSubtask(subtask3.getId());//3
        manager.getEpic(epic1.getId());//
        manager.getEpic(epic1.getId());//
        manager.getEpic(epic1.getId());//
        manager.getEpic(epic1.getId());//4
        manager.getSubtask(subtask1.getId());//5
        manager.getTask(task1.getId());//6
        manager.getTask(task2.getId());//7
        manager.getSubtask(subtask4.getId());//8
        File file2 = new File("saveFile2.txt");
        TaskManager manager1 = FileBackedTasksManager.loadFromFile(file1, file2);

        boolean task = manager.getListTasks().equals(manager1.getListTasks());
        int taskListSize1 = manager.getListTasks().size();
        int taskListSize2 = manager1.getListTasks().size();

        boolean epic = manager.getListEpics().equals(manager1.getListEpics());
        int epicListSize1 = manager.getListEpics().size();
        int epicListSize2 = manager1.getListEpics().size();

        boolean subtask = manager.getListSubtasks().equals(manager1.getListSubtasks());
        int subtaskListSize1 = manager.getListSubtasks().size();
        int subtaskListSize2 = manager1.getListSubtasks().size();

        boolean history = manager.getHistory().equals(manager1.getHistory());
        int historyListSize1 = manager.getHistory().size();
        int historyListSize2 = manager1.getHistory().size();

        System.out.println(task);
        System.out.println(taskListSize1);
        System.out.println(taskListSize2);
        System.out.println("");
        System.out.println(epic);
        System.out.println(epicListSize1);
        System.out.println(epicListSize2);
        System.out.println("");
        System.out.println(subtask);
        System.out.println(subtaskListSize1);
        System.out.println(subtaskListSize2);
        System.out.println("");
        System.out.println(history);
        System.out.println(historyListSize1);
        System.out.println(historyListSize2);

    }
}