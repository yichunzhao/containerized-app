package com.ynz.demo.containerizedapp.service;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.domain.Order;
import com.ynz.demo.containerizedapp.dto.ClientDto;
import com.ynz.demo.containerizedapp.dto.OrderDto;
import com.ynz.demo.containerizedapp.dto.mapper.OrderMapper;
import com.ynz.demo.containerizedapp.dto.projection.ClientInfo;
import com.ynz.demo.containerizedapp.exceptions.ClientNotFoundException;
import com.ynz.demo.containerizedapp.exceptions.DuplicatedClientException;
import com.ynz.demo.containerizedapp.repository.ClientRepository;
import com.ynz.demo.containerizedapp.repository.OrderRepository;
import com.ynz.demo.containerizedapp.shared.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

        OrderMapper orderMapper = OrderMapper.instance();
        Order newOrder = orderMapper.mapToEntity(orderDto);

        client.addOrder(newOrder);

        Client persisted = clientRepository.save(client);

        return new ClientDto(persisted);
    }

    public ClientInfo findClientByEmail(String clientEmail) {
        return findClientByEmail(clientEmail, ClientInfo.class);
    }

    public <T> T findClientByEmail(String clientEmail, Class<T> type) {
        return clientRepository.findByEmail(clientEmail, type)
                .orElseThrow(() -> new ClientNotFoundException("User:  " + clientEmail + " is not found..."));
    }

    public OrderDto createClientOrder(String clientEmail, OrderDto orderDto) {
        OrderMapper mapper = OrderMapper.instance();
        Order order = mapper.mapToEntity(orderDto);
        order.setOrderStatus(OrderStatus.SUSPENDING);
        order.setCreatedAt(OffsetDateTime.now());

        Client client = findClientByEmail(clientEmail, Client.class);
        order.setClient(client);

        Order persisted = orderRepository.save(order);

        return mapper.mapToDto(persisted);
    }

    public List<OrderDto> findClientOrders(String clientEmail){
        findClientByEmail(clientEmail);

        List<Order> orders = clientRepository.findClientOrdersByEmail(clientEmail);

        OrderMapper mapper = OrderMapper.instance();
        return orders.stream().map(mapper::mapToDto).collect(toList());
    }

}
