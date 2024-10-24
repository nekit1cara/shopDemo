package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Products;
import com.project.shopDemo.ExceptionHandler.Exceptions.ProductAlreadyExistException;
import com.project.shopDemo.ExceptionHandler.Exceptions.ProductNotFoundException;
import com.project.shopDemo.Repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsServiceImpl {

    public ProductsRepository productsRepository;

    @Autowired
    public ProductsServiceImpl(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public ResponseEntity<?> getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Products> allProducts = productsRepository.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    public ResponseEntity<?> getAllWatches(int page, int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Products> watches = productsRepository.findByProductCategory("Watches", pageable);
//
//            return ResponseEntity.status(HttpStatus.OK).body(watches);
//
//    }
//
//    public ResponseEntity<?> getAllNecklace(int page, int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Products> necklaces = productsRepository.findByProductCategory("Necklaces", pageable);
//
//        return ResponseEntity.status(HttpStatus.OK).body(necklaces);
//
//    }
//
//    public ResponseEntity<?> getAllBracelets(int page, int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Products> bracelets = productsRepository.findByProductCategory("Bracelets", pageable);
//
//        return ResponseEntity.status(HttpStatus.OK).body(bracelets);
//
//    }
//
//    public ResponseEntity<?> getAllRings(int page, int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Products> rings = productsRepository.findByProductCategory("Rings", pageable);
//
//        return ResponseEntity.status(HttpStatus.OK).body(rings);
//
//    }
//
//    public ResponseEntity<?> getAllSunglasses(int page, int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Products> sunglasses = productsRepository.findByProductCategory("Sunglasses", pageable);
//
//        return ResponseEntity.status(HttpStatus.OK).body(sunglasses);
//
//    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public ResponseEntity<?> getProductByCategory(int page, int size, String category) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Products> watches = productsRepository.findByProductCategory("Watches", pageable);
        Page<Products> necklaces = productsRepository.findByProductCategory("Necklaces", pageable);
        Page<Products> bracelets = productsRepository.findByProductCategory("Bracelets", pageable);
        Page<Products> rings = productsRepository.findByProductCategory("Rings", pageable);
        Page<Products> sunglasses = productsRepository.findByProductCategory("Sunglasses", pageable);

        return switch (category) {
            case  "Watches", "watches" -> ResponseEntity.status(HttpStatus.OK).body(watches);
            case  "Necklaces", "necklaces" -> ResponseEntity.status(HttpStatus.OK).body(necklaces);
            case  "Bracelets", "bracelets" -> ResponseEntity.status(HttpStatus.OK).body(bracelets);
            case  "Rings", "rings" -> ResponseEntity.status(HttpStatus.OK).body(rings);
            case  "Sunglasses", "sunglasses" -> ResponseEntity.status(HttpStatus.OK).body(sunglasses);
            default -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        };

    }

    public ResponseEntity<?> getProductById(Long productId) throws ProductNotFoundException {

        Optional<Products> existingProduct = productsRepository.findById(productId);

        if (existingProduct.isEmpty()) {
            throw new ProductNotFoundException("Product : " + productId + " not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(existingProduct.get());

    }


    public ResponseEntity<?> getProductsByProductBrand(int page, int size, String productBrands) throws ProductNotFoundException {

        Pageable pageable = PageRequest.of(page, size);
        Page<Products> brand = productsRepository.findByProductBrand(productBrands, pageable);

            if (brand.isEmpty()) {
                throw new ProductNotFoundException("Brand : " + productBrands + " not found.");
            }

        return ResponseEntity.status(HttpStatus.OK).body(brand);

    }


    public ResponseEntity<?> addProduct(Products product) throws ProductAlreadyExistException {

        if (productsRepository.existsByProductModel(product.getProductModel())) {
            throw new ProductAlreadyExistException("Product : " +
                    product.getProductBrand() + " " + product.getProductModel() + " already exist.");
        }

        productsRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);

    }

    public ResponseEntity<?> addProducts(List<Products> products) {
        productsRepository.saveAll(products);
        return ResponseEntity.status(HttpStatus.CREATED).body(products);
    }

    public ResponseEntity<?> updateProductById(Long productId, Products product) throws ProductNotFoundException, ProductAlreadyExistException {

        Optional<Products> existingProduct = productsRepository.findById(productId);

        if (existingProduct.isEmpty()) {
            throw new ProductNotFoundException("Product : " + productId + " not found.");
        }

        Products updateProduct = existingProduct.get();

        if (product.getProductBrand() != null) {
            updateProduct.setProductBrand(product.getProductBrand());
        }
        if (product.getProductModel() != null) {
            updateProduct.setProductModel(product.getProductModel());
        }
        if (product.getProductPrice() != 0) {
            updateProduct.setProductPrice(product.getProductPrice());
        }
        if (product.getProductDescription() != null) {
            updateProduct.setProductDescription(product.getProductDescription());
        }
        if (product.getProductCategory() != null) {
            updateProduct.setProductCategory(product.getProductCategory());
        }
        if (product.getProductQuantity() != 0) {
            updateProduct.setProductQuantity(product.getProductQuantity());
        }


        productsRepository.save(updateProduct);
        return ResponseEntity.status(HttpStatus.OK).body("Product : " + productId + " successfully updated");

    }

    public ResponseEntity<?> deleteProductById(Long productId) throws ProductNotFoundException {

        Optional<Products> existingProduct = productsRepository.findById(productId);

        if (existingProduct.isEmpty()) {
            throw new ProductNotFoundException("Product : " + productId + " not found.");
        }

        productsRepository.deleteById(productId);
        return ResponseEntity.status(HttpStatus.OK).body("Product : " + productId + " successfully deleted");

    }



}
