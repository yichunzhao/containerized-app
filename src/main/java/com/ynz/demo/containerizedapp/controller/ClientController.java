package com.ynz.demo.containerizedapp.controller;

import com.ynz.demo.containerizedapp.dto.ClientDto;
import com.ynz.demo.containerizedapp.dto.OrderDto;
import com.ynz.demo.containerizedapp.dto.projection.ClientInfo;
import com.ynz.demo.containerizedapp.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDto> createNewClient(@Validated @RequestBody ClientDto clientDto, HttpServletRequest request) {
        ClientDto created = clientService.createNewClient(clientDto);
        URI uri = ServletUriComponentsBuilder.fromRequestUri(request).pathSegment("{email}").buildAndExpand(created.getEmail()).encode().toUri();

        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("{email}")
    public ResponseEntity<ClientInfo> getClientByEmail(@PathVariable String email) {
        return ResponseEntity.ok().body(clientService.findClientByEmail(email));
    }

    @PostMapping("order")
    public ResponseEntity<OrderDto> createClientOrder(@RequestBody OrderDto orderDto) {
        OrderDto created = clientService.createOrder(orderDto);
        return ResponseEntity.ok(created);
    }

}
