package com.project.shopDemo.Controller;

import com.project.shopDemo.Entity.Products;
import com.project.shopDemo.Service.Impl.ProductsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/products")
@Tag(name = "Products", description = "Products Controller")
public class ProductsController {

    public ProductsServiceImpl productsServiceImpl;

    @Autowired
    public ProductsController(ProductsServiceImpl productsServiceImpl) {
        this.productsServiceImpl = productsServiceImpl;
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return productsServiceImpl.getAllProducts(page, size);
    }

    @GetMapping("/category")
    @Operation(summary = "Get products by category")
    public ResponseEntity<?> getProductsByCategory(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam String category) {
        return productsServiceImpl.getProductByCategory(page, size, category);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        return productsServiceImpl.getProductById(productId);
    }

    @GetMapping("/brand")
    @Operation(summary = "Get products by BRAND")
    public ResponseEntity<?> getProductByBrand(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam String brand) {
        return productsServiceImpl.getProductsByProductBrand(page, size, brand);
    }

    @PostMapping("/add-product")
    @Operation(summary = "Add product")
    public ResponseEntity<?> addProduct(@RequestBody Products product) {
        return productsServiceImpl.addProduct(product);
    }

    @PostMapping("/add-products")
    @Operation(summary = "Add products")
    public ResponseEntity<?> addProducts(@RequestBody List<Products> products) {
        return productsServiceImpl.addProducts(products);
    }

    @PutMapping("/update-product/{productId}")
    @Operation(summary = "Update products parameters BY ID")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Products product) {
        return productsServiceImpl.updateProductById(productId, product);
    }

    @PutMapping("/update-product/status")
    @Operation(summary = "Update product status BY ID AND STATUS")
    public ResponseEntity<?> updateProductStatus(@RequestParam("productId") Long productId,
                                                 @RequestParam boolean status) {
        return productsServiceImpl.updateProductStatus(productId,status);
    }

    @DeleteMapping("/delete-product/{productId}")
    @Operation(summary = "Delete product BY ID")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        return productsServiceImpl.deleteProductById(productId);
    }


}
