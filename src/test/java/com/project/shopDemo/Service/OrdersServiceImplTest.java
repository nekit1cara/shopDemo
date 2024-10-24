package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.*;
import com.project.shopDemo.ExceptionHandler.Exceptions.OrderAlreadyBeAppliedException;
import com.project.shopDemo.Repository.*;
import com.project.shopDemo.Service.Impl.OrdersServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdersServiceImplTest {

    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private ClientsRepository clientsRepository;
    @Mock
    private OrderItemsRepository orderItemsRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartsItemsRepository cartsItemsRepository;
    @Mock
    private HttpSession session;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    private Clients client;
    private Carts cart;
    private Orders order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new Clients();
        client.setId(1L);

        cart = new Carts();
        cart.setSessionId("session-id");
        cart.setCartsItems(new ArrayList<>());

        order = new Orders();
        order.setClient(client);
        order.setOrderItems(new ArrayList<>());
    }

    @Test
    void getOrdersBySession_ShouldReturnOrders_WhenOrdersExist() {
        List<Orders> orders = new ArrayList<>();
        orders.add(order);

        when(session.getId()).thenReturn("session-id");
        when(ordersRepository.findBySessionIdAndOrderStatus("session-id", true)).thenReturn(orders);

        ResponseEntity<?> response = ordersService.getOrdersBySession(session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder_WhenValidDataProvided() {
        cart.getCartsItems().add(new CartsItems()); // Add a sample item to the cart

        when(session.getId()).thenReturn("session-id");
        when(cartRepository.findBySessionId("session-id")).thenReturn(Optional.of(cart));
        when(clientsRepository.findById(1L)).thenReturn(Optional.of(client));
        when(ordersRepository.save(any())).thenReturn(order);

        ResponseEntity<?> response = ordersService.createOrder(session, order);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(orderItemsRepository, times(1)).saveAll(anyList());
        verify(cartsItemsRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void createOrder_ShouldThrowCartNotFoundException_WhenNoCartItems() {
        when(session.getId()).thenReturn("session-id");
        when(cartRepository.findBySessionId("session-id")).thenReturn(Optional.of(cart));

        ResponseEntity<?> response = ordersService.createOrder(session, order);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No cart items found", response.getBody());
    }

    @Test
    void applyOrder_ShouldReturnOk_WhenOrderIsApplied() {
        order.setOrderStatus(false);
        when(session.getId()).thenReturn("session-id");
        when(ordersRepository.findBySessionIdAndId("session-id", 1L)).thenReturn(Optional.of(order));

        ResponseEntity<?> response = ordersService.applyOrder(session, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Заказ успешно подтверждён.", response.getBody());
        assertTrue(order.isOrderStatus());
        verify(ordersRepository, times(1)).save(order);
    }

    @Test
    void deleteOrderById_ShouldReturnOk_WhenOrderDeletedSuccessfully() {
        order.setOrderStatus(false);
        when(session.getId()).thenReturn("session-id");
        when(ordersRepository.findBySessionIdAndId("session-id", 1L)).thenReturn(Optional.of(order));
        when(orderItemsRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = ordersService.deleteOrderById(session, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order 1 deleted successfully.", response.getBody());
        verify(ordersRepository, times(1)).delete(order);
    }

    @Test
    void deleteOrderById_ShouldThrowOrderAlreadyBeAppliedException_WhenOrderIsAlreadyApplied() {
        order.setOrderStatus(true);
        when(session.getId()).thenReturn("session-id");
        when(ordersRepository.findBySessionIdAndId("session-id", 1L)).thenReturn(Optional.of(order));

        Exception exception = assertThrows(OrderAlreadyBeAppliedException.class, () -> {
            ordersService.deleteOrderById(session, 1L);
        });

        assertEquals("Заказ : 1 уже подтвержден.", exception.getMessage());
    }

    @Test
    void getOrders_ShouldReturnOrders_WhenCalled() {
        when(session.getId()).thenReturn("session-id");
        when(ordersRepository.findBySessionIdAndOrderStatus("session-id", true)).thenReturn(new ArrayList<>());

        List<Orders> orders = ordersService.getOrders(session);

        assertNotNull(orders);
        verify(ordersRepository, times(1)).findBySessionIdAndOrderStatus("session-id", true);
    }

    // Additional tests can be written for other methods as needed
}
