package com.springboot.MyTodoList.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Size(max = 15, message = "El número de teléfono no puede exceder 15 caracteres")
    private String phoneNumber;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(max = 100, message = "La contraseña no puede exceder 100 caracteres")
    private String userPassword;

    public UserRequest() {}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
