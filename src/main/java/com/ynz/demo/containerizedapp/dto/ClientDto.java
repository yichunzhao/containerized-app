package com.ynz.demo.containerizedapp.dto;

import com.ynz.demo.containerizedapp.domain.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto implements IsDto {

    @NotBlank(message = "Client should provide a name")
    private String name;

    @Email(message = "invalid email")
    @NotBlank(message = "Client should provide an Email address")
    private String email;

    public ClientDto(Client client) {
        this.name = client.getName();
        this.email = client.getEmail();
    }

}
