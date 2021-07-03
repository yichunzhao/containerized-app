package com.ynz.demo.containerizedapp.controller;

import com.ynz.demo.containerizedapp.dto.ClientDto;
import com.ynz.demo.containerizedapp.dto.OrderDto;
import com.ynz.demo.containerizedapp.dto.projection.ClientInfo;
import com.ynz.demo.containerizedapp.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("client")
@RequiredArgsConstructor
@Validated
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDto> createNewClient(@Valid @RequestBody ClientDto clientDto, HttpServletRequest request) {
        ClientDto created = clientService.createNewClient(clientDto);
        URI uri = ServletUriComponentsBuilder.fromRequestUri(request).pathSegment("{email}").buildAndExpand(created.getEmail()).encode().toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("{email}")
    public ResponseEntity<ClientInfo> getClientByEmail(@PathVariable("email") @Email @NotBlank String email) {
        return ResponseEntity.status(HttpStatus.FOUND).body(clientService.findClientByEmail(email));
    }

    @PostMapping("{email}/orders")
    public ResponseEntity<OrderDto> createClientOrder(@PathVariable("email") @Email @NotBlank String clientEmail, @RequestBody OrderDto orderDto) {
        OrderDto createdOrderDto = clientService.createClientOrder(clientEmail, orderDto);
        return ResponseEntity.ok(createdOrderDto);
    }

    @GetMapping("{email}/orders")
    public ResponseEntity<List<OrderDto>> getClientOrders(@PathVariable("email") @Email @NotBlank String clientEmail) {
        return ResponseEntity.status(HttpStatus.FOUND).body(clientService.findClientOrders(clientEmail));
    }

}
