package model;

import manager.enums.StatusTask;
import manager.enums.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();

    public Epic(String name, String description, int id, StatusTask status, TypeTask typeTask) {
        super(name, description, id, status, typeTask, null, 0);
    }

    @Override
    public LocalDateTime getStartTime() {
        startTimeEpic(subtaskHashMap);
        return super.getStartTime();
    }

    @Override
    public LocalDateTime getEndTime() {
        startTimeEpic(subtaskHashMap);
        durationEpic(subtaskHashMap);
        return super.getEndTime();
    }

    @Override
    public Duration getDuration() {
        durationEpic(subtaskHashMap);
        return super.getDuration();
    }

    @Override
    public String getStartTimeToString() {
        startTimeEpic(subtaskHashMap);
        return super.getStartTimeToString();
    }

    @Override
    public String getEndTimeToString() {
        startTimeEpic(subtaskHashMap);
        durationEpic(subtaskHashMap);
        return super.getEndTimeToString();
    }

    @Override
    public long getDurationToLong() {
        durationEpic(subtaskHashMap);
        return super.getDurationToLong();
    }

    private void startTimeEpic(HashMap<Integer, Subtask> subtaskHashMap) {
        LocalDateTime startTime = null;
        for (HashMap.Entry<Integer, Subtask> entry : subtaskHashMap.entrySet()) {
            Subtask subtask = entry.getValue();
            if (startTime == null) {
                startTime = subtask.getStartTime();
            }
            boolean isAfter = startTime.isAfter(subtask.getStartTime());
            if (isAfter) {
                startTime = subtask.getStartTime();
            }
        }
        this.startTime = startTime;
    }

    private void durationEpic(HashMap<Integer, Subtask> subtaskHashMap) {
        Duration durationResult = Duration.ZERO;
        for (HashMap.Entry<Integer, Subtask> entry : subtaskHashMap.entrySet()) {
            Subtask subtask = entry.getValue();
            durationResult = durationResult.plus(subtask.getDuration());
        }
        this.duration = durationResult;
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
