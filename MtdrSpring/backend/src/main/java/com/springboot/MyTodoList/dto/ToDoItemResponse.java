package com.springboot.MyTodoList.dto;

import com.springboot.MyTodoList.model.ToDoItem;

import java.time.OffsetDateTime;

public class ToDoItemResponse {

    private int id;
    private String description;
    private OffsetDateTime creationTs;
    private boolean done;

    public ToDoItemResponse() {}

    public static ToDoItemResponse fromEntity(ToDoItem entity) {
        ToDoItemResponse dto = new ToDoItemResponse();
        dto.setId(entity.getID());
        dto.setDescription(entity.getDescription());
        dto.setCreationTs(entity.getCreation_ts());
        dto.setDone(entity.isDone());
        return dto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreationTs() {
        return creationTs;
    }

    public void setCreationTs(OffsetDateTime creationTs) {
        this.creationTs = creationTs;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
