package model;

import manager.enums.StatusTask;
import manager.enums.TypeTask;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Task {
    protected final String name;
    protected final String description;
    protected StatusTask status;
    protected final int id;
    protected TypeTask type;


    protected LocalDateTime startTime;
    protected Duration duration;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description, int id, StatusTask status, TypeTask type, String startTime, long duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = type;
        this.duration = Duration.ofMinutes(duration);
        if (startTime != null) {
            this.startTime = LocalDateTime.parse(startTime, formatter);
        } else {
            this.startTime = null;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TypeTask getType() {
        return type;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getStartTimeToString() {
        return startTime.format(formatter);
    }

    public long getDurationToLong() {
        return duration.toMinutes();
    }

    public int getId() {
        return id;
    }

    public StatusTask getStatus() {
        if (status == null) {
            return null;
        }
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }
}