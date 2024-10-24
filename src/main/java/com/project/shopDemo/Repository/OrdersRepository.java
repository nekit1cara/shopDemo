package com.project.shopDemo.Repository;

import com.project.shopDemo.Entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Long> {


    List<Orders> findBySessionId(String sessionId);

    Optional<Orders> findBySessionIdAndId(String sessionId, Long id);


    List<Orders> findBySessionIdAndOrderStatus(String sessionId, boolean status);



}
