package com.project.shopDemo.Repository;

import com.project.shopDemo.Entity.CartsItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartsItemsRepository extends JpaRepository<CartsItems, Long> {
}
