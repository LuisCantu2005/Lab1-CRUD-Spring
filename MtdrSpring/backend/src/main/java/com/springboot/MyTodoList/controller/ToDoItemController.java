package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.dto.ToDoItemRequest;
import com.springboot.MyTodoList.dto.ToDoItemResponse;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.service.ToDoItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
public class ToDoItemController {

    private final ToDoItemService toDoItemService;

    public ToDoItemController(ToDoItemService toDoItemService) {
        this.toDoItemService = toDoItemService;
    }

    // ==================== API REST limpia ====================

    @GetMapping("/api/todos")
    public ResponseEntity<List<ToDoItemResponse>> getAllTodos() {
        List<ToDoItemResponse> items = toDoItemService.findAll().stream()
                .map(ToDoItemResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/api/todos/{id}")
    public ResponseEntity<ToDoItemResponse> getTodoById(@PathVariable int id) {
        ToDoItem item = toDoItemService.getToDoItemById(id);
        return ResponseEntity.ok(ToDoItemResponse.fromEntity(item));
    }

    @PostMapping("/api/todos")
    public ResponseEntity<ToDoItemResponse> createTodo(@Valid @RequestBody ToDoItemRequest request) {
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setDescription(request.getDescription());
        toDoItem.setDone(request.getDone() != null ? request.getDone() : false);
        toDoItem.setCreation_ts(OffsetDateTime.now());
        ToDoItem saved = toDoItemService.addToDoItem(toDoItem);
        return new ResponseEntity<>(ToDoItemResponse.fromEntity(saved), HttpStatus.CREATED);
    }

    @PutMapping("/api/todos/{id}")
    public ResponseEntity<ToDoItemResponse> updateTodo(@PathVariable int id, @Valid @RequestBody ToDoItemRequest request) {
        ToDoItem td = new ToDoItem();
        td.setDescription(request.getDescription());
        td.setDone(request.getDone() != null ? request.getDone() : false);
        ToDoItem updated = toDoItemService.updateToDoItem(id, td);
        return ResponseEntity.ok(ToDoItemResponse.fromEntity(updated));
    }

    @DeleteMapping("/api/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable int id) {
        toDoItemService.deleteToDoItem(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Rutas legacy (backward compatibility con frontend) ====================

    @GetMapping("/todolist")
    public List<ToDoItem> getAllToDoItems() {
        return toDoItemService.findAll();
    }

    @GetMapping("/todolist/{id}")
    public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable int id) {
        ToDoItem item = toDoItemService.getToDoItemById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping("/todolist")
    public ResponseEntity<ToDoItem> addToDoItem(@RequestBody ToDoItem todoItem) {
        if (todoItem.getCreation_ts() == null) {
            todoItem.setCreation_ts(OffsetDateTime.now());
        }
        ToDoItem td = toDoItemService.addToDoItem(todoItem);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location", "" + td.getID());
        responseHeaders.set("Access-Control-Expose-Headers", "location");
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PutMapping("/todolist/{id}")
    public ResponseEntity<ToDoItem> updateToDoItem(@RequestBody ToDoItem toDoItem, @PathVariable int id) {
        ToDoItem updated = toDoItemService.updateToDoItem(id, toDoItem);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/todolist/{id}")
    public ResponseEntity<Void> deleteToDoItem(@PathVariable("id") int id) {
        toDoItemService.deleteToDoItem(id);
        return ResponseEntity.ok().build();
    }
}
