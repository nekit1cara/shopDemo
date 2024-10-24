package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Clients;
import com.project.shopDemo.ExceptionHandler.Exceptions.ClientAlreadyExistException;
import com.project.shopDemo.ExceptionHandler.Exceptions.ClientNotFoundException;
import com.project.shopDemo.Repository.ClientsRepository;
import com.project.shopDemo.Service.Impl.ClientsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientsServiceImplTest {

    @Mock
    private ClientsRepository clientsRepository;

    @InjectMocks
    private ClientsServiceImpl clientsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllClients_ShouldReturnPagedClients() {
        // Arrange
        List<Clients> clientsList = new ArrayList<>();
        clientsList.add(new Clients());
        when(clientsRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(clientsList));

        // Act
        ResponseEntity<?> response = clientsService.getAllClients(0, 10, "clientName");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(clientsRepository, times(1)).findAll((Pageable) any());
        System.out.println("Test getAllClients passed");
    }

    @Test
    void getClientById_ShouldReturnClient_WhenFound() throws ClientNotFoundException {
        // Arrange
        Clients client = new Clients();
        when(clientsRepository.findById(anyLong())).thenReturn(Optional.of(client));

        // Act
        ResponseEntity<?> response = clientsService.getClientById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
        verify(clientsRepository, times(1)).findById(anyLong());
        System.out.println("Test getClientById passed");
    }

    @Test
    void getClientById_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(clientsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> clientsService.getClientById(1L));
        verify(clientsRepository, times(1)).findById(anyLong());
        System.out.println("Test getClientById_ShouldThrowException passed");
    }

    @Test
    void addClient_ShouldSaveClient_WhenNotExist() throws ClientAlreadyExistException {
        // Arrange
        Clients client = new Clients();
        client.setClientEmail("test@example.com");
        when(clientsRepository.existsByClientEmail(anyString())).thenReturn(false);

        // Act
        ResponseEntity<?> response = clientsService.addClient(client);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(client, response.getBody());
        verify(clientsRepository, times(1)).save(client);
        System.out.println("Test addClient_ShouldSaveClient passed");
    }

    @Test
    void addClient_ShouldThrowException_WhenClientAlreadyExists() {
        // Arrange
        Clients client = new Clients();
        client.setClientEmail("test@example.com");
        when(clientsRepository.existsByClientEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ClientAlreadyExistException.class, () -> clientsService.addClient(client));
        verify(clientsRepository, times(0)).save(client);
        System.out.println("Test addClient_ShouldThrowException passed");
    }

    @Test
    void addClients_ShouldSaveAllClients() {
        // Arrange
        List<Clients> clients = List.of(new Clients(), new Clients());

        // Act
        ResponseEntity<?> response = clientsService.addClients(clients);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clients, response.getBody());
        verify(clientsRepository, times(1)).saveAll(clients);
        System.out.println("Test addClients passed");
    }

    @Test
    void updateClientById_ShouldUpdateClient_WhenFound() throws ClientNotFoundException, ClientAlreadyExistException {
        // Arrange
        Clients existingClient = new Clients();
        existingClient.setClientEmail("existing@example.com");
        Clients clientUpdate = new Clients();
        clientUpdate.setClientEmail("updated@example.com");
        when(clientsRepository.findById(anyLong())).thenReturn(Optional.of(existingClient));
        when(clientsRepository.existsByClientEmail(anyString())).thenReturn(false);

        // Act
        ResponseEntity<?> response = clientsService.updateClientById(1L, clientUpdate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Client : 1 successfully updated", response.getBody());
        verify(clientsRepository, times(1)).save(existingClient);
        System.out.println("Test updateClientById passed");
    }

    @Test
    void updateClientById_ShouldThrowException_WhenNotFound() {
        // Arrange
        Clients clientUpdate = new Clients();
        when(clientsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> clientsService.updateClientById(1L, clientUpdate));
        verify(clientsRepository, times(0)).save(any());
        System.out.println("Test updateClientById_ShouldThrowException passed");
    }

    @Test
    void updateClientById_ShouldThrowException_WhenClientEmailAlreadyExists() {
        // Arrange
        Clients existingClient = new Clients();
        existingClient.setClientEmail("existing@example.com");
        Clients clientUpdate = new Clients();
        clientUpdate.setClientEmail("existing@example.com");
        when(clientsRepository.findById(anyLong())).thenReturn(Optional.of(existingClient));
        when(clientsRepository.existsByClientEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ClientAlreadyExistException.class, () -> clientsService.updateClientById(1L, clientUpdate));
        verify(clientsRepository, times(0)).save(existingClient);
        System.out.println("Test updateClientById_ShouldThrowException_WhenClientEmailAlreadyExists passed");
    }

    @Test
    void deleteClientById_ShouldDeleteClient_WhenFound() throws ClientNotFoundException {
        // Arrange
        Clients existingClient = new Clients();
        when(clientsRepository.findById(anyLong())).thenReturn(Optional.of(existingClient));

        // Act
        ResponseEntity<?> response = clientsService.deleteClientById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Client : 1 successfully deleted", response.getBody());
        verify(clientsRepository, times(1)).deleteById(1L);
        System.out.println("Test deleteClientById passed");
    }

    @Test
    void deleteClientById_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(clientsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> clientsService.deleteClientById(1L));
        verify(clientsRepository, times(0)).deleteById(anyLong());
        System.out.println("Test deleteClientById_ShouldThrowException passed");
    }
}
