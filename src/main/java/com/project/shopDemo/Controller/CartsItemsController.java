package com.project.shopDemo.Controller;

import com.project.shopDemo.Entity.CartsItems;
import com.project.shopDemo.Service.Impl.CartsItemsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/shop/cart-items")
@Tag(name = "Cart items" , description = "Operations in cart")
public class CartsItemsController {

    public CartsItemsServiceImpl cartsItemsServiceImpl;

    @Autowired
    public CartsItemsController(CartsItemsServiceImpl cartsItemsServiceImpl) {
        this.cartsItemsServiceImpl = cartsItemsServiceImpl;
    }

    @PostMapping("/add-item")
    @Operation(summary = "Add item in cart")
    public ResponseEntity<?> addItemToCart(HttpSession session, @RequestBody CartsItems product) {
        return cartsItemsServiceImpl.addItemInCart(session,product);
    }

    @DeleteMapping("/delete-product")
    @Operation(summary = "Delete item in cart")
    public ResponseEntity<?> deleteItemFromCart(HttpSession session, @RequestParam Long productFromCartId) {
        return cartsItemsServiceImpl.deleteProductFromCart(session,productFromCartId);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Clean all cart")
    public ResponseEntity<?> removeItemFromCart(HttpSession session) {
        return cartsItemsServiceImpl.clearCart(session);
    }

}
