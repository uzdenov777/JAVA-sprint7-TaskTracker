package model;

import manager.enums.StatusTask;

import java.util.Objects;

public class Subtask extends Task {
    private int idEpic;

    public Subtask(String name, String description, int id, StatusTask status, int idEpic) {
        super(name, description, id, status);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idEpic == subtask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + idEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
