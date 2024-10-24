package com.project.shopDemo.Controller;

import com.project.shopDemo.Entity.Clients;
import com.project.shopDemo.Service.Impl.ClientsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/clients")
@Tag(name = "clients", description = "Clients Controller")
public class ClientsController {

    public ClientsServiceImpl clientsServiceImpl;

    @Autowired
    public ClientsController(ClientsServiceImpl clientsServiceImpl) {
        this.clientsServiceImpl = clientsServiceImpl;
    }

    @GetMapping
    @Operation(summary = "Get all clients")
     public ResponseEntity<?> getAllClients(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id") String sortBy) {
        return clientsServiceImpl.getAllClients(page, size, sortBy);
    }

    @GetMapping("/{clientId}")
    @Operation(summary = "Get client by {clientId}")
     public ResponseEntity<?> getClientById(@PathVariable Long clientId) {
        return clientsServiceImpl.getClientById(clientId);
    }

    @PostMapping("/add-client")
    @Operation(summary = "Create client")
     public ResponseEntity<?> addClient(@RequestBody Clients client) {
        return clientsServiceImpl.addClient(client);
    }

    @PostMapping("/add-clients")
    @Operation(summary = "Create clientS")
     public ResponseEntity<?> addClients(@RequestBody List<Clients> clients) {
        return clientsServiceImpl.addClients(clients);
    }

    @PutMapping("/update-client/{clientId}")
    @Operation(summary = "Update client by {clientId}")
     public ResponseEntity<?> updateClientById(@PathVariable Long clientId, @RequestBody Clients client) {
        return clientsServiceImpl.updateClientById(clientId, client);
    }

    @DeleteMapping("/delete-client/{clientId}")
    @Operation(summary = "Delete client by {clientId}")
     public ResponseEntity<?> deleteClientById(@PathVariable Long clientId) {
        return clientsServiceImpl.deleteClientById(clientId);
    }

}
