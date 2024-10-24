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
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sessionId;

    private LocalDateTime orderDate;
        @PrePersist
        protected void onCreate() {
            this.orderDate = LocalDateTime.now();
        }

    private int orderTotalPrice;
    private boolean orderStatus;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Clients client;

    @JsonManagedReference
    @OneToMany(mappedBy = "orders")
    private List<OrderItems> orderItems;

}
