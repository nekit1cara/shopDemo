package com.project.shopDemo.Repository;

import com.project.shopDemo.Entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {


}
