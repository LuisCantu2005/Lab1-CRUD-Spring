package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.controller.GlobalExceptionHandler.ResourceNotFoundException;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User getUserById(int id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User con id " + id + " no encontrado"));
    }

    public User addUser(User newUser){
        return userRepository.save(newUser);
    }

    public void deleteUser(int id){
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User con id " + id + " no encontrado");
        }
        userRepository.deleteById(id);
    }

    public User updateUser(int id, User user2update){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User con id " + id + " no encontrado"));
        user.setPhoneNumber(user2update.getPhoneNumber());
        user.setUserPassword(user2update.getUserPassword());
        return userRepository.save(user);
    }

}
