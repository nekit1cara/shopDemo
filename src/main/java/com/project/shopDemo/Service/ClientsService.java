package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Clients;
import com.project.shopDemo.ExceptionHandler.Exceptions.ClientAlreadyExistException;
import com.project.shopDemo.ExceptionHandler.Exceptions.ClientNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClientsService {


    ResponseEntity<?> getAllClients(int page, int size, String sortBy);

    ResponseEntity<?> getClientById(Long clientId) throws ClientNotFoundException;

    ResponseEntity<?> addClient(Clients client) throws ClientAlreadyExistException;

    ResponseEntity<?> addClients(List<Clients> clients);

    ResponseEntity<?> updateClientById(Long clientId, Clients client) throws ClientNotFoundException, ClientAlreadyExistException;

    ResponseEntity<?> deleteClientById(Long clientId) throws ClientNotFoundException;
}
