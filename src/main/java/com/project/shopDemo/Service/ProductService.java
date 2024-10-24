package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Products;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<?> getAllProducts(int page, int size);

    ResponseEntity<?> getProductByCategory(int page, int size, String category);

    ResponseEntity<?> getProductById(Long productId);

    ResponseEntity<?> getProductsByProductBrand(int page, int size, String productBrands);

    ResponseEntity<?> addProduct(Products product);

    ResponseEntity<?> addProducts(List<Products> products);

    ResponseEntity<?> updateProductById(Long productId, Products product);







    //////////////////////////////////////////////////////////////////////
    ResponseEntity<?> getAllWatches(int page, int size);
    ResponseEntity<?> getAllNecklace(int page, int size);
    ResponseEntity<?> getAllBracelets(int page, int size);
    ResponseEntity<?> getAllRings(int page, int size);
    ResponseEntity<?> getAllSunglasses(int page, int size);

}
