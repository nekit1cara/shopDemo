package com.project.shopDemo.Service.Impl;

import com.project.shopDemo.Entity.*;
import com.project.shopDemo.ExceptionHandler.Exceptions.CartNotFoundException;
import com.project.shopDemo.ExceptionHandler.Exceptions.OrderAlreadyBeAppliedException;
import com.project.shopDemo.ExceptionHandler.Exceptions.OrderNotFoundException;
import com.project.shopDemo.Repository.*;
import com.project.shopDemo.Service.OrdersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersServiceImpl implements OrdersService {

    public CartsItemsRepository cartsItemsRepository;
    public CartRepository cartRepository;
    public OrdersRepository ordersRepository;
    public ClientsRepository clientsRepository;
    public OrderItemsRepository orderItemsRepository;

    @Autowired
    public OrdersServiceImpl(OrdersRepository ordersRepository,
                             ClientsRepository clientsRepository,
                             OrderItemsRepository orderItemsRepository,
                             CartRepository cartRepository,
                             CartsItemsRepository cartsItemsRepository) {
        this.ordersRepository = ordersRepository;
        this.clientsRepository = clientsRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.cartRepository = cartRepository;
        this.cartsItemsRepository = cartsItemsRepository;
    }

    @Override
    public ResponseEntity<?> getOrdersBySession(HttpSession session) {

        List<Orders> orders = getOrders(session);

            if (orders != null) {
                return ResponseEntity.status(HttpStatus.OK).body(orders);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Подтвержденных заказов не найдено."); //наладить тут
            }

    }

    @Override
    public Optional<Orders> getOrderBySessionAndOrderId(HttpSession session, Long orderId) {

        Optional<Orders> order = ordersRepository.findById(orderId);

            if (order.isEmpty()) {
                throw new OrderNotFoundException("Order  " + orderId + "not found");
            }



        return getOrder(session, orderId);
    }

    @Override
    public ResponseEntity<?> createOrder(HttpSession session, @RequestBody Orders order) {

        Carts cart = getOrCreateCart(session);
        List<CartsItems> cartsItems = cart.getCartsItems();

        if (cartsItems == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No cart items found");
        }
        if (order.getClient() == null || order.getClient().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client information is required to create an order");
        }

        Clients client = clientsRepository.findById(order.getClient().getId())
                .orElseThrow(() -> new CartNotFoundException("No client found"));


        order.setSessionId(cart.getSessionId());
        order.setOrderStatus(false);

        // Преобразуем CartsItems в OrderItems
        List<OrderItems> orderItems = new ArrayList<>();

            for (CartsItems cartsItem : cartsItems) {
                OrderItems orderItem = new OrderItems();
                orderItem.setOrders(order);
                orderItem.setProducts(cartsItem.getProducts());
                orderItem.setQuantity(cartsItem.getQuantity());
                orderItem.setPrice(cartsItem.getProducts().getProductPrice() * cartsItem.getQuantity());
                orderItems.add(orderItem);
            }

        order.setClient(client);
        order.setOrderItems(orderItems);
        order.setOrderTotalPrice(calculateOrderTotalPrice(cart));
        ordersRepository.save(order);
        orderItemsRepository.saveAll(orderItems);
        cartsItemsRepository.deleteAll(cartsItems);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);


    }

    @Override
    public ResponseEntity<?> applyOrder(HttpSession session, Long orderId) {

        Optional<Orders> order = getOrder(session, orderId);

            if (order.isEmpty()) {
                throw new OrderNotFoundException("Order  " + orderId + "not found");
            }

        Orders foundedOrder = order.get();
            foundedOrder.setOrderStatus(true);
            ordersRepository.save(foundedOrder);
            return ResponseEntity.status(HttpStatus.OK).body("Заказ успешно подтверждён.");

    }


    @Override
    public ResponseEntity<?> deleteOrderById(HttpSession session, Long orderId) {

        // Получаем заказ по ID и сессии
        Optional<Orders> order = getOrder(session, orderId);

            // Если заказ не найден, выбрасываем исключение
            if (order.isEmpty()) {
                throw new OrderNotFoundException("Order " + orderId + " not found");
            }

        // Извлекаем найденный заказ
        Orders foundedOrder = order.get();

        // Получаем связанные элементы заказа
        List<OrderItems> itemsInCurrentOrder = foundedOrder.getOrderItems();

            if (foundedOrder.isOrderStatus()) {     //Если статус заказа уже - TRUE
                throw new OrderAlreadyBeAppliedException("Заказ : " + orderId + " уже подтвержден.");
            }

            // Удаляем все элементы заказа
            orderItemsRepository.deleteAll(itemsInCurrentOrder);
            // Удаляем сам заказ
            ordersRepository.delete(foundedOrder);

        // Возвращаем успешный ответ
        return ResponseEntity.status(HttpStatus.OK).body("Order " + orderId + " deleted successfully.");
    }



    ////////////////////////////////// ///////////////// ///////////////// ///////////////// ///////////////// ///////////////// /////////////////
    @Override
    public List<Orders> getOrders(HttpSession session) {

        String sessionId = session.getId();
            //return  ordersRepository.findBySessionId(sessionId);
            return ordersRepository.findBySessionIdAndOrderStatus(sessionId, true);

    }

    @Override
    public Optional<Orders> getOrder(HttpSession session, Long orderId) {
        String sessionId = session.getId();
            return ordersRepository.findBySessionIdAndId(sessionId,orderId);
    }

    @Override
    public int calculateOrderTotalPrice(Carts cart) {

        // Возвращаем список всех товаров в корзине как поток (Stream) объектов.
        return cart.getCartsItems().stream()

                // Для каждого товара (CartsItems) в корзине:
                // Вычисляем стоимость этого товара, умножая количество товара (Quantity)
                // на его цену (ProductPrice).
                .mapToInt(cartsItems ->
                        cartsItems.getQuantity() *            // Получаем количество данного товара в корзине.
                                cartsItems.getProducts().getProductPrice() // Получаем цену товара из объекта Products.

                ).sum(); // Суммируем все вычисленные стоимости товаров.


    }

    @Override
    public Carts getOrCreateCart(HttpSession session) {
        String sessionId = session.getId();

            return cartRepository.findBySessionId(sessionId).orElseGet(() -> {
                Carts newCart = new Carts();
                newCart.setSessionId(sessionId);
                return cartRepository.save(newCart);
            });
    }

}
