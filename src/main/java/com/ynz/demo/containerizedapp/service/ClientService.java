package com.ynz.demo.containerizedapp.service;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.domain.Order;
import com.ynz.demo.containerizedapp.dto.ClientDto;
import com.ynz.demo.containerizedapp.dto.ClientInfo;
import com.ynz.demo.containerizedapp.exceptions.ClientNotFoundException;
import com.ynz.demo.containerizedapp.exceptions.DuplicatedClientException;
import com.ynz.demo.containerizedapp.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientDto createNewClient(ClientDto clientDto) {
        try {
            findClientByEmail(clientDto.getEmail());
        } catch (ClientNotFoundException e) {
            Client client = new Client();
            client.setName(clientDto.getName());
            client.setEmail(clientDto.getEmail());

            Client persisted = clientRepository.save(client);
            return new ClientDto(persisted);
        }

        throw new DuplicatedClientException("User: " + clientDto.getEmail() + " already existed...");
    }

    public Client addClientOrder(@Email String clientEmail, Order order) throws ClientNotFoundException {
        ClientInfo clientInfo = findClientByEmail(clientEmail);

        Client client = new Client();
        client.setEmail(clientInfo.getEmail());
        client.setName(clientInfo.getName());
        client.addOrder(order);

        return clientRepository.save(client);
    }

    public ClientInfo findClientByEmail(@Email String clientEmail) throws ClientNotFoundException {
        return clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ClientNotFoundException("User:  " + clientEmail + " is not found..."));
    }

}
