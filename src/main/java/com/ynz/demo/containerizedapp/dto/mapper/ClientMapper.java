package com.ynz.demo.containerizedapp.dto.mapper;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.dto.ClientDto;

public class ClientMapper implements Invertible<Client, ClientDto> {

    @Override
    public ClientDto mapToDto(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setEmail(client.getEmail());
        clientDto.setName(client.getName());

        return clientDto;
    }

    @Override
    public Client mapToEntity(ClientDto clientDto) {
        Client client = new Client();
        client.setEmail(clientDto.getEmail());
        client.setName(clientDto.getName());

        return client;
    }
}
