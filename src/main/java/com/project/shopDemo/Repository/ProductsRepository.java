package com.project.shopDemo.Repository;

import com.project.shopDemo.Entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products,Long> {

    boolean existsByProductModel(String productModel);

    Page<Products> findByProductCategory(String productCategory, Pageable pageable);

    Page<Products> findByProductBrand(String productBrand, Pageable pageable);
}
