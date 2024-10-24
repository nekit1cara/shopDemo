package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Carts;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

public interface CartService {


    //Получаем товары из корзины по сессии юзера
    ResponseEntity<?> getItemsFromCart(HttpSession session);

    //Получить саму корзину или же если её нет то создать и показать
    Carts getOrCreateCart(HttpSession session);

}
