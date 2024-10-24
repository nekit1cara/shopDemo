package com.project.shopDemo.Controller;

import com.project.shopDemo.Entity.Orders;
import com.project.shopDemo.Service.Impl.OrdersServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/shop/orders")
public class OrdersController {

    public OrdersServiceImpl ordersServiceImpl;

    @Autowired
    public OrdersController(OrdersServiceImpl ordersServiceImpl) {
        this.ordersServiceImpl = ordersServiceImpl;
    }

    @GetMapping
    public ResponseEntity<?> getOrder(HttpSession session) {
        return ordersServiceImpl.getOrdersBySession(session);
    }

    @GetMapping("/{orderId}")
    public Optional<Orders> getOrderById(HttpSession session, @PathVariable Long orderId) {
        return ordersServiceImpl.getOrderBySessionAndOrderId(session, orderId);
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(HttpSession session, @RequestBody Orders order) {
        return ordersServiceImpl.createOrder(session, order);
    }

    @PutMapping("/apply")
    public ResponseEntity<?> applyOrder(HttpSession session, @RequestParam Long orderId) {
        return ordersServiceImpl.applyOrder(session, orderId);
    }



}
