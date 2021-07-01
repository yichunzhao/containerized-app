package com.ynz.demo.containerizedapp.dto.mapper;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.dto.ClientDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "instance")
public class ClientMapper implements Invertible<Client, ClientDto> {

    @Override
    public ClientDto mapToDto(@NonNull  Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setEmail(client.getEmail());
        clientDto.setName(client.getName());

        return clientDto;
    }

    @Override
    public Client mapToEntity(@NonNull ClientDto clientDto) {
        Client client = new Client();
        client.setEmail(clientDto.getEmail());
        client.setName(clientDto.getName());

        return client;
    }
}
