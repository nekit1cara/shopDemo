package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Carts;
import com.project.shopDemo.Entity.CartsItems;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

public interface CartsItemsService {
    ResponseEntity<?> addItemInCart(HttpSession session, CartsItems cartsItem);

    ResponseEntity<?> deleteProductFromCart(HttpSession session, Long productId);

    ResponseEntity<?> clearCart(HttpSession session);

    //Получить саму корзину или же если её нет то создать и показать
    Carts getOrCreateCart(HttpSession session);
}
