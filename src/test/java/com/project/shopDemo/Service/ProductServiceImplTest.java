package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Products;
import com.project.shopDemo.ExceptionHandler.Exceptions.ProductAlreadyExistException;
import com.project.shopDemo.ExceptionHandler.Exceptions.ProductNotFoundException;
import com.project.shopDemo.Repository.ProductsRepository;
import com.project.shopDemo.Service.Impl.ProductsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {


    @Mock
    private ProductsRepository productsRepository;

    @InjectMocks
    private ProductsServiceImpl productsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts_ShouldReturnPagedProducts() {
        // Arrange
        Products product = new Products();
        Page<Products> productsPage = new PageImpl<>(List.of(product));
        when(productsRepository.findAll(any(PageRequest.class))).thenReturn(productsPage);

        // Act
        ResponseEntity<?> response = productsService.getAllProducts(0, 5);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productsPage, response.getBody());
        verify(productsRepository, times(1)).findAll(any(PageRequest.class));

        // Console output
        System.out.println("Test getAllProducts passed: " + response.getStatusCode());
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenFound() throws ProductNotFoundException {
        // Arrange
        Products product = new Products();
        when(productsRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Act
        ResponseEntity<?> response = productsService.getProductById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productsRepository, times(1)).findById(anyLong());

        // Console output
        System.out.println("Test getProductById passed: " + response.getStatusCode());
    }

    @Test
    void getProductById_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(productsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productsService.getProductById(1L));
        verify(productsRepository, times(1)).findById(anyLong());

        // Console output
        System.out.println("Test getProductById_ShouldThrowException passed: Exception thrown");
    }

    @Test
    void addProduct_ShouldSaveProduct_WhenNotExist() throws ProductAlreadyExistException {
        // Arrange
        Products product = new Products();
        product.setProductModel("Model123");
        when(productsRepository.existsByProductModel(anyString())).thenReturn(false);

        // Act
        ResponseEntity<?> response = productsService.addProduct(product);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productsRepository, times(1)).save(product);

        // Console output
        System.out.println("Test addProduct passed: " + response.getStatusCode());
    }

    @Test
    void addProduct_ShouldThrowException_WhenProductAlreadyExists() {
        // Arrange
        Products product = new Products();
        product.setProductModel("Model123");
        when(productsRepository.existsByProductModel(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ProductAlreadyExistException.class, () -> productsService.addProduct(product));
        verify(productsRepository, times(0)).save(product);

        // Console output
        System.out.println("Test addProduct_ShouldThrowException passed: Exception thrown");
    }

    @Test
    void updateProductById_ShouldUpdateProduct_WhenFound() throws ProductNotFoundException, ProductAlreadyExistException {
        // Arrange
        Products existingProduct = new Products();
        Products newProduct = new Products();
        newProduct.setProductBrand("NewBrand");
        when(productsRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));

        // Act
        ResponseEntity<?> response = productsService.updateProductById(1L, newProduct);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productsRepository, times(1)).save(existingProduct);
        assertEquals("NewBrand", existingProduct.getProductBrand());

        // Console output
        System.out.println("Test updateProductById passed: " + response.getStatusCode());
    }

    @Test
    void updateProductStatus_ShouldUpdateStatus_WhenProductExists() {
        // Arrange
        Products existingProduct = new Products();
        when(productsRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));

        // Act
        ResponseEntity<?> response = productsService.updateProductStatus(1L, true);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(existingProduct.isProductStatus()); // remove getProductStatus
        verify(productsRepository, times(1)).save(existingProduct);

        // Console output
        System.out.println("Test updateProductStatus passed: " + response.getStatusCode());
    }

    @Test
    void deleteProductById_ShouldDeleteProduct_WhenFound() throws ProductNotFoundException {
        // Arrange
        Products existingProduct = new Products();
        when(productsRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));

        // Act
        ResponseEntity<?> response = productsService.deleteProductById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productsRepository, times(1)).deleteById(anyLong());

        // Console output
        System.out.println("Test deleteProductById passed: " + response.getStatusCode());
    }

    @Test
    void deleteProductById_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(productsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productsService.deleteProductById(1L));
        verify(productsRepository, times(0)).deleteById(anyLong());

        // Console output
        System.out.println("Test deleteProductById_ShouldThrowException passed: Exception thrown");
    }

}
