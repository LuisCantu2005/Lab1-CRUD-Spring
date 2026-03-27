package com.springboot.MyTodoList.dto;

import com.springboot.MyTodoList.model.User;

public class UserResponse {

    private int id;
    private String phoneNumber;

    public UserResponse() {}

    public static UserResponse fromEntity(User entity) {
        UserResponse dto = new UserResponse();
        dto.setId(entity.getID());
        dto.setPhoneNumber(entity.getPhoneNumber());
        return dto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
