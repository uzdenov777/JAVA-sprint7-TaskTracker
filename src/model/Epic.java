package model;

import manager.enums.StatusTask;

import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();

    public Epic(String name, String description, int id, StatusTask status) {
        super(name, description, id, status);
    }

    public void addSubtask(Subtask subtask) {
        subtaskHashMap.put(subtask.getId(), subtask);
    }

    public HashMap<Integer, Subtask> getSubtasksArray() {
        return subtaskHashMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskHashMap, epic.subtaskHashMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskHashMap);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
