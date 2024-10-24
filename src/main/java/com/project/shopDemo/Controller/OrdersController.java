package com.project.shopDemo.Controller;

import com.project.shopDemo.Entity.Orders;
import com.project.shopDemo.Service.Impl.OrdersServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/shop/orders")
@Tag(name = "Orders", description = "Orders Controller")
public class OrdersController {

    public OrdersServiceImpl ordersServiceImpl;

    @Autowired
    public OrdersController(OrdersServiceImpl ordersServiceImpl) {
        this.ordersServiceImpl = ordersServiceImpl;
    }

    @GetMapping
    @Operation(summary = "Get orders if they exist")
    public ResponseEntity<?> getOrders(HttpSession session) {
        return ordersServiceImpl.getOrdersBySession(session);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order if it exist")
    public Optional<Orders> getOrderById(HttpSession session, @PathVariable Long orderId) {
        return ordersServiceImpl.getOrderBySessionAndOrderId(session, orderId);
    }

    @PostMapping("/create-order")
    @Operation(summary = "Create order")
    public ResponseEntity<?> createOrder(HttpSession session, @RequestBody Orders order) {
        return ordersServiceImpl.createOrder(session, order);
    }

    @PutMapping("/apply")
    @Operation(summary = "Apply order if it exist")
    public ResponseEntity<?> applyOrder(HttpSession session, @RequestParam Long orderId) {
        return ordersServiceImpl.applyOrder(session, orderId);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete order by ID")
    public ResponseEntity<?> deleteOrder(HttpSession session, @RequestParam Long orderId) {
        return ordersServiceImpl.deleteOrderById(session, orderId);
    }



}
