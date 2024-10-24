package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Products;
import com.project.shopDemo.ExceptionHandler.Exceptions.ProductAlreadyExistException;
import com.project.shopDemo.ExceptionHandler.Exceptions.ProductNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<?> getAllProducts(int page, int size);

    ResponseEntity<?> getProductByCategory(int page, int size, String category);

    ResponseEntity<?> getProductById(Long productId) throws ProductNotFoundException;

    ResponseEntity<?> getProductsByProductBrand(int page, int size, String productBrands) throws ProductNotFoundException;

    ResponseEntity<?> addProduct(Products product) throws ProductAlreadyExistException;

    ResponseEntity<?> addProducts(List<Products> products);

    ResponseEntity<?> updateProductById(Long productId, Products product) throws ProductNotFoundException, ProductAlreadyExistException;

    ResponseEntity<?> updateProductStatus(Long productId, Boolean status);

    ResponseEntity<?> deleteProductById(Long productId) throws ProductNotFoundException;

    //ResponseEntity<?> updateStatusForAllProducts();


    //////////////////////////////////////////////////////////////////////
//    ResponseEntity<?> getAllWatches(int page, int size);
//    ResponseEntity<?> getAllNecklace(int page, int size);
//    ResponseEntity<?> getAllBracelets(int page, int size);
//    ResponseEntity<?> getAllRings(int page, int size);
//    ResponseEntity<?> getAllSunglasses(int page, int size);

}
