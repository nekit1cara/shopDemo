package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Carts;
import com.project.shopDemo.Entity.CartsItems;
import com.project.shopDemo.Entity.Products;
import com.project.shopDemo.ExceptionHandler.Exceptions.CartNotFoundException;
import com.project.shopDemo.ExceptionHandler.Exceptions.ProductNotFoundException;
import com.project.shopDemo.ExceptionHandler.Exceptions.ProductOutOfStockException;
import com.project.shopDemo.Repository.CartRepository;
import com.project.shopDemo.Repository.CartsItemsRepository;
import com.project.shopDemo.Repository.ProductsRepository;
import com.project.shopDemo.Service.Impl.CartsItemsServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartsItemsServiceImplTest {

    @Mock
    private CartsItemsRepository cartsItemsRepository;

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private CartRepository cartsRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartsItemsServiceImpl cartsItemsService;

    private Carts cart;
    private Products product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cart = new Carts();
        product = new Products();
        product.setId(1L);
        product.setProductQuantity(10);
        product.setProductStatus(true);
    }

    @Test
    void addItemInCart_ShouldAddItem_WhenProductIsAvailable() {
        // Arrange
        CartsItems cartsItem = new CartsItems();
        cartsItem.setProducts(product);
        cartsItem.setQuantity(2);

        String sessionId = "testSessionId";
        when(session.getId()).thenReturn(sessionId);
        when(cartsRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));
        when(productsRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(cartsItemsRepository.save(cartsItem)).thenReturn(cartsItem);
        when(productsRepository.save(product)).thenReturn(product);

        // Act
        ResponseEntity<?> response = cartsItemsService.addItemInCart(session, cartsItem);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cartsItem, response.getBody());
        assertEquals(8, product.getProductQuantity());
        verify(cartsItemsRepository, times(1)).save(cartsItem);
        verify(productsRepository, times(1)).save(product);
        System.out.println("Test addItemInCart_ShouldAddItem passed");
    }

    @Test
    void addItemInCart_ShouldThrowProductNotFound_WhenProductDoesNotExist() {
        // Arrange
        CartsItems cartsItem = new CartsItems();
        cartsItem.setProducts(product);
        cartsItem.setQuantity(2);

        String sessionId = "testSessionId";
        when(session.getId()).thenReturn(sessionId);
        when(cartsRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));
        when(productsRepository.findById(product.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> {
            cartsItemsService.addItemInCart(session, cartsItem);
        });
        System.out.println("Test addItemInCart_ShouldThrowProductNotFound passed");
    }

    @Test
    void addItemInCart_ShouldThrowProductOutOfStock_WhenProductNotAvailable() {
        // Arrange
        CartsItems cartsItem = new CartsItems();
        cartsItem.setProducts(product);
        cartsItem.setQuantity(12); // More than available

        String sessionId = "testSessionId";
        when(session.getId()).thenReturn(sessionId);
        when(cartsRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));
        when(productsRepository.findById(product.getId())).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(ProductOutOfStockException.class, () -> {
            cartsItemsService.addItemInCart(session, cartsItem);
        });
        System.out.println("Test addItemInCart_ShouldThrowProductOutOfStock passed");
    }

    @Test
    void deleteProductFromCart_ShouldDeleteProduct_WhenProductExists() {
        // Arrange
        CartsItems cartsItem = new CartsItems();
        cartsItem.setProducts(product);
        cartsItem.setQuantity(1);
        cart.getCartsItems().add(cartsItem);
        String sessionId = "testSessionId";
        when(session.getId()).thenReturn(sessionId);
        when(cartsRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));
        when(cartsItemsRepository.findById(cartsItem.getProducts().getId())).thenReturn(Optional.of(cartsItem));

        // Act
        ResponseEntity<?> response = cartsItemsService.deleteProductFromCart(session, cartsItem.getProducts().getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product deleted.", response.getBody());
        verify(cartsItemsRepository, times(1)).delete(cartsItem);
        System.out.println("Test deleteProductFromCart_ShouldDeleteProduct passed");
    }

    @Test
    void deleteProductFromCart_ShouldThrowProductNotFound_WhenProductDoesNotExist() {
        // Arrange
        String sessionId = "testSessionId";
        when(session.getId()).thenReturn(sessionId);
        when(cartsRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));
        when(cartsItemsRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> {
            cartsItemsService.deleteProductFromCart(session, 1L);
        });
        System.out.println("Test deleteProductFromCart_ShouldThrowProductNotFound passed");
    }

    @Test
    void clearCart_ShouldClearCart_WhenCartExists() {
        // Arrange
        String sessionId = "testSessionId";
        when(session.getId()).thenReturn(sessionId);
        when(cartsRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));

        // Act
        ResponseEntity<?> response = cartsItemsService.clearCart(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Корзина очищена.", response.getBody());
        verify(cartsItemsRepository, times(1)).deleteAll(cart.getCartsItems());
        System.out.println("Test clearCart_ShouldClearCart passed");
    }

    @Test
    void clearCart_ShouldThrowCartNotFound_WhenCartDoesNotExist() {
        // Arrange
        String sessionId = "testSessionId";
        when(session.getId()).thenReturn(sessionId);
        when(cartsRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CartNotFoundException.class, () -> {
            cartsItemsService.clearCart(session);
        });
        System.out.println("Test clearCart_ShouldThrowCartNotFound passed");
    }
}
