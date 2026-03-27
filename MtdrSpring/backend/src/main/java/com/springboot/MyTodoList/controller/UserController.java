package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.dto.UserRequest;
import com.springboot.MyTodoList.dto.UserResponse;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ==================== API REST limpia ====================

    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getAllUsersApi() {
        List<UserResponse> users = userService.findAll().stream()
                .map(UserResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> getUserByIdApi(@PathVariable int id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }

    @PostMapping("/api/users")
    public ResponseEntity<UserResponse> createUserApi(@Valid @RequestBody UserRequest request) {
        User newUser = new User();
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setUserPassword(request.getUserPassword());
        User saved = userService.addUser(newUser);
        return new ResponseEntity<>(UserResponse.fromEntity(saved), HttpStatus.CREATED);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> updateUserApi(@PathVariable int id, @Valid @RequestBody UserRequest request) {
        User u = new User();
        u.setPhoneNumber(request.getPhoneNumber());
        u.setUserPassword(request.getUserPassword());
        User updated = userService.updateUser(id, u);
        return ResponseEntity.ok(UserResponse.fromEntity(updated));
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<Void> deleteUserApi(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Rutas legacy (backward compatibility) ====================

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/adduser")
    public ResponseEntity<User> addUser(@RequestBody User newUser) {
        User dbUser = userService.addUser(newUser);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location", "" + dbUser.getID());
        responseHeaders.set("Access-Control-Expose-Headers", "location");
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable int id) {
        User dbUser = userService.updateUser(id, user);
        return ResponseEntity.ok(dbUser);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
