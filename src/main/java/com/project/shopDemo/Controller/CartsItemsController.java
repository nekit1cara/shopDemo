package com.project.shopDemo.Controller;

import com.project.shopDemo.Entity.CartsItems;
import com.project.shopDemo.Service.Impl.CartsItemsServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/shop/cart-items")
public class CartsItemsController {

    public CartsItemsServiceImpl cartsItemsServiceImpl;

    @Autowired
    public CartsItemsController(CartsItemsServiceImpl cartsItemsServiceImpl) {
        this.cartsItemsServiceImpl = cartsItemsServiceImpl;
    }

    @PostMapping("/add-item")
    public ResponseEntity<?> addItemToCart(HttpSession session, @RequestBody CartsItems product) {
        return cartsItemsServiceImpl.addItemInCart(session,product);
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteItemFromCart(HttpSession session, @RequestParam Long productFromCartId) {
        return cartsItemsServiceImpl.deleteProductFromCart(session,productFromCartId);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeItemFromCart(HttpSession session) {
        return cartsItemsServiceImpl.clearCart(session);
    }

}
