package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Carts;
import com.project.shopDemo.Entity.CartsItems;
import com.project.shopDemo.Repository.CartRepository;
import com.project.shopDemo.Service.Impl.CartServiceImpl;
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

class CartServiceImplTest {

    @Mock
    private CartRepository cartsRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartServiceImpl cartService;

    private Carts cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cart = new Carts();
        cart.setCartsItems(new ArrayList<>());
    }

    @Test
    void getItemsFromCart_ShouldReturnItems_WhenCartExists() {
        // Arrange
        String sessionId = "testSessionId";
        when(session.getId()).thenReturn(sessionId);
        when(cartsRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));

        // Act
        ResponseEntity<?> response = cartService.getItemsFromCart(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart.getCartsItems(), response.getBody());
        verify(cartsRepository, times(1)).findBySessionId(sessionId);
        System.out.println("Test getItemsFromCart_ShouldReturnItems passed");
    }

    @Test
    void getItemsFromCart_ShouldReturnNotFound_WhenNoItemsInCart() {
        // Arrange
        String sessionId = "testSessionId";
        when(session.getId()).thenReturn(sessionId);
        cart.setCartsItems(null); // No items in the cart
        when(cartsRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));

        // Act
        ResponseEntity<?> response = cartService.getItemsFromCart(session);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No items in cart", response.getBody());
        verify(cartsRepository, times(1)).findBySessionId(sessionId);
        System.out.println("Test getItemsFromCart_ShouldReturnNotFound passed");
    }

    @Test
    void getOrCreateCart_ShouldReturnExistingCart_WhenCartExists() {
        // Arrange
        String sessionId = "testSessionId";
        when(session.getId()).thenReturn(sessionId);
        when(cartsRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));

        // Act
        Carts returnedCart = cartService.getOrCreateCart(session);

        // Assert
        assertEquals(cart, returnedCart);
        verify(cartsRepository, times(1)).findBySessionId(sessionId);
        System.out.println("Test getOrCreateCart_ShouldReturnExistingCart passed");
    }

    @Test
    void getOrCreateCart_ShouldCreateNewCart_WhenCartDoesNotExist() {
        // Arrange
        String sessionId = "testSessionId";
        when(session.getId()).thenReturn(sessionId);
        when(cartsRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());
        when(cartsRepository.save(any(Carts.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Carts returnedCart = cartService.getOrCreateCart(session);

        // Assert
        assertNotNull(returnedCart);
        assertEquals(sessionId, returnedCart.getSessionId());
        verify(cartsRepository, times(1)).findBySessionId(sessionId);
        verify(cartsRepository, times(1)).save(any(Carts.class));
        System.out.println("Test getOrCreateCart_ShouldCreateNewCart passed");
    }
}
