package manager;

import manager.enums.TypeTask;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import model.Epic;
import manager.enums.StatusTask;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;


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
    public boolean clearTasks() { //Удаление всех задач.
        boolean isNotEmpty = !tasks.isEmpty();
        if (isNotEmpty) {
            historyManager.removeTaskAll(tasks);
            tasks.clear();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean clearEpics() { //Удаление всех Epic.
        boolean isNotEmpty = !epics.isEmpty();
        if (isNotEmpty) {
            historyManager.removeEpicAll(epics);
            historyManager.removeSubtaskAll(subtasks);

            epics.clear();
            subtasks.clear();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean clearSubtasks() { //Удаление всех подзадач.
        boolean isNotEmpty = !subtasks.isEmpty();
        if (isNotEmpty) {
            historyManager.removeSubtaskAll(subtasks);

            for (Epic epic : epics.values()) {
                epic.clearSubtasks();

                int epicId = epic.getId();
                StatusTask.checkStatus(epicId, epics);
            }
            subtasks.clear();
            return true;
        } else {
            return false;
        }
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
    public boolean addTask(Task taskInput) { //Добавляет полученный объект Task в соответсвующий HashMap и проверяет, если такой ID уже.
        int idTaskInput = taskInput.getId();
        boolean isTaskExist = tasks.containsKey(idTaskInput);// Проверяет на наличие задачи.

        if (isTaskExist) {
            System.out.println("Задача с таким ID уже создана");
            return false;
        } else {
            tasks.put(idTaskInput, taskInput);
            return true;
        }
    }

    @Override
    public boolean addEpic(Epic taskInput) { //Добавляет полученный объект Epic в соответсвующий HashMap и проверяет, если такой ID уже.
        int idEpicInput = taskInput.getId();
        boolean isEpicExist = epics.containsKey(idEpicInput);//Проверяет на наличие Epic.

        if (isEpicExist) {
            System.out.println("Epic с таким ID уже создан");
            return false;
        } else {
            epics.put(idEpicInput, taskInput);
            return true;
        }
    }

    @Override
    public boolean addSubtask(Subtask subtaskInput) { //Добавляет полученный объект Subtask в соответсвующий HashMap и проверяет, если такой ID уже.
        int idSubtaskInput = subtaskInput.getId();
        boolean isSubtaskExist = subtasks.containsKey(idSubtaskInput);

        if (isSubtaskExist) {
            System.out.println("Подзадача с таким ID уже создана");
            return false;
        }

        int idEpic = subtaskInput.getIdEpic();
        boolean isEpicExist = epics.containsKey(idEpic);//если есть Epic, к которому подзадача принадлежит.

        if (isEpicExist) {
            subtasks.put(idSubtaskInput, subtaskInput);
            Epic epic = epics.get(idEpic);
            epic.addSubtask(subtaskInput); //Добавляет подзадачу в список определенного Epic.
            StatusTask.checkStatus(idEpic, epics); //Проверяет статус Epic после добавления в него подзадачи.
            return true;
        } else {
            System.out.println("Такого Epic не существует для добавления в него подзадачи");
            return false;
        }
    }

    @Override
    public boolean updateTask(Task taskInput) { // Обновление Task. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        int idTaskInput = taskInput.getId();
        boolean isTaskExist = tasks.containsKey(idTaskInput);

        if (isTaskExist) {
            tasks.put(idTaskInput, taskInput);
            return true;
        } else {
            System.out.println("Такой Задачи не существует для обновления");
            return false;
        }
    }

    @Override
    public boolean updateEpic(Epic epicInput) { // Обновление Epic. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        int idEpicInput = epicInput.getId();
        boolean isEpicExist = epics.containsKey(idEpicInput);

        if (isEpicExist) {
            epics.put(idEpicInput, epicInput);
            HashMap<Integer, Subtask> epicInputSubtasksMap = epicInput.getSubtasksMap();
            for (Subtask subtask : epicInputSubtasksMap.values()) {//Обновляю список всех существующих подзадач, после обновления Epic.
                subtasks.put(idEpicInput, subtask);
            }
            return true;
        } else {
            System.out.println("Такого Epic не существует для обновления");
            return false;
        }
    }

    @Override
    public boolean updateSubtask(Subtask subtaskInput) { // Обновление Subtask. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        int idSubtaskInput = subtaskInput.getId();
        boolean isSubtaskExist = subtasks.containsKey(idSubtaskInput);

        if (isSubtaskExist) {
            subtasks.put(idSubtaskInput, subtaskInput);
            int idEpic = subtaskInput.getIdEpic();
            Epic epicSubtaskInput = epics.get(idEpic);
            epicSubtaskInput.addSubtask(subtaskInput); //Добавляет подзадачу в список Epic.
            StatusTask.checkStatus(idEpic, epics); //Проверяет статус Epic после обновления его подзадачи.
            return true;
        } else {
            System.out.println("Такой подзадачи не существует для обновления");
            return false;
        }
    }

    @Override
    public boolean removeTaskById(int id) { //Удаление Task по идентификатору.
        boolean containsTask = tasks.containsKey(id);
        if (containsTask) {
            historyManager.removeById(id);//удаляет Task по ID в истории задач
            tasks.remove(id);
            return true;
        } else {
            System.out.println("Такой задачи не существует");
            return false;
        }

    }

    @Override
    public boolean removeEpicById(int id) { //Удаление Epic по идентификатору и его подзадач.
        boolean containsEpic = epics.containsKey(id);

        if (containsEpic) {
            historyManager.removeById(id);//удаляет Epic по ID в истории задач

            Epic epicDelete = epics.get(id);
            HashMap<Integer, Subtask> subtasksEpicMap = epicDelete.getSubtasksMap();
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
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeSubtaskById(int id) { //Удаление Subtask по идентификатору.
        boolean containsSubtask = subtasks.containsKey(id);

        if (containsSubtask) {
            Subtask subtaskDelete = subtasks.get(id);
            int idEpicSubtask = subtaskDelete.getIdEpic(); //сохраняет ID Epic пока не удалил Subtask.
            Epic epicSubtaskDelete = epics.get(idEpicSubtask);
            epicSubtaskDelete.removeSubtaskById(id);//Удаление Subtask по идентификатору в самом Epic.
            subtasks.remove(id);
            StatusTask.checkStatus(idEpicSubtask, epics);

            historyManager.removeById(id);//удаляет подзадачу по ID в истории задач
            return true;
        } else {
            return false;
        }
    }

    @Override
    public HashMap<Integer, Subtask> getListSubtasks(int id) { //Получение списка всех подзадач определённого Epic.

        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            return epic.getSubtasksMap();
        }

        System.out.println("Такого Epic нету");
        return new HashMap<>();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

//    public List<Task> getPrioritizedTasks() {
//
//    }

    public List<Task> getValidatedTasks() {
        List<Task> validatedTasks = getAllTasksEpicSubtask();

        for (int i = 0; i < validatedTasks.size(); i++) {
            boolean isTypeEpic = TypeTask.EPIC == validatedTasks.get(i).getType();

            boolean isEpicNotEmptyMapSubtasks = false;
            if (isTypeEpic) {
                Epic epic = (Epic) validatedTasks.get(i);
                isEpicNotEmptyMapSubtasks = !(epic.getSubtasksMap().isEmpty());
            }

            if (isEpicNotEmptyMapSubtasks) {
                validatedTasks.remove(i);
            }
        }
        return validatedTasks;
    }

    public List<Task> getAllTasksEpicSubtask() {
        List<Task> allTasksEpicSubtask = new ArrayList<>();
        allTasksEpicSubtask.addAll(getListTasks());
        allTasksEpicSubtask.addAll(getListEpics());
        allTasksEpicSubtask.addAll(getListSubtasks());
        return allTasksEpicSubtask;
    }
}

