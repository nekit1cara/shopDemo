package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Carts;
import com.project.shopDemo.Entity.CartsItems;
import com.project.shopDemo.Repository.CartRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartServiceImpl {

    public CartRepository cartsRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartsRepository) {
        this.cartsRepository = cartsRepository;
    }

    //Получаем товары из корзины по сессии юзера
    public ResponseEntity<?> getItemsFromCart(HttpSession session) {

        List<CartsItems> itemsInCart = getOrCreateCart(session).getCartsItems();

            if (itemsInCart != null) {
                return ResponseEntity.status(HttpStatus.OK).body(itemsInCart);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No items in cart");
            }

    }

    //Получить саму корзину или же если её нет то создать и показать
    private Carts getOrCreateCart(HttpSession session) {

        String sessionId = session.getId();

        return cartsRepository.findBySessionId(sessionId).orElseGet(() -> {
            Carts newCart = new Carts();
            newCart.setSessionId(sessionId);
            return cartsRepository.save(newCart);
        });

    }


}
