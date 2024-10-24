package com.project.shopDemo.Repository;

import com.project.shopDemo.Entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Carts, Long> {

    Optional<Carts> findBySessionId(String sessionId);

}