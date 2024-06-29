package com.docker3.model;


import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String orderId;
    @Column(nullable = false)
    private String nameOfOrder;
    @Column(nullable = false)
    private LocalDate dateOfOrder;
    @Column
    private String descriptionOfOrder;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Users users;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) return false;
        Orders orders = (Orders) o;
        return Objects.equals(orderId, orders.orderId) &&
                Objects.equals(nameOfOrder, orders.nameOfOrder) &&
                Objects.equals(dateOfOrder, orders.dateOfOrder) &&
                Objects.equals(descriptionOfOrder, orders.descriptionOfOrder) &&
                Objects.equals(users, orders.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, nameOfOrder, descriptionOfOrder, dateOfOrder);
    }
}
