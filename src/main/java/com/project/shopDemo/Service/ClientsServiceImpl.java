package com.project.shopDemo.Service;


import com.project.shopDemo.Entity.Clients;
import com.project.shopDemo.ExceptionHandler.Exceptions.ClientAlreadyExistException;
import com.project.shopDemo.ExceptionHandler.Exceptions.ClientNotFoundException;
import com.project.shopDemo.Repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsServiceImpl {

    public ClientsRepository clientsRepository;

    @Autowired
    public ClientsServiceImpl(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    public ResponseEntity<?> getAllClients(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy));
        Page<Clients> allClients = clientsRepository.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(allClients);

    }

    public ResponseEntity<?> getClientById(Long clientId) throws ClientNotFoundException {

        Optional<Clients> existingClient = clientsRepository.findById(clientId);

        if (existingClient.isEmpty()) {
            throw new ClientNotFoundException("Client : " + clientId + " not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(existingClient.get());

    }

    public ResponseEntity<?> addClient(Clients client) throws ClientAlreadyExistException {

        if (clientsRepository.existsByClientEmail(client.getClientEmail())) {
            throw new ClientAlreadyExistException("Client with this : " + client.getClientEmail() + " email already exist");
        }

        clientsRepository.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);

    }

    public ResponseEntity<?> addClients(List<Clients> clients) {

        clientsRepository.saveAll(clients);
        return ResponseEntity.status(HttpStatus.CREATED).body(clients);

    }

    public ResponseEntity<?> updateClientById(Long clientId, Clients client) throws ClientNotFoundException, ClientAlreadyExistException {

        Optional<Clients> existingClient = clientsRepository.findById(clientId);

        if (existingClient.isEmpty()) {
            throw new ClientNotFoundException("Client : " + clientId + " not found.");
        }


        Clients updatedClient = existingClient.get();

        if (client.getClientName() != null) {
            updatedClient.setClientName(client.getClientName());
        }
        if (client.getClientSurname() != null) {
            updatedClient.setClientSurname(client.getClientSurname());
        }
        if (client.getClientPhone() != null) {
            updatedClient.setClientPhone(client.getClientPhone());
        }
        if (client.getClientAddress() != null) {
            updatedClient.setClientAddress(client.getClientAddress());
        }
        if (client.getClientEmail() != null) {
            updatedClient.setClientEmail(client.getClientEmail());
        }

        if (clientsRepository.existsByClientEmail(updatedClient.getClientEmail())) {
            throw new ClientAlreadyExistException("Client with this : " + updatedClient.getClientEmail() + " email already exist");
        }

        clientsRepository.save(updatedClient);
        return ResponseEntity.status(HttpStatus.OK).body("Client : " + clientId + " successfully updated");


    }

    public ResponseEntity<?> deleteClientById(Long clientId) throws ClientNotFoundException {

        Optional<Clients> existingClient = clientsRepository.findById(clientId);

        if (existingClient.isEmpty()) {
            throw new ClientNotFoundException("Client : " + clientId + " not found.");
        }

        clientsRepository.deleteById(clientId);
        return ResponseEntity.status(HttpStatus.OK).body("Client : " + clientId + " successfully deleted");

    }

}

