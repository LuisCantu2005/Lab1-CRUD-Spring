package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.controller.GlobalExceptionHandler.ResourceNotFoundException;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoItemService {

    private final ToDoItemRepository toDoItemRepository;

    public ToDoItemService(ToDoItemRepository toDoItemRepository) {
        this.toDoItemRepository = toDoItemRepository;
    }

    public List<ToDoItem> findAll(){
        return toDoItemRepository.findAll();
    }

    public ToDoItem getToDoItemById(int id){
        return toDoItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ToDoItem con id " + id + " no encontrado"));
    }

    public Optional<ToDoItem> findById(int id){
        return toDoItemRepository.findById(id);
    }

    public ToDoItem addToDoItem(ToDoItem toDoItem){
        if (toDoItem.getCreation_ts() == null) {
            toDoItem.setCreation_ts(OffsetDateTime.now());
        }
        return toDoItemRepository.save(toDoItem);
    }

    public void deleteToDoItem(int id){
        if (!toDoItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("ToDoItem con id " + id + " no encontrado");
        }
        toDoItemRepository.deleteById(id);
    }

    public ToDoItem updateToDoItem(int id, ToDoItem td){
        ToDoItem toDoItem = toDoItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ToDoItem con id " + id + " no encontrado"));
        toDoItem.setDescription(td.getDescription());
        if (td.getCreation_ts() != null) {
            toDoItem.setCreation_ts(td.getCreation_ts());
        }
        toDoItem.setDone(td.isDone());
        return toDoItemRepository.save(toDoItem);
    }

}
