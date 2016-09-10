/*
 * Created by Iliiazbek Akhmedov on 9/7/2016
 * Copyright (c) 2016.
 */

package codepath.todoapp.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ToDoItem extends RealmObject {

    public enum Priority {
        HIGH,
        MEDIUM,
        LOW
    }

    public enum Status {
        TODO,
        DONE
    }

    @PrimaryKey
    private Long taskId;

    private String taskName;

    private Date dueDate;

    private String priorityLevel;

    private String notes;

    private String status;

    public ToDoItem() {
    }

    public ToDoItem(String taskName, Date dueDate, Priority priorityLevel, String notes, Status status) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.priorityLevel = priorityLevel.toString();
        this.notes = notes;
        this.status = status.toString();
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Priority getPriorityLevel() {
        return Priority.valueOf(priorityLevel);
    }

    public void setPriorityLevel(Priority priorityLevel) {
        this.priorityLevel = priorityLevel.toString();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Status getStatus() {
        return Status.valueOf(status);
    }

    public void setStatus(Status status) {
        this.status = status.toString();
    }
}
