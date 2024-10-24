package com.project.shopDemo.Service.Impl;

import com.project.shopDemo.Entity.Carts;
import com.project.shopDemo.Entity.CartsItems;
import com.project.shopDemo.Repository.CartRepository;
import com.project.shopDemo.Service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    public CartRepository cartsRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartsRepository) {
        this.cartsRepository = cartsRepository;
    }

    @Override
    public ResponseEntity<?> getItemsFromCart(HttpSession session) {

        List<CartsItems> itemsInCart = getOrCreateCart(session).getCartsItems();

            if (itemsInCart != null) {
                return ResponseEntity.status(HttpStatus.OK).body(itemsInCart);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No items in cart");
            }

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
