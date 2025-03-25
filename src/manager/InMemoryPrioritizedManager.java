package manager;

import manager.interfaces.PrioritizedManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class InMemoryPrioritizedManager implements PrioritizedManager {
    private final TreeMap<LocalDateTime, Task> prioritizedTasksNotNUll = new TreeMap<>();
    private final List<Task> nullDateTasks = new ArrayList<>();

    public boolean addTaskWithoutIntersection(Task task) {
        LocalDateTime startOutTask = task.getStartTime();
        LocalDateTime endOutTask = task.getEndTime();


        if (startOutTask == null) {//Null может быть только у Epic-a, у которого нет подзадач, такие Epic-и храним в отдельном списке.
            nullDateTasks.add(task);
            return true;
        }

        if (prioritizedTasksNotNUll.isEmpty()) { //Если не добавлено еще ни одной задачи можно ничего не проверять и вставлять
            prioritizedTasksNotNUll.put(startOutTask, task);
            return true;
        }

        // Проверка на пересечение с уже существующей задачей (по времени)
        if (prioritizedTasksNotNUll.containsKey(startOutTask)) {
            System.out.println("Дата начала задачи уже занята!");
            return false;
        }

        // Проверка, если задача должна быть вставлена перед самой первой задачей
        LocalDateTime before = prioritizedTasksNotNUll.lowerKey(startOutTask);
        if (before == null) {
            LocalDateTime first = prioritizedTasksNotNUll.firstKey();
            if (endOutTask.isBefore(first)) {
                prioritizedTasksNotNUll.put(startOutTask, task);
                return true;
            }
        }

        Task previousTask = prioritizedTasksNotNUll.get(before);
        LocalDateTime previousTaskEndTime = previousTask.getEndTime();

        // Если задача начинается после окончания предыдущей
        if (startOutTask.isAfter(previousTaskEndTime)) {
            LocalDateTime nextTaskStartTime = prioritizedTasksNotNUll.higherKey(startOutTask);
            // Если следующей задачи нет
            if (nextTaskStartTime == null) {
                prioritizedTasksNotNUll.put(startOutTask, task);
                return true;
            }

            //Задача заканчивается до начала следующей задачи
            if (endOutTask.isBefore(nextTaskStartTime)) {
                prioritizedTasksNotNUll.put(startOutTask, task);
                return true;
            } else {
                System.out.println("Задача не закончена вовремя.");
                return false;
            }
        } else {
            System.out.println("Задача начинается до окончания предыдущей задачи.");
            return false;
        }
    }

    public void removeTaskFromPrioritizedAndNullLists(Task task) {
        LocalDateTime startOutTask = task.getStartTime();

        if (startOutTask == null) { // Скорее всего это Epic, у которого нет подзадач и поэтому находиться в nullDateTasks.
            nullDateTasks.remove(task);
        } else {
            prioritizedTasksNotNUll.remove(startOutTask);
        }
    }

    public void removeAllTasksFromPrioritizedAndNullLists(Map<Integer, Task> inputTasksMap) {
        for (Task task : inputTasksMap.values()) {
            LocalDateTime startOutTask = task.getStartTime();
            prioritizedTasksNotNUll.remove(startOutTask);
        }
    }

    public void removeAllEpicsFromPrioritizedAndNullLists(Map<Integer, Epic> inputTasksMap) {
        for (Epic epic : inputTasksMap.values()) {
            LocalDateTime startOutTask = epic.getStartTime();
            if (startOutTask == null) { // Скорее всего это Epic, у которого нет подзадач и поэтому находиться в nullDateTasks.
                nullDateTasks.remove(epic);
            }
        }
    }

    public void removeAllSubtasksFromPrioritizedAndNullLists(Map<Integer, Subtask> inputTasksMap) {
        for (Subtask subtask : inputTasksMap.values()) {
            LocalDateTime startOutTask = subtask.getStartTime();
            prioritizedTasksNotNUll.remove(startOutTask);
        }
    }

    public List<Task> getPrioritizedTasks() {
        List<Task> prioritizedTasks = new ArrayList<>(prioritizedTasksNotNUll.values());
        prioritizedTasks.addAll(nullDateTasks);
        return prioritizedTasks;
    }
}
