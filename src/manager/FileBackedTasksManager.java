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

    public FileBackedTasksManager(File saveFile) {
        super();
        this.saveFile = saveFile;
    }

    public String toString(Task task) {
        if (task instanceof Subtask) {
          return toStringSubtask((Subtask) task);
        } else if (task instanceof Epic) {
            return toStringEpic((Epic) task);
        } else {
           return toStringTask(task);
        }
    }

    public String toStringTask(Task task) {
        String result;
        int id = task.getId();
        String type = TypeTask.TASK.toString();
        String name = task.getName();
        String status = task.getStatus().toString();
        String description = task.getDescription();
        result = String.format("%d,%s,%s,%s,%s,\n", id, type, name, status, description);
        return result;
    }

    public String toStringEpic(Epic epic) {
        String result;
        int id = epic.getId();
        String type = TypeTask.EPIC.toString();
        String name = epic.getName();
        String status = epic.getStatus().toString();
        String description = epic.getDescription();
        result = String.format("%d,%s,%s,%s,%s,\n", id, type, name, status, description);
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
        result = String.format("%d,%s,%s,%s,%s,%d\n", id, type, name, status, description, idEpic);
        return result;
    }

    public Task fromString(String value) {

        String[] taskComposition = value.split(",");
        int id = Integer.parseInt(taskComposition[0]);
        TypeTask typeTask = TypeTask.valueOf(taskComposition[1]);
        String name = taskComposition[2];
        StatusTask status = StatusTask.valueOf(taskComposition[3]);
        String description = taskComposition[4];


        Task restoredTask = null;

        switch (typeTask) {
            case EPIC:
                restoredTask = new Epic(name, description, id, status);
                break;
            case SUBTASK:
                int idEpic = Integer.parseInt(taskComposition[5]);
                restoredTask = new Subtask(name, description, id, status, idEpic);
                break;
            case TASK:
                restoredTask = new Task(name, description, id, status);
                break;
            default:
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
            writer.write("id,type,name,status,description,epic\n");

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

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager;
        ArrayList<String> readTasksFromFile = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            while (reader.ready()) {
                readTasksFromFile.add(reader.readLine());
            }
            manager = new FileBackedTasksManager(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 1; i < readTasksFromFile.size(); i++) {
            String taskTCsvFormat = readTasksFromFile.get(i);
            if (!taskTCsvFormat.isBlank()) {
                Task restoredTaskToAdd = manager.fromString(taskTCsvFormat);
                if (restoredTaskToAdd instanceof Epic) {
                    manager.addEpic((Epic) restoredTaskToAdd);
                } else if (restoredTaskToAdd instanceof Subtask) {
                    manager.addSubtask((Subtask) restoredTaskToAdd);
                } else {
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
