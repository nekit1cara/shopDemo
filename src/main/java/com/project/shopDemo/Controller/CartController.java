package com.project.shopDemo.Controller;

import com.project.shopDemo.Service.Impl.CartServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/shop/cart")
@Tag(name = "Cart", description = "Cart Controller")
public class CartController {

    public CartServiceImpl cartServiceImpl;

    @Autowired
    public CartController(CartServiceImpl cartServiceImpl) {
        this.cartServiceImpl = cartServiceImpl;
    }

    @GetMapping
    @Operation(summary = "Get item in cart")
    public ResponseEntity<?> getCart(HttpSession session) {
        return cartServiceImpl.getItemsFromCart(session);
    }

}
