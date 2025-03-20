package manager;

import exceptions.ManagerSaveException;
import manager.enums.StatusTask;
import manager.enums.TypeTask;
import manager.interfaces.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File saveFile;
    private String description;

    public FileBackedTasksManager(File saveFile) {
        super();
        this.saveFile = saveFile;
    }

    public String toString(Task task) {
        TypeTask typeTask = task.getType();

        String result;
        switch (typeTask) {
            case SUBTASK:
                result = toStringSubtask((Subtask) task);
                break;
            case EPIC:
                result = toStringEpic((Epic) task);
                break;
            default://Значит Task
                result = toStringTask(task);
        }
        return result;
    }

    public String toStringTask(Task task) {
        String result;
        int id = task.getId();
        String type = TypeTask.TASK.toString();
        String name = task.getName();
        String status = task.getStatus().toString();
        String description = task.getDescription();
        String startTime = task.getStartTimeToString();
        long duration = task.getDurationToLong();
        result = String.format("%d,%s,%s,%s,%s,%s,%d\n", id, type, name, status, description, startTime, duration);
        return result;
    }

    public String toStringEpic(Epic epic) {
        String result;
        int id = epic.getId();
        String type = TypeTask.EPIC.toString();
        String name = epic.getName();
        String status = epic.getStatus().toString();
        String description = epic.getDescription();
        String startTime = epic.getStartTimeToString();
        long duration = epic.getDurationToLong();
        result = String.format("%d,%s,%s,%s,%s,%s,%d\n", id, type, name, status, description, startTime, duration);
        return result;
    }

    public String toStringSubtask(Subtask subtask) {
        String result;
        int id = subtask.getId();
        String type = TypeTask.SUBTASK.toString();
        String name = subtask.getName();
        String status = subtask.getStatus().toString();
        String description = subtask.getDescription();
        int idEpic = subtask.getIdEpic();
        String startTime = subtask.getStartTimeToString();
        long duration = subtask.getDurationToLong();
        result = String.format("%d,%s,%s,%s,%s,%s,%d,%d\n", id, type, name, status, description, startTime, duration, idEpic);
        return result;
    }

    public Task fromString(String value) {

        String[] taskComposition = value.split(",");
        int id = Integer.parseInt(taskComposition[0]);
        TypeTask typeTask = TypeTask.valueOf(taskComposition[1]);
        String name = taskComposition[2];
        StatusTask status = StatusTask.valueOf(taskComposition[3]);
        String description = taskComposition[4];
        String startTime = taskComposition[5];
        long duration = Long.parseLong(taskComposition[6]);

        Task restoredTask;

        switch (typeTask) {
            case EPIC:
                restoredTask = new Epic(name, description, id, status, typeTask);
                break;
            case SUBTASK:
                int idEpic = Integer.parseInt(taskComposition[7]);
                restoredTask = new Subtask(name, description, id, status, idEpic, typeTask, startTime, duration);
                break;
            default://Значит Task
                restoredTask = new Task(name, description, id, status, typeTask, startTime, duration);
                break;
        }
        return restoredTask;
    }

    static String historyToString(HistoryManager manager) {
        if (manager.getTasksHistoryInMap().isEmpty()) {
            return "";
        }

        StringBuilder resultBuilder = new StringBuilder();
        ArrayList<Task> history = manager.getListHistory();
        for (Task task : history) {
            int id = task.getId();
            resultBuilder.append(id).append(",");
        }
        resultBuilder.deleteCharAt(resultBuilder.length() - 1);
        return resultBuilder.toString();
    }

    static List<Integer> historyFromString(String value) {
        String[] taskIdInHistory = value.split(",");

        List<Integer> history = new ArrayList<>();
        for (String s : taskIdInHistory) {
            history.add(Integer.parseInt(s));
        }

        return history;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            writer.write("id,type,name,status,description,startTime,duration,epic,\n");

            ArrayList<Task> allTasksEpicSubtask = getAllTasksEpicSubtask();

            if (allTasksEpicSubtask.isEmpty()) {
                System.out.println("Не добавлено ни одной задачи в приложение");
                return;
            }

            for (Task task : allTasksEpicSubtask) {
                String taskInCsvFormat = toString(task);
                writer.write(taskInCsvFormat);
            }
            writer.write("\n");
            writer.write(historyToString(getHistoryManager()));

        } catch (IOException e) {
            System.out.println("Ошибка при сохранении новых данных в файл");
            throw new ManagerSaveException();
        }
    }

    public ArrayList<Task> getAllTasksEpicSubtask() {
        ArrayList<Task> allTasksEpicSubtask = new ArrayList<>();
        allTasksEpicSubtask.addAll(getListTasks());
        allTasksEpicSubtask.addAll(getListEpics());
        allTasksEpicSubtask.addAll(getListSubtasks());
        return allTasksEpicSubtask;
    }

    public static FileBackedTasksManager loadFromFile(File readerFile, File newWriterFile) {
        FileBackedTasksManager manager;
        ArrayList<String> readTasksFromFile = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(readerFile))) {

            while (reader.ready()) {
                readTasksFromFile.add(reader.readLine());
            }
            manager = new FileBackedTasksManager(newWriterFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 1; i < readTasksFromFile.size(); i++) {
            String taskCsvFormat = readTasksFromFile.get(i);
            if (!taskCsvFormat.isBlank()) {
                Task restoredTaskToAdd = manager.fromString(taskCsvFormat);
                TypeTask typeTask = restoredTaskToAdd.getType();
                switch (typeTask) {
                    case EPIC:
                        manager.addEpic((Epic) restoredTaskToAdd);
                        break;
                    case SUBTASK:
                        manager.addSubtask((Subtask) restoredTaskToAdd);
                        break;
                    default:
                        manager.addTask(restoredTaskToAdd);
                }
            } else {
                i++;
                List<Integer> history = historyFromString(readTasksFromFile.get(i));
                manager.addHistory(history, manager);
            }
        }
        return manager;
    }

    private void addHistory(List<Integer> history, FileBackedTasksManager manager) {
        HashMap<Integer, Task> tasks = manager.getMapTasks();
        HashMap<Integer, Epic> epics = manager.getMapEpics();
        HashMap<Integer, Subtask> subtasks = manager.getMapSubtasks();

        for (Integer id : history) {
            if (tasks.containsKey(id)) {
                manager.getTask(id);
            } else if (epics.containsKey(id)) {
                manager.getEpic(id);
            } else if (subtasks.containsKey(id)) {
                manager.getSubtask(id);
            }
        }
    }

    @Override
    public Optional<Task> getTask(int id) {
        Optional<Task> result = super.getTask(id);
        save();
        return result;
    }

    @Override
    public Optional<Epic> getEpic(int id) {
        Optional<Epic> result = super.getEpic(id);
        save();
        return result;
    }

    @Override
    public Optional<Subtask> getSubtask(int id) {
        Optional<Subtask> result = super.getSubtask(id);
        save();
        return result;
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void addTask(Task taskInput) {
        super.addTask(taskInput);
        save();
    }

    @Override
    public void addEpic(Epic taskInput) {
        super.addEpic(taskInput);
        save();
    }

    @Override
    public void addSubtask(Subtask subtaskInput) {
        super.addSubtask(subtaskInput);
        save();
    }

    @Override
    public void updateTask(Task taskInput) {
        super.updateTask(taskInput);
        save();
    }

    @Override
    public void updateEpic(Epic epicInput) {
        super.updateEpic(epicInput);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtaskInput) {
        super.updateSubtask(subtaskInput);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }
}
