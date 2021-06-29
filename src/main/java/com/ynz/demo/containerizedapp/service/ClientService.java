package com.ynz.demo.containerizedapp.service;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.domain.Order;
import com.ynz.demo.containerizedapp.dto.ClientDto;
import com.ynz.demo.containerizedapp.dto.mapper.OrderMapper;
import com.ynz.demo.containerizedapp.dto.projection.ClientInfo;
import com.ynz.demo.containerizedapp.dto.OrderDto;
import com.ynz.demo.containerizedapp.exceptions.ClientNotFoundException;
import com.ynz.demo.containerizedapp.exceptions.DuplicatedClientException;
import com.ynz.demo.containerizedapp.repository.ClientRepository;
import com.ynz.demo.containerizedapp.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;

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

    public ClientDto addClientOrder(String clientEmail, OrderDto orderDto) {
        ClientInfo clientInfo = findClientByEmail(clientEmail);

        Client client = new Client();
        client.setEmail(clientInfo.getEmail());
        client.setName(clientInfo.getName());

        Order newOrder = new Order();
        newOrder.setOrderStatus(orderDto.getOrderStatus());

        Client persisted = clientRepository.save(client);

        return new ClientDto(persisted);
    }

    public ClientInfo findClientByEmail(String clientEmail) {
        return clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ClientNotFoundException("User:  " + clientEmail + " is not found..."));
    }

    public OrderDto createOrder(OrderDto orderDto){
        OrderMapper mapper = OrderMapper.instance();
        Order order = mapper.mapToEntity(orderDto);
        order.setCreatedAt(OffsetDateTime.now());

        Order persisted = orderRepository.save(order);

        return mapper.mapToDto(persisted);
    }

}
