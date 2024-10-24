package com.project.shopDemo.Service.Impl;

import com.project.shopDemo.Entity.Carts;
import com.project.shopDemo.Entity.CartsItems;
import com.project.shopDemo.Entity.Products;
import com.project.shopDemo.ExceptionHandler.Exceptions.CartNotFoundException;
import com.project.shopDemo.ExceptionHandler.Exceptions.ProductNotFoundException;
import com.project.shopDemo.ExceptionHandler.Exceptions.ProductOutOfStockException;
import com.project.shopDemo.Repository.CartRepository;
import com.project.shopDemo.Repository.CartsItemsRepository;
import com.project.shopDemo.Repository.ProductsRepository;
import com.project.shopDemo.Service.CartsItemsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartsItemsServiceImpl implements CartsItemsService {

    public CartsItemsRepository cartsItemsRepository;
    public ProductsRepository productsRepository;
    public CartRepository cartsRepository;

    @Autowired
    public CartsItemsServiceImpl(CartsItemsRepository cartsItemsRepository,
                                 ProductsRepository productsRepository,
                                 CartRepository cartsRepository) {
        this.cartsItemsRepository = cartsItemsRepository;
        this.productsRepository = productsRepository;
        this.cartsRepository = cartsRepository;
    }

    @Override
    public ResponseEntity<?> addItemInCart(HttpSession session, CartsItems cartsItem) {

        Carts cart = getOrCreateCart(session);

        if (cart != null) {
            Long cartId = cart.getId();
            cartsRepository.findById(cartId)
                    .orElseThrow(() -> new CartNotFoundException("Cart : " + cartId + " not found."));
            cartsItem.setCarts(cart);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart not found.");
        }

        Products product = cartsItem.getProducts();

        if (product != null) {
            Long productId = product.getId();
            product = productsRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product : " + productId + " not found."));
            cartsItem.setProducts(product);
        }


        assert product != null;

        if (!product.isProductStatus()) {
            throw new ProductOutOfStockException("Product not available now.");
        }

        if (cartsItem.getQuantity() > product.getProductQuantity()) {
            throw new ProductOutOfStockException("Product : " + product.getId() + " out of stock.");
        }

        if (cartsItem.getQuantity() == 0) {
            throw new RuntimeException("No quantity set");
        }

        int newProductQuantity = product.getProductQuantity() - cartsItem.getQuantity();
        product.setProductQuantity(newProductQuantity);
        productsRepository.save(product);

        cartsItem.setQuantity(cartsItem.getQuantity());
        cartsItemsRepository.save(cartsItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartsItem);


    }

    @Override
    public ResponseEntity<?> deleteProductFromCart(HttpSession session, Long productId) {

        Carts cart = getOrCreateCart(session);

        if (cart != null) {
            CartsItems productInCart = cartsItemsRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            cart.getCartsItems().remove(productInCart);
            cartsItemsRepository.delete(productInCart);
            cartsRepository.save(cart);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart not found.");
        }
    }

    @Override
    public ResponseEntity<?> clearCart(HttpSession session) {
        Carts cart = getOrCreateCart(session);
        cartsItemsRepository.deleteAll(cart.getCartsItems());
        return ResponseEntity.status(HttpStatus.OK).body("Корзина очищена.");
    }


    @Override
    public Carts getOrCreateCart(HttpSession session) {

        String sessionId = session.getId();

        return cartsRepository.findBySessionId(sessionId).orElseGet(() -> {
            Carts newCart = new Carts();
            newCart.setSessionId(sessionId);
            return cartsRepository.save(newCart);
        });

    }


}
