package com.ynz.demo.containerizedapp.dto;

import com.ynz.demo.containerizedapp.domain.Client;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ClientDto {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    public ClientDto(){
    }

    public ClientDto(Client client) {
        this.name = client.getName();
        this.email = client.getEmail();
    }

}
