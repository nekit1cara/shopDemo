package com.project.shopDemo.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;

    private LocalDateTime createdDate;
    //Создание текущего времени заранее до создания корзины
    @PrePersist
    protected void onCreate() {this.createdDate = LocalDateTime.now();}

    @JsonManagedReference
    @OneToMany(mappedBy = "carts")
    private List<CartsItems> cartsItems;

//    @JsonIgnore
//    @OneToOne(mappedBy = "carts")
//    private Orders orders;

}
