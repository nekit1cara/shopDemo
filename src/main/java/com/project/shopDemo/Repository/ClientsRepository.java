package com.project.shopDemo.Repository;

import com.project.shopDemo.Entity.Clients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientsRepository extends JpaRepository<Clients,Long> {

    boolean existsByClientEmail(String email);

}
