package com.projetoforum.forum.controller.dto;

import javax.validation.constraints.NotBlank;

public class DesinscreverEmailDto {

    @NotBlank(message = "O email não deve estar em branco.")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
