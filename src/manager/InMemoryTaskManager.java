package manager;

import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import model.Epic;
import manager.enums.StatusTask;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>(); //Хранит задачи.
    private final HashMap<Integer, Epic> epics = new HashMap<>();//Хранит Epic.
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();//Хранит подзадачи.
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int countId = 0;


    @Override
    public int getNewId() {//Генерирует уникальный ID.
        countId++;
        return countId;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getListHistory();
    }

    @Override
    public List<Task> getListTasks() { //Получение списка всех задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getListEpics() { //Получение списка всех Epic
        return new ArrayList<>(epics.values());
    }

    @Override
    public HashMap<Integer, Task> getMapTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getMapEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> getMapSubtasks() {
        return subtasks;
    }

    @Override
    public List<Subtask> getListSubtasks() { //Получение списка всех подзадач
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearTasks() { //Удаление всех задач.
        historyManager.removeTaskAll(tasks);
        tasks.clear();
    }

    @Override
    public void clearEpics() { //Удаление всех Epic.
        historyManager.removeEpicAll(epics);
        historyManager.removeSubtaskAll(subtasks);

        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() { //Удаление всех подзадач.
        historyManager.removeSubtaskAll(subtasks);

        for (Epic epic : epics.values()) {
            HashMap<Integer, Subtask> mapDelete = epic.getSubtasksArray();
            mapDelete.clear();

            int epicId = epic.getId();
            StatusTask.checkStatus(epicId, epics);
        }
        subtasks.clear();
    }

    @Override
    public Optional<Task> getTask(int id) { //Получение Task по идентификатору.

        if (tasks.containsKey(id)) { //Проверяет если такой ID в задачах.
            Task task = tasks.get(id);
            historyManager.add(task);
        } else {
            System.out.println("Не существует такого ID Task");
        }
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public Optional<Epic> getEpic(int id) { //Получение Epic по идентификатору.

        if (epics.containsKey(id)) { //Проверяет если такой ID в Epic.
            Epic epic = epics.get(id);
            historyManager.add(epic);
        } else {
            System.out.println("Не существует такого ID Epic");
        }
        return Optional.ofNullable(epics.get(id));
    }

    @Override
    public Optional<Subtask> getSubtask(int id) { //Получение Subtask по идентификатору.

        if (subtasks.containsKey(id)) { //Проверяет если такой ID в подзадачах.
            Subtask subtask = subtasks.get(id);
            historyManager.add(subtask);
        } else {
            System.out.println("Не существует такого ID Subtask");
        }
        return Optional.ofNullable(subtasks.get(id));
    }

    @Override
    public void addTask(Task taskInput) { //Добавляет полученный объект Task в соответсвующий HashMap и проверяет, если такой ID уже.
        int idTaskInput = taskInput.getId();
        boolean isTaskExist = tasks.containsKey(idTaskInput);// Проверяет на наличие задачи.

        if (isTaskExist) {
            System.out.println("Задача с таким ID уже создана");
        } else {
            tasks.put(idTaskInput, taskInput);
        }
    }

    @Override
    public void addEpic(Epic taskInput) { //Добавляет полученный объект Epic в соответсвующий HashMap и проверяет, если такой ID уже.
        int idEpicInput = taskInput.getId();
        boolean isEpicExist = epics.containsKey(idEpicInput);//Проверяет на наличие Epic.

        if (isEpicExist) {
            System.out.println("Epic с таким ID уже создан");
        } else {
            epics.put(idEpicInput, taskInput);
        }
    }

    @Override
    public void addSubtask(Subtask subtaskInput) { //Добавляет полученный объект Subtask в соответсвующий HashMap и проверяет, если такой ID уже.
        int idSubtaskInput = subtaskInput.getId();
        boolean isSubtaskExist = subtasks.containsKey(idSubtaskInput);

        if (isSubtaskExist) {
            System.out.println("Подзадача с таким ID уже создана");
            return;
        }

        int idEpic = subtaskInput.getIdEpic();
        boolean isEpicExist = epics.containsKey(idEpic);//если есть Epic, к которому подзадача принадлежит.

        if (isEpicExist) {
            subtasks.put(idSubtaskInput, subtaskInput);
            epics.get(idEpic).
                    addSubtask(subtaskInput); //Добавляет подзадачу в список определенного Epic.
            StatusTask.checkStatus(idEpic, epics); //Проверяет статус Epic после добавления в него подзадачи.
        } else {
            System.out.println("Такого Epic не существует для добавления в него подзадачи");
        }
    }


    @Override
    public void updateTask(Task taskInput) { // Обновление Task. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        int idTaskInput = taskInput.getId();
        boolean isTaskExist = tasks.containsKey(idTaskInput);

        if (isTaskExist) {
            tasks.put(idTaskInput, taskInput);
        } else {
            System.out.println("Такой Задачи не существует для обновления");
        }
    }

    @Override
    public void updateEpic(Epic epicInput) { // Обновление Epic. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        int idEpicInput = epicInput.getId();
        boolean isEpicExist = epics.containsKey(idEpicInput);

        if (isEpicExist) {
            epics.put(idEpicInput, epicInput);
            HashMap<Integer, Subtask> epicInputSubtasksMap = epicInput.getSubtasksArray();
            for (Subtask subtask : epicInputSubtasksMap.values()) {//Обновляю список всех существующих подзадач, после обновления Epic.
                subtasks.put(idEpicInput, subtask);
            }

        } else {
            System.out.println("Такого Epic не существует для обновления");
        }
    }

    @Override
    public void updateSubtask(Subtask subtaskInput) { // Обновление Subtask. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        int idSubtaskInput = subtaskInput.getId();
        boolean isSubtaskExist = subtasks.containsKey(idSubtaskInput);

        if (isSubtaskExist) {
            subtasks.put(idSubtaskInput, subtaskInput);
            int idEpic = subtaskInput.getIdEpic();
            Epic epicSubtaskInput = epics.get(idEpic);
            epicSubtaskInput.addSubtask(subtaskInput); //Добавляет подзадачу в список Epic.
            StatusTask.checkStatus(idEpic, epics); //Проверяет статус Epic после обновления его подзадачи.
        } else {
            System.out.println("Такой подзадачи не существует для обновления");
        }
    }

    @Override
    public void removeTaskById(int id) { //Удаление Task по идентификатору.
        historyManager.removeById(id);//удаляет Task по ID в истории задач
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) { //Удаление Epic по идентификатору и его подзадач.
        historyManager.removeById(id);//удаляет Epic по ID в истории задач

        Epic epicDelete = epics.get(id);
        HashMap<Integer, Subtask> subtasksEpicMap = epicDelete.getSubtasksArray();
        for (Subtask subtask : subtasksEpicMap.values()) {//удаляет подзадачи Epic-а по ID в истории задач
            int subtaskId = subtask.getId();
            historyManager.removeById(subtaskId);
        }

        epics.remove(id);
        for (Subtask subtask : subtasks.values()) {
            int idEpic = subtask.getIdEpic();
            if (idEpic == id) {
                subtasks.remove(id);
            }
        }

    }

    @Override
    public void removeSubtaskById(int id) { //Удаление Subtask по идентификатору.
        Subtask subtaskDelete = subtasks.get(id);
        int idEpicSubtask = subtaskDelete.getIdEpic(); //сохраняет ID Epic пока не удалил Subtask.
        Epic epicSubtaskDelete = epics.get(idEpicSubtask);
        HashMap<Integer, Subtask> subtaskHashMapEpic = epicSubtaskDelete.getSubtasksArray();
        subtaskHashMapEpic.remove(id);//Удаление Subtask по идентификатору в самом Epic.
        subtasks.remove(id);
        StatusTask.checkStatus(idEpicSubtask, epics);

        historyManager.removeById(id);//удаляет подзадачу по ID в истории задач
    }

    @Override
    public HashMap<Integer, Subtask> getListSubtasks(int id) { //Получение списка всех подзадач определённого Epic.

        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            return epic.getSubtasksArray();
        }

        System.out.println("Такого Epic нету");
        return new HashMap<>();
    }


    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}