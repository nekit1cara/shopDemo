package com.project.shopDemo.Service;

import com.project.shopDemo.Entity.Carts;
import com.project.shopDemo.Entity.Orders;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface OrdersService{


    ResponseEntity<?> getOrdersBySession(HttpSession session);

    Optional<Orders> getOrderBySessionAndOrderId(HttpSession session, Long orderId);

    ResponseEntity<?> createOrder(HttpSession session, @RequestBody Orders order);

    ResponseEntity<?> applyOrder(HttpSession session, Long orderId);

    ////////////////////////////////// ///////////////// ///////////////// ///////////////// ///////////////// ///////////////// /////////////////
    List<Orders> getOrders(HttpSession session);

    Optional<Orders> getOrder(HttpSession session, Long orderId);

    int calculateOrderTotalPrice(Carts cart);

    Carts getOrCreateCart(HttpSession session);
}
