package com.ynz.demo.containerizedapp.service;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.domain.Order;
import com.ynz.demo.containerizedapp.exceptions.ClientNotFoundException;
import com.ynz.demo.containerizedapp.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Client addClientOrder(String clientEmail, Order order) throws ClientNotFoundException {
        Client client = findClientByEmail(clientEmail);
        client.addOrder(order);
        return  clientRepository.save(client);
    }

    public Client findClientByEmail(String clientEmail) throws ClientNotFoundException {
        return clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ClientNotFoundException("User:  " + clientEmail + " is not found..."));
    }

}
