package com.project.shopDemo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productBrand;
    private String productModel;
    private int productPrice;
    private String productDescription;
    private String productCategory;
    private int productQuantity;


    @JsonIgnore
    @OneToMany(mappedBy = "products")
    private List<CartsItems> cartsItems;

    @JsonIgnore
    @OneToMany(mappedBy = "products")
    private List<OrderItems> orders;


}
