package modelTest;

import manager.Managers;
import manager.enums.StatusTask;
import manager.enums.TypeTask;
import manager.interfaces.TaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class EpicTest {

    TaskManager manager;

    @BeforeEach
    public void setManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void return0SubtasksEmpty() {
        Epic epic = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        manager.addEpic(epic);
        HashMap<Integer, Subtask> subtaskHashMap = epic.getSubtasksMap();
        Assertions.assertEquals(0, subtaskHashMap.size());
    }

    @Test
    public void returnNewStatusEpicAllSubtasksStatusNew() {
        Epic epic = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.NEW, epic.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2subtask2", manager.getNewId(), StatusTask.NEW, epic.getId(), TypeTask.SUBTASK, "20.03.2025 12:00", 30);
        Subtask subtask3 = new Subtask("subtask3", "subtask3subtask3", manager.getNewId(), StatusTask.NEW, epic.getId(), TypeTask.SUBTASK, "27.03.2025 12:00", 30);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        Assertions.assertEquals(StatusTask.NEW, epic.getStatus());
    }

    @Test
    public void returnDoneStatusEpicAllSubtasksStatusDone() {
        Epic epic = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.DONE, epic.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2subtask2", manager.getNewId(), StatusTask.DONE, epic.getId(), TypeTask.SUBTASK, "20.03.2025 12:00", 30);
        Subtask subtask3 = new Subtask("subtask3", "subtask3subtask3", manager.getNewId(), StatusTask.DONE, epic.getId(), TypeTask.SUBTASK, "27.03.2025 12:00", 30);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        Assertions.assertEquals(StatusTask.DONE, epic.getStatus());
    }

    @Test
    public void returnInProgressStatusEpicAllSubtasksStatusNewAndDone() {
        Epic epic = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.NEW, epic.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2subtask2", manager.getNewId(), StatusTask.DONE, epic.getId(), TypeTask.SUBTASK, "20.03.2025 12:00", 30);
        Subtask subtask3 = new Subtask("subtask3", "subtask3subtask3", manager.getNewId(), StatusTask.DONE, epic.getId(), TypeTask.SUBTASK, "27.03.2025 12:00", 30);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void returnInProgressStatusEpicAllSubtasksStatusInProgress() {
        Epic epic = new Epic("epic1", "epic1epic1", manager.getNewId(), StatusTask.NEW, TypeTask.EPIC);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(), StatusTask.IN_PROGRESS, epic.getId(), TypeTask.SUBTASK, "24.03.2025 12:00", 1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2subtask2", manager.getNewId(), StatusTask.IN_PROGRESS, epic.getId(), TypeTask.SUBTASK, "20.03.2025 12:00", 30);
        Subtask subtask3 = new Subtask("subtask3", "subtask3subtask3", manager.getNewId(), StatusTask.IN_PROGRESS, epic.getId(), TypeTask.SUBTASK, "27.03.2025 12:00", 30);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        Assertions.assertEquals(StatusTask.IN_PROGRESS, epic.getStatus());
    }
}