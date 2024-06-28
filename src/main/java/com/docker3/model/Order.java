package com.docker3.model;


import org.hibernate.annotations.Cascade;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Order {
    @Id
    @Column(nullable = false)
    private Long orderId;
    @Column(nullable = false)
    private String nameOfOrder;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
}
